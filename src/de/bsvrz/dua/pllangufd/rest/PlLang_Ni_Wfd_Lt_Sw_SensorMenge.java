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

import java.util.Date;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensorMenge;
import de.bsvrz.dua.pllangufd.VergleichsWertMitAktuellemDatum;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenMessStelle;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenSensor;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;

/**
 * Assoziator fuer eine Menge von NI-, WFD-, LT-, oder SW-Sensoren der Art:<br>
 * Hauptsensor, Vorgaenger, Nachfolger,<br>
 * wobei der Hauptsensor im Sinne der Pl-Pruefung langzeit UFD 
 * ueberprueft wird
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PlLang_Ni_Wfd_Lt_Sw_SensorMenge 
extends AbstraktPlLangSensorMenge<VergleichsWertMitAktuellemDatum>{


	/**
	 * Sensor mit aktuellen Online- und Parameter-Daten
	 */
	private PlLang_Ni_Wfd_Lt_Sw_Sensor onlineSensor = null;
	
	
	/**
	 * Standardkonstruktor
	 *  
	 * @param dav Verbindung zum Datenverteiler
	 * @param messStelle die UFD-Messstelle
	 * @param sensorSelbst der Hauptsensor (der ueberprueft wird)
	 * @param sensorVorgaenger sein Vorgaenger
	 * @param sensorNachfolger sein Nachfolger
	 */
	public PlLang_Ni_Wfd_Lt_Sw_SensorMenge(ClientDavInterface dav,
									 DUAUmfeldDatenMessStelle messStelle,
									 DUAUmfeldDatenSensor sensorSelbst,
								 	 DUAUmfeldDatenSensor sensorVorgaenger, 
								 	 DUAUmfeldDatenSensor sensorNachfolger){
		super(dav, messStelle, sensorSelbst, sensorVorgaenger, sensorNachfolger);
		this.onlineSensor = PlLang_Ni_Wfd_Lt_Sw_Sensor.getInstanz(dav, this.sensorSelbst);
		onlineSensor.addListener(this, true);
		PlLang_Ni_Wfd_Lt_Sw_Sensor.getInstanz(dav, this.vorgaengerObj).addListener(this, true);
		PlLang_Ni_Wfd_Lt_Sw_Sensor.getInstanz(dav, this.nachfolgerObj).addListener(this, true);
	}


	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereDaten(VergleichsWertMitAktuellemDatum datum) {
		if(datum != null && datum.getAktuellenWert() != null){
			if(datum.getAktuellenWert().getObject().equals(this.sensorSelbst)){
				this.aktuellesSensorDatum = datum;
			}else
			if(datum.getAktuellenWert().getObject().equals(this.nachfolgerObj)){
				this.aktuellesNachfolgerDatum = datum;
			}else
			if(datum.getAktuellenWert().getObject().equals(this.vorgaengerObj)){
				this.aktuellesVorgaengerDatum = datum;
			}
		}
		
		UfdsLangZeitPlPruefungsParameter parameter = this.onlineSensor.getAktuelleParameter();
		
		if(parameter != null && parameter.isValid() && parameter.getMaxAbweichung().isOk()){
			
			double abweichung24 = this.getAbweichung(true);
			if(abweichung24 != Double.NaN){
				synchronized (this) {
					if(abweichung24 <= parameter.getMaxAbweichung().getSkaliertenWert()){
						long start = -1; // TODO
						long ende = -1; // TODO
						
						String vergleichsZeitBereich = DUAKonstanten.BM_ZEIT_FORMAT.format(new Date(start)) + " - " +  //$NON-NLS-1$
													DUAKonstanten.BM_ZEIT_FORMAT.format(new Date(ende)) + " (24 Stunden)"; //$NON-NLS-1$
						
						this.sendeBetriebsmeldung(this.sensorSelbst, "Der Wert " +  //$NON-NLS-1$
								UmfeldDatenArt.getUmfeldDatenArtVon(this.sensorSelbst) + 
								" für die Messstelle " + this.messStelle + " weicht um " //$NON-NLS-1$ //$NON-NLS-2$
								+ abweichung24 + " (>" + parameter.getMaxAbweichung().getSkaliertenWert() +  //$NON-NLS-1$
								") vom erwarteten Vergleichswert im Vergleichszeitbereich " + //$NON-NLS-1$
								"" + vergleichsZeitBereich + " ab.", //$NON-NLS-1$ //$NON-NLS-2$
								"Langzeitmessfehler Umfelddaten (24h)"); //$NON-NLS-1$										
					}
				}				
			}else{
				this.sendeBetriebsmeldung(this.sensorSelbst, "Die Plausibilitätsprüfung zur " +  //$NON-NLS-1$
						UmfeldDatenArt.getUmfeldDatenArtVon(this.sensorSelbst) + 
						" für die Messstelle " + this.messStelle + " konnte nicht durchgeführt werden," + //$NON-NLS-1$ //$NON-NLS-2$
				" da ein Vergleichswert nicht bestimmt werden konnte.", "Langzeit-Pl-Prüfung (24h)"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			double abweichung = this.getAbweichung(false);
			if(abweichung != Double.NaN){
				synchronized (this) {
					if(abweichung <= parameter.getMaxAbweichung().getSkaliertenWert()){
						long start = -1; // TODO
						long ende = -1; // TODO
						
						String vergleichsZeitBereich = DUAKonstanten.BM_ZEIT_FORMAT.format(new Date(start)) + " - " +  //$NON-NLS-1$
													DUAKonstanten.BM_ZEIT_FORMAT.format(new Date(ende)) + " (" + //$NON-NLS-1$
													parameter.getVergleichsIntervall() + ")"; //$NON-NLS-1$
						
						this.sendeBetriebsmeldung(this.sensorSelbst, "Der Wert " +  //$NON-NLS-1$
								UmfeldDatenArt.getUmfeldDatenArtVon(this.sensorSelbst) + 
								" für die Messstelle " + this.messStelle + " weicht um " //$NON-NLS-1$ //$NON-NLS-2$
								+ abweichung + " (>" + parameter.getMaxAbweichung().getSkaliertenWert() +  //$NON-NLS-1$
								") vom erwarteten Vergleichswert im Vergleichszeitbereich " + //$NON-NLS-1$
								"" + vergleichsZeitBereich + " ab.", //$NON-NLS-1$ //$NON-NLS-2$
								"Langzeitmessfehler Umfelddaten"); //$NON-NLS-1$																
					}					
				}
			}else{
				this.sendeBetriebsmeldung(this.sensorSelbst, "Die Plausibilitätsprüfung zur " +  //$NON-NLS-1$
						UmfeldDatenArt.getUmfeldDatenArtVon(this.sensorSelbst) + 
						" für die Messstelle " + this.messStelle + " konnte nicht durchgeführt werden," + //$NON-NLS-1$ //$NON-NLS-2$
				" da ein Vergleichswert nicht bestimmt werden konnte.", "Langzeit-Pl-Prüfung"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	
	/**
	 * Berechnet die Abweichung analog Afo-4.0, S.108
	 * 
	 * @param intervall24 ob die Abweichung fuer das Bezugsintervall von 24h 
	 * berechnet werden soll (sonst wird fuer das parametrierbare Bezugsintervall
	 * berechnet)
	 * @return die Abweichung analog Afo-4.0, S.108
	 */
	private final synchronized double getAbweichung(final boolean intervall24){
		double abweichung = Double.NaN;

		if(this.aktuellesSensorDatum != null && 
				this.aktuellesVorgaengerDatum != null &&
				this.aktuellesNachfolgerDatum != null){
			double vergleichsWertPruefling = Double.NaN;
			double vergleichsWertVorgaenger = Double.NaN;
			double vergleichsWertNachfolger = Double.NaN;
			
			if(intervall24){
				if(this.aktuellesSensorDatum.isValid24() && 
						this.aktuellesVorgaengerDatum.isValid24() &&
						this.aktuellesNachfolgerDatum.isValid24()){
					vergleichsWertPruefling = this.aktuellesSensorDatum.getVergleichsWert24();
					vergleichsWertVorgaenger = this.aktuellesVorgaengerDatum.getVergleichsWert24();
					vergleichsWertNachfolger = this.aktuellesNachfolgerDatum.getVergleichsWert24();
				}
			}else{
				if(this.aktuellesSensorDatum.isValid() && 
						this.aktuellesVorgaengerDatum.isValid() &&
						this.aktuellesNachfolgerDatum.isValid()){
					vergleichsWertPruefling = this.aktuellesSensorDatum.getVergleichsWert();
					vergleichsWertVorgaenger = this.aktuellesVorgaengerDatum.getVergleichsWert();
					vergleichsWertNachfolger = this.aktuellesNachfolgerDatum.getVergleichsWert();					
				}				
			}
			
			if(vergleichsWertPruefling != Double.NaN && 
				vergleichsWertVorgaenger != Double.NaN && 
				vergleichsWertNachfolger != Double.NaN){
				abweichung = Math.abs(vergleichsWertPruefling - ((vergleichsWertVorgaenger + vergleichsWertNachfolger) / 2.0));
			}
		}
		
		return abweichung;
	}
}
