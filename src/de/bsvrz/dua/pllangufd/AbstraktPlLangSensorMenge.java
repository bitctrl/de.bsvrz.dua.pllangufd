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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenMessStelle;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenSensor;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.IOnlineUfdSensorListener;
import de.bsvrz.sys.funclib.operatingMessage.MessageCauser;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;


/**
 * Abstraktes Rohgeruest fuer eine Menge von Sensoren der Art:<br>
 * Hauptsensor (Pruefling), Vorgaenger, Nachfolger,<br>
 * wobei der Hauptsensor im Sinne der Pl-Pruefung langzeit UFD 
 * ueberprueft wird
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktPlLangSensorMenge<G>
implements IOnlineUfdSensorListener<ResultData>{
	
	/**
	 * <code>Langzeit-Pl-Prüfung</code>
	 */
	protected static final String LZ_PL_PR = "Langzeit-Pl-Prüfung"; //$NON-NLS-1$
	
	/**
	 * <code>Langzeit-Pl-Prüfung (24h)</code>
	 */
	protected static final String LZ_PL_PR24 = "Langzeit-Pl-Prüfung (24h)"; //$NON-NLS-1$
	
	/**
	 * <code>Langzeitmessfehler Umfelddaten</code>
	 */
	protected static final String LZMF_UFD = "Langzeitmessfehler Umfelddaten"; //$NON-NLS-1$

	/**
	 * <code>Langzeitmessfehler Umfelddaten (24h)</code>
	 */
	protected static final String LZMF_UFD24 = "Langzeitmessfehler Umfelddaten (24h)"; //$NON-NLS-1$
	
	/**
	 * statische Verbindung zum Datenverteiler
	 */
	protected static ClientDavInterface DAV = null;

	/**
	 * Vorgaenger-Sensor mit aktuellen Online-Daten
	 */
	protected AbstraktPlLangSensor<G> vorgaengerSensor = null;

	/**
	 * Nachfolger-Sensor mit aktuellen Online-Daten
	 */
	protected AbstraktPlLangSensor<G> nachfolgerSensor = null;

	/**
	 * Pruefling mit aktuellen Online-Daten
	 */
	protected AbstraktPlLangSensor<G> prueflingSensor = null;
		
	/**
	 * Messstelle, zu der der Pruefling gehoert
	 */
	protected SystemObject messStelle = null; 
	
	/**
	 * Bildet den Nachrichtenzusatz auf die letzte Datenzeit ab, 
	 * fuer die eine Nachricht mit diesem Zusatz publiziert worden ist
	 */
	private Map<String, Long> zusatzAufLetzteDatenzeit = Collections.synchronizedMap(new HashMap<String, Long>());


	
	/**
	 * Erfragt eine statische Instanz des Online-Sensors, der mit dem uebergebenen
	 * Systemobjekt assoziiert ist
	 * 
	 * @param objekt ein Systemobjekt eines Umfelddatensensors
	 * @return eine statische Instanz des Online-Sensors, der mit dem uebergebenen
	 * Systemobjekt assoziiert ist
	 */
	protected abstract AbstraktPlLangSensor<G> getSensorInstanz(final SystemObject objekt);
	
	
	/**
	 * Initialisiert dieses Objekt (Instanziierung und Anmeldung der einzelnen
	 * Sensoren auf Daten und Parameter usw.)
	 *  
	 * @param dav Verbindung zum Datenverteiler
	 * @param messStelle die UFD-Messstelle des Prueflings
	 * @param sensorSelbst der Hauptsensor (der ueberprueft wird)
	 * @param sensorVorgaenger sein Vorgaenger
	 * @param sensorNachfolger sein Nachfolger
	 */
	public final void initialisiere(ClientDavInterface dav,
			 DUAUmfeldDatenMessStelle messStelle,
			 DUAUmfeldDatenSensor sensorSelbst,
		 	 DUAUmfeldDatenSensor sensorVorgaenger, 
		 	 DUAUmfeldDatenSensor sensorNachfolger){
		if(DAV == null){
			DAV = dav;
		}
		this.messStelle = messStelle.getObjekt();
		
		this.prueflingSensor = this.getSensorInstanz(sensorSelbst.getObjekt());
		this.vorgaengerSensor = this.getSensorInstanz(sensorVorgaenger.getObjekt());
		this.nachfolgerSensor = this.getSensorInstanz(sensorNachfolger.getObjekt());
		
		this.prueflingSensor.addListener(this, true);
		this.vorgaengerSensor.addListener(this, true);
		this.nachfolgerSensor.addListener(this, true);		
	}
	
	
	/**
	 * Sendet eine Betriebsmeldung als Warnung an den Operator.
	 * <br><b>Achtung:</b> Es koennen nur zwei unterschiedliche
	 * Nachrichten in Folge versendet werden (Zeitstempel)
	 * 
	 * @param objekt das betroffene Systemobjekt
	 * @param nachricht der Nachrichtentext
	 * @param zusatz ein Nachrichtenzusatz
	 * @param datenzeit der Zeitstempel des Datums, das diese Fehlermeldung provoziert hat
	 */
	protected final void sendeBetriebsmeldung(SystemObject objekt,
											  String nachricht,
											  String zusatz,
											  long datenzeit){
		
		synchronized (this.zusatzAufLetzteDatenzeit) {
			if(this.zusatzAufLetzteDatenzeit.get(zusatz) == null ||
					this.zusatzAufLetzteDatenzeit.get(zusatz) != datenzeit){

				this.zusatzAufLetzteDatenzeit.put(zusatz, datenzeit);
				MessageSender nachrichtenSender = MessageSender.getInstance();
				nachrichtenSender.sendMessage(
						MessageType.APPLICATION_DOMAIN, 
						zusatz,
						MessageGrade.WARNING,
						objekt,
						new MessageCauser(DAV.getLocalUser(), Constants.EMPTY_STRING, "Pl-Prüfung langzeit UFD"), //$NON-NLS-1$
						nachricht);
			}			
		}
	}

}
