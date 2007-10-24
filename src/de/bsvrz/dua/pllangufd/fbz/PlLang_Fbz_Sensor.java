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

package de.bsvrz.dua.pllangufd.fbz;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensor;
import de.bsvrz.dua.pllangufd.VergleichsEreignisWerteMitAktuellemDatum;
import de.bsvrz.dua.pllangufd.historie.HistorischerUfdsWert;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;

/**
 * Sensor, der die aktuellen Daten eines FBZ-Sensors zu Vergleichswerten im
 * Sinne der Pl-Pruefung langzeit UFD verarbeitet
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PlLang_Fbz_Sensor
extends AbstraktPlLangSensor<VergleichsEreignisWerteMitAktuellemDatum>{

	/**
	 * statische Instanzen dieser Klasse
	 */
	private static final Map<SystemObject, PlLang_Fbz_Sensor> INSTANZEN = new HashMap<SystemObject, PlLang_Fbz_Sensor>();
	
	
	/**
	 * Erfragt eine statische Instanz dieser Klasse
	 * 
	 * @param dav Datenverteiler-Verbindung
	 * @param objekt ein Systemobjekt eines Umfelddatensensors (<code>!= null</code>)
	 * @return eine statische Instanz dieser Klasse
	 */
	public static final PlLang_Fbz_Sensor getInstanz(final ClientDavInterface dav, 
													 final SystemObject objekt){
		if(objekt == null){
			throw new NullPointerException("Sensor-Objekt ist <<null>>"); //$NON-NLS-1$
		}
		PlLang_Fbz_Sensor instanz = INSTANZEN.get(objekt);
		
		if(instanz == null){
			instanz = new PlLang_Fbz_Sensor();
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
		this.onlineWert = new VergleichsEreignisWerteMitAktuellemDatum(resultat);

		HistorischerUfdsWert historischerWert = new HistorischerUfdsWert(resultat);
		this.hitorie24.addDatum(historischerWert);

		synchronized (this) {
			/**
			 * berechne Vergleichswerte fuer parametriertes Vergleichsintervall
			 */
			if(this.aktuelleParameter != null && this.aktuelleParameter.isValid()){					
				SortedSet<HistorischerUfdsWert> historieVergleich = 
					this.hitorie24.getPufferInhalt(this.aktuelleParameter.getVergleichsIntervall().getMillis());
				
				if(!historieVergleich.isEmpty()){
					this.onlineWert.setVergleichsWerte(
							getVergleichsWerte(historieVergleich, FahrBahnZustandsEreignis.getInstanzen()));
				}


				/**
				 * berechne Vergleichswerte fuer letzte 24h
				 */
				if(!this.hitorie24.getPufferInhalt().isEmpty()){
					this.onlineWert.setVergleichsWerte24(
							getVergleichsWerte(this.hitorie24.getPufferInhalt(),
									FahrBahnZustandsEreignis.getInstanzen()));
				}
			}
		}
	}
	
}