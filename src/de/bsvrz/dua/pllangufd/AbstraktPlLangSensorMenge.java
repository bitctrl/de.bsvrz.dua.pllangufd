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

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenMessStelle;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenSensor;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.IOnlineUfdSensorListener;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;
import de.bsvrz.sys.funclib.operatingMessage.MessageCauser;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;


/**
 * Abstraktes Geruest fuer eine Menge von Sensoren der Art:<br>
 * Hauptsensor, Vorgaenger, Nachfolger,<br>
 * wobei der Hauptsensor im Sinne der Pl-Pruefung langzeit UFD 
 * ueberprueft wird
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktPlLangSensorMenge<G>
implements IOnlineUfdSensorListener<G>{
	
	/**
	 * statische Datenverteiler-Verbindung
	 */
	protected static ClientDavInterface DAV = null;

	/**
	 * aktuelles Vorgaenger-Datum
	 */
	protected G aktuellesVorgaengerDatum = null;

	/**
	 * aktuelles Nachfolger-Datum
	 */
	protected G aktuellesNachfolgerDatum = null;

	/**
	 * aktuelles Sensor-Datum
	 */
	protected G aktuellesSensorDatum = null;
	
	/**
	 * Messstelle
	 */
	protected SystemObject messStelle = null;

	/**
	 * Vorgaenger
	 */
	protected SystemObject vorgaengerObj = null;

	/**
	 * Nachfolger
	 */
	protected SystemObject nachfolgerObj = null;

	/**
	 * Sensor selbst
	 */
	protected SystemObject sensorSelbst = null;

	
	/**
	 * Standardkonstruktor
	 *  
	 * @param dav Verbindung zum Datenverteiler
	 * @param messStelle die UFD-Messstelle
	 * @param sensorSelbst der Hauptsensor (der ueberprueft wird)
	 * @param sensorVorgaenger sein Vorgaenger
	 * @param sensorNachfolger sein Nachfolger
	 */
	public AbstraktPlLangSensorMenge(ClientDavInterface dav,
									 DUAUmfeldDatenMessStelle messStelle,
									 DUAUmfeldDatenSensor sensorSelbst,
								 	 DUAUmfeldDatenSensor sensorVorgaenger, 
								 	 DUAUmfeldDatenSensor sensorNachfolger){
		if(DAV == null){
			DAV = dav;
		}
		this.messStelle = messStelle.getObjekt();
		this.sensorSelbst = sensorSelbst.getObjekt();
		this.vorgaengerObj = sensorVorgaenger.getObjekt();
		this.nachfolgerObj = sensorNachfolger.getObjekt();
		if(this.sensorSelbst == null || 
		   this.vorgaengerObj == null || 
		   this.nachfolgerObj == null){
			throw new NullPointerException("Sensormenge kann nicht initialisiert werden." + //$NON-NLS-1$
					" Einer der Sensoren ist <<null>>"); //$NON-NLS-1$
		}
	}
	
	
	/**
	 * Sendet eine Betriebsmeldung als Warnung an den Operator
	 * 
	 * @param nachricht der Nachrichtentext
	 */
	protected final void sendeBetriebsmeldung(SystemObject objekt, String nachricht, String zusatz){
		MessageSender nachrichtenSender = MessageSender.getInstance();
		nachrichtenSender.sendMessage(
				MessageType.APPLICATION_DOMAIN, 
				zusatz,
				MessageGrade.WARNING,
				objekt,
				new MessageCauser(DAV.getLocalUser(), Konstante.LEERSTRING, "Pl-Pruefung langzeit UFD"), //$NON-NLS-1$
				nachricht);
	}

}
