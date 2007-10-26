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

package de.bsvrz.dua.pllangufd;

import java.util.Map;
import java.util.Set;

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Abstrakter Assoziator fuer eine Menge von NS- bzw. FBZ-Sensoren der Art:<br>
 * Hauptsensor, Vorgaenger, Nachfolger,<br>
 * wobei der Hauptsensor im Sinne der Pl-Pruefung langzeit UFD 
 * ueberprueft wird
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktPlLangEreignisSensorMenge 
extends AbstraktPlLangSensorMenge<VergleichsEreignisWerte>{
		
	
	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereDaten(ResultData datum) {
		synchronized (this) {
			VergleichsEreignisWerte aktuellesSensorDatum = 
				this.prueflingSensor.getAktuellenVergleichsWert(datum.getDataTime());
			
			VergleichsEreignisWerte aktuellesNachfolgerDatum = 
				this.nachfolgerSensor.getAktuellenVergleichsWert(datum.getDataTime());
			
			VergleichsEreignisWerte aktuellesVorgaengerDatum = 
				this.vorgaengerSensor.getAktuellenVergleichsWert(datum.getDataTime());		
			
			if(aktuellesSensorDatum != null &&
				aktuellesNachfolgerDatum != null &&
				aktuellesVorgaengerDatum != null){
				
				UfdsLangZeitPlPruefungsParameter parameter = this.prueflingSensor.getAktuelleParameter();
				
				if(parameter != null &&
					parameter.isValid() && 
					parameter.getMaxAbweichung().isOk()){
					
					/**
					 * parametrierter Bezugszeitraum
					 */
					if(aktuellesSensorDatum.getAusfall() > 0 &&
						parameter.getMaxAusfallZeit() > 0 &&
						aktuellesSensorDatum.getAusfall() > parameter.getMaxAusfallZeit()){
						this.sendeBetriebsmeldung(this.messStelle, "Die Plausibilitätsprüfung zur " + //$NON-NLS-1$
								UmfeldDatenArt.getUmfeldDatenArtVon(this.prueflingSensor.getObjekt()) + " für die Messstelle " + //$NON-NLS-1$ 
								this.messStelle + " konnte nicht durchgeführt werden, da keine" + //$NON-NLS-1$
										" ausreichende Datenbasis vorlag", //$NON-NLS-1$
								"Langzeit-PL-Prüfung"); //$NON-NLS-1$
					}else{
						double abweichung = this.getAbweichung(false, aktuellesSensorDatum, 
																aktuellesVorgaengerDatum, aktuellesNachfolgerDatum);
						if(abweichung >= 0 && abweichung > parameter.getMaxAbweichung().getWert()){
							this.sendeBetriebsmeldung(this.messStelle, "Der Wert " + //$NON-NLS-1$
									UmfeldDatenArt.getUmfeldDatenArtVon(this.prueflingSensor.getObjekt()) + " für die Messstelle " + //$NON-NLS-1$ 
									this.messStelle + " weicht um " + abweichung + " (>" + parameter.getMaxAbweichung().getWert() +  //$NON-NLS-1$ //$NON-NLS-2$ 
									") vom erwarteten Vergleichswert im Vergleichszeitbereich " +  //$NON-NLS-1$ 
									DUAKonstanten.BM_ZEIT_FORMAT.format(datum.getDataTime() - parameter.getVergleichsIntervall().getMillis()) + " - " +   //$NON-NLS-1$
									DUAKonstanten.BM_ZEIT_FORMAT.format(datum.getDataTime()) + 
									"(" + parameter.getVergleichsIntervall() + ") ab.",   //$NON-NLS-1$ //$NON-NLS-2$
									"Langzeitmessfehler Umfelddaten"); //$NON-NLS-1$						
						}
					}

					
					/**
					 * 24h Bezugszeitraum
					 */
					if(aktuellesSensorDatum.getAusfall24() > 0 &&
						parameter.getMaxAusfallZeit() > 0 &&
						aktuellesSensorDatum.getAusfall24() > parameter.getMaxAusfallZeit()){
							this.sendeBetriebsmeldung(this.messStelle, "Die Plausibilitätsprüfung zur " + //$NON-NLS-1$
									UmfeldDatenArt.getUmfeldDatenArtVon(this.prueflingSensor.getObjekt()) + " für die Messstelle " + //$NON-NLS-1$ 
									this.messStelle + " konnte nicht durchgeführt werden, da keine" + //$NON-NLS-1$
											" ausreichende Datenbasis vorlag", //$NON-NLS-1$
									"Langzeit-PL-Prüfung (24h)"); //$NON-NLS-1$
						}else{
							double abweichung = this.getAbweichung(true, aktuellesSensorDatum, 
									aktuellesVorgaengerDatum, aktuellesNachfolgerDatum);
							if(abweichung >= 0 && abweichung > parameter.getMaxAbweichung().getWert()){
								this.sendeBetriebsmeldung(this.messStelle, "Der Wert " + //$NON-NLS-1$
										UmfeldDatenArt.getUmfeldDatenArtVon(this.prueflingSensor.getObjekt()) + " für die Messstelle " + //$NON-NLS-1$ 
										this.messStelle + " weicht um " + abweichung + " (>" + parameter.getMaxAbweichung().getWert() +  //$NON-NLS-1$ //$NON-NLS-2$ 
										") vom erwarteten Vergleichswert im Vergleichszeitbereich " +  //$NON-NLS-1$ 
										DUAKonstanten.BM_ZEIT_FORMAT.format(datum.getDataTime() - Konstante.TAG_24_IN_MS) + " - " +   //$NON-NLS-1$
										DUAKonstanten.BM_ZEIT_FORMAT.format(datum.getDataTime()) + 
										"(24 Stunden) ab.",   //$NON-NLS-1$
										"Langzeitmessfehler Umfelddaten (24h)"); //$NON-NLS-1$						
							}
						}
				}
			}
		}		
	}
	
	
	/**
	 * Errechnet den Wert AbweichungXY auf Basis der aktuellen Vergleichswerte
	 * pro XY-Ereignis aller assoziierten Sensoren.<br>
	 * Siehe Afo-4.0 6.6.2.4.7.6 (NS, S. 109)
	 * 
	 * @param intervall24 ob die Abweichung fuer das Bezugsintervall von 24h 
	 * berechnet werden soll (sonst wird fuer das parametrierbare Bezugsintervall
	 * berechnet)
	 * @param aktuellesSensorDatum aktuelle Daten des Sensor-Prueflings
	 * @param aktuellesVorgaengerDatum aktuelle Daten des Vorgaengers
	 * @param aktuellesNachfolgerDatum aktuelle Daten des Nachfolgers
	 * @return der Wert AbweichungXY (>= 0) auf Basis der aktuellen Vergleichswerte
	 * pro XY-Ereignis aller assoziierten Sensoren oder ein Wert < 0, wenn die
	 * AbweichungXY nicht ermittelt werden konnte
	 */
	private final double getAbweichung(final boolean intervall24,
									VergleichsEreignisWerte aktuellesSensorDatum,
									VergleichsEreignisWerte aktuellesVorgaengerDatum,
									VergleichsEreignisWerte aktuellesNachfolgerDatum){
		double ergebnis = Double.MIN_VALUE;
		
		if(aktuellesSensorDatum != null &&
			aktuellesNachfolgerDatum != null && 
			aktuellesVorgaengerDatum != null){

			Map<AbstraktEreignis, Double> sensorVergleichsWerte = null;
			Map<AbstraktEreignis, Double> nachfolgerVergleichsWerte = null;
			Map<AbstraktEreignis, Double> vorgaengerVergleichsWerte = null;
			if(intervall24){
				sensorVergleichsWerte = aktuellesSensorDatum.getVergleichsWerte24();
				nachfolgerVergleichsWerte = aktuellesNachfolgerDatum.getVergleichsWerte24();
				vorgaengerVergleichsWerte = aktuellesVorgaengerDatum.getVergleichsWerte24();
			}else{
				sensorVergleichsWerte = aktuellesSensorDatum.getVergleichsWerte();
				nachfolgerVergleichsWerte = aktuellesNachfolgerDatum.getVergleichsWerte();
				vorgaengerVergleichsWerte = aktuellesVorgaengerDatum.getVergleichsWerte();
			}
			
			for(AbstraktEreignis ereignis:getEreignisInstanzen()){
				Double sensorVergleichsWert = sensorVergleichsWerte.get(ereignis);
				Double vorgaengerVergleichsWert = vorgaengerVergleichsWerte.get(ereignis);
				Double nachfolgerVergleichsWert = nachfolgerVergleichsWerte.get(ereignis);
				
				if(sensorVergleichsWert != null &&
					vorgaengerVergleichsWert != null &&
					nachfolgerVergleichsWert != null){
					double ereignisAbweichung = Math.abs(sensorVergleichsWert - 
								((vorgaengerVergleichsWert + nachfolgerVergleichsWert) / 2));
					if(ereignisAbweichung > ergebnis){
						ergebnis = ereignisAbweichung;
					}
				}
			}
		}
		
		return ergebnis;
	}

	
	/**
	 * Erfragt die Menge von Ereignissen, fuer die die Abweichung bzw. ueber denen
	 * die Vergleichswerte berechnet werden sollen
	 * 
	 * @return die Menge von Ereignissen, fuer die die Abweichung bzw. ueber denen
	 * die Vergleichswerte berechnet werden sollen
	 */
	protected abstract Set<? extends AbstraktEreignis> getEreignisInstanzen();
	
}
