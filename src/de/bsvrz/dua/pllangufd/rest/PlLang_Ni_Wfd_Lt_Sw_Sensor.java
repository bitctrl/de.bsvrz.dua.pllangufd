/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.13 PL-Pruefung Langzeit UFD
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.pllangufd.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensor;
import de.bsvrz.dua.pllangufd.VergleichsWertMitAktuellemDatum;
import de.bsvrz.dua.pllangufd.historie.HistorischerUfdsWert;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;

/**
 * Sensor, der die aktuellen Daten eines NI-, WFD-, LT-, oder SW-Sensors
 * zu Vergleichswerten im Sinne der Pl-Pruefung langzeit UFD verarbeitet
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PlLang_Ni_Wfd_Lt_Sw_Sensor
extends AbstraktPlLangSensor<VergleichsWertMitAktuellemDatum>{

	/**
	 * statische Instanzen dieser Klasse
	 */
	private static final Map<SystemObject, PlLang_Ni_Wfd_Lt_Sw_Sensor> INSTANZEN
								= new HashMap<SystemObject, PlLang_Ni_Wfd_Lt_Sw_Sensor>();
	
	
	/**
	 * Erfragt eine statische Instanz dieser Klasse
	 * 
	 * @param dav Datenverteiler-Verbindung
	 * @param objekt ein Systemobjekt eines Umfelddatensensors (<code>!= null</code>)
	 * @return eine statische Instanz dieser Klasse
	 */
	public static final PlLang_Ni_Wfd_Lt_Sw_Sensor getInstanz(final ClientDavInterface dav, 
															  final SystemObject objekt){
		if(objekt == null){
			throw new NullPointerException("Sensor-Objekt ist <<null>>"); //$NON-NLS-1$
		}
		PlLang_Ni_Wfd_Lt_Sw_Sensor instanz = INSTANZEN.get(objekt);
		
		if(instanz == null){
			instanz = new PlLang_Ni_Wfd_Lt_Sw_Sensor();
			instanz.initialisiere(dav, objekt, dav.getDataModel().getAspect(DUAKonstanten.ASP_MESSWERTERSETZUNG));
			INSTANZEN.put(objekt, instanz);
		}
		
		return instanz;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void berechneOnlineWert(ResultData resultat) {
		this.onlineWert = new VergleichsWertMitAktuellemDatum(resultat);

		HistorischerUfdsWert historischerWert = new HistorischerUfdsWert(resultat);
		this.hitorie24.addDatum(historischerWert);

		synchronized (this) {
			/**
			 * berechne Vergleichswert fuer parametriertes Vergleichsintervall
			 */
			if(this.aktuelleParameter != null && this.aktuelleParameter.isValid()){					
				double ergebnis = Double.NaN;

				SortedSet<HistorischerUfdsWert> historieVergleich = 
					this.hitorie24.getPufferInhalt(this.aktuelleParameter.getVergleichsIntervall().getMillis());
				if(!historieVergleich.isEmpty()){	
					long summeD = -1;
					long summeWmalD = -1;
					long summeA = -1;
					for(HistorischerUfdsWert wert:historieVergleich){
						if(wert.getWert().isOk()){
							if(summeD == -1){
								summeD = 0;	// Initialisierung
								summeWmalD = 0;	// Initialisierung
							}
							summeD += wert.getT();
							summeWmalD += wert.getWert().getWert() * wert.getT(); 
						}
					}

					if(summeD >= 0){
						summeA = this.hitorie24.getIntervallLaenge() - summeD;
						if(summeA < 0){
							summeA = 0;

							/**
							 * TODO: wieder raus
							 */
							throw new RuntimeException("---------------------------"); //$NON-NLS-1$
						}

						double anteilAusfall = ((double)summeA) / ((double)this.hitorie24.getIntervallLaenge());

						if(anteilAusfall <= aktuelleParameter.getMaxAusfallZeit()){
							ergebnis = ((double)summeWmalD)/((double)summeD);
						}
					}
				}	

				this.onlineWert.setVergleichsWert( ergebnis );


				/**
				 * berechne Vergleichswert fuer letzte 24h
				 */
				double ergebnis24 = Double.NaN;

				if(!this.hitorie24.getPufferInhalt().isEmpty()){	
					long summeD24 = -1;
					long summeWmalD24 = -1;
					long summeA24 = -1;
					for(HistorischerUfdsWert wert:this.hitorie24.getPufferInhalt()){
						if(wert.getWert().isOk()){
							if(summeD24 == -1){
								summeD24 = 0;	// Initialisierung
								summeWmalD24 = 0;	// Initialisierung
							}
							summeD24 += wert.getT();
							summeWmalD24 += wert.getWert().getWert() * wert.getT(); 
						}
					}

					if(summeD24 >= 0){
						summeA24 = this.hitorie24.getIntervallLaenge() - summeD24;
						if(summeA24 < 0){
							summeA24 = 0;

							/**
							 * TODO: wieder raus
							 */
							throw new RuntimeException("---------------------------"); //$NON-NLS-1$
						}

						double anteilAusfall = ((double)summeA24) / ((double)this.hitorie24.getIntervallLaenge());

						if(anteilAusfall <= aktuelleParameter.getMaxAusfallZeit()){
							ergebnis24 = ((double)summeWmalD24)/((double)summeD24);
						}
					}
				}	

				this.onlineWert.setVergleichsWert24( ergebnis24 );
			}
		}

	}
	
}