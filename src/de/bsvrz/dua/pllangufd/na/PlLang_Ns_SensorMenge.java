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

package de.bsvrz.dua.pllangufd.na;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensorMenge;
import de.bsvrz.dua.pllangufd.VergleichsEreignisWerteMitAktuellemDatum;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenMessStelle;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenSensor;

/**
 * Assoziator fuer eine Menge von NS-Sensoren der Art:<br>
 * Hauptsensor, Vorgaenger, Nachfolger,<br>
 * wobei der Hauptsensor im Sinne der Pl-Pruefung langzeit UFD 
 * ueberprueft wird
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PlLang_Ns_SensorMenge 
extends AbstraktPlLangSensorMenge<VergleichsEreignisWerteMitAktuellemDatum>{

	/**
	 * Sensor mit aktuellen Online- und Parameter-Daten
	 */
	private PlLang_Ns_Sensor onlineSensor = null;
	
	
	/**
	 * Standardkonstruktor
	 *  
	 * @param dav Verbindung zum Datenverteiler
	 * @param messStelle die UFD-Messstelle
	 * @param sensorSelbst der Hauptsensor (der ueberprueft wird)
	 * @param sensorVorgaenger sein Vorgaenger
	 * @param sensorNachfolger sein Nachfolger
	 */
	public PlLang_Ns_SensorMenge(ClientDavInterface dav,
									 DUAUmfeldDatenMessStelle messStelle,
									 DUAUmfeldDatenSensor sensorSelbst,
								 	 DUAUmfeldDatenSensor sensorVorgaenger, 
								 	 DUAUmfeldDatenSensor sensorNachfolger){
		super(dav, messStelle, sensorSelbst, sensorVorgaenger, sensorNachfolger);
		this.onlineSensor = PlLang_Ns_Sensor.getInstanz(dav, this.sensorSelbst);
		onlineSensor.addListener(this, true);
		PlLang_Ns_Sensor.getInstanz(dav, this.vorgaengerObj).addListener(this, true);
		PlLang_Ns_Sensor.getInstanz(dav, this.nachfolgerObj).addListener(this, true);
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereDaten(VergleichsEreignisWerteMitAktuellemDatum datum) {
		synchronized (this) {
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
				
				
				/****
				 * 
				 * TODODO
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 */
				
				
			}
		}		
	}
	
}
