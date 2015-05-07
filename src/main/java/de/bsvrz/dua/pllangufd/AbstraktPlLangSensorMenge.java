/*
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.13 PL-Pruefung Langzeit UFD
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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
 * Wei�enfelser Stra�e 67<br>
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
import de.bsvrz.sys.funclib.bitctrl.daf.BetriebsmeldungDaten;
import de.bsvrz.sys.funclib.bitctrl.daf.DefaultBetriebsMeldungsIdKonverter;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenMessStelle;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenSensor;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.IOnlineUfdSensorListener;
import de.bsvrz.sys.funclib.operatingMessage.MessageCauser;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;
import de.bsvrz.sys.funclib.operatingMessage.MessageState;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;

/**
 * Abstraktes Rohgeruest fuer eine Menge von Sensoren der Art:<br>
 * Hauptsensor (Pruefling), Vorgaenger, Nachfolger,<br>
 * wobei der Hauptsensor im Sinne der Pl-Pruefung langzeit UFD ueberprueft wird.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @param <G>
 *            Art
 *
 * @version $Id: AbstraktPlLangSensorMenge.java 54549 2015-04-17 13:40:51Z
 *          gieseler $
 */
public abstract class AbstraktPlLangSensorMenge<G> implements IOnlineUfdSensorListener<ResultData> {

	private static final DefaultBetriebsMeldungsIdKonverter KONVERTER = new DefaultBetriebsMeldungsIdKonverter();

	/**
	 * <code>Langzeit-Pl-Pr�fung</code>.
	 */
	protected static final String LZ_PL_PR = "Langzeit-Pl-Pr�fung"; //$NON-NLS-1$

	/**
	 * <code>Langzeit-Pl-Pr�fung (24h)</code>.
	 */
	protected static final String LZ_PL_PR24 = "Langzeit-Pl-Pr�fung (24h)"; //$NON-NLS-1$

	/**
	 * <code>Langzeitmessfehler Umfelddaten</code>.
	 */
	protected static final String LZMF_UFD = "Langzeitmessfehler Umfelddaten"; //$NON-NLS-1$

	/**
	 * <code>Langzeitmessfehler Umfelddaten (24h)</code>.
	 */
	protected static final String LZMF_UFD24 = "Langzeitmessfehler Umfelddaten (24h)"; //$NON-NLS-1$

	/**
	 * statische Verbindung zum Datenverteiler.
	 */
	protected static ClientDavInterface derDav = null;

	/**
	 * Vorgaenger-Sensor mit aktuellen Online-Daten.
	 */
	protected AbstraktPlLangSensor<G> vorgaengerSensor = null;

	/**
	 * Nachfolger-Sensor mit aktuellen Online-Daten.
	 */
	protected AbstraktPlLangSensor<G> nachfolgerSensor = null;

	/**
	 * Pruefling mit aktuellen Online-Daten.
	 */
	protected AbstraktPlLangSensor<G> prueflingSensor = null;

	/**
	 * Messstelle, zu der der Pruefling gehoert.
	 */
	protected SystemObject messStelle = null;

	/**
	 * Bildet den Nachrichtenzusatz auf die letzte Datenzeit ab, fuer die eine
	 * Nachricht mit diesem Zusatz publiziert worden ist.
	 */
	private final Map<String, Long> zusatzAufLetzteDatenzeit = Collections.synchronizedMap(new HashMap<String, Long>());

	/**
	 * Erfragt eine statische Instanz des Online-Sensors, der mit dem
	 * uebergebenen Systemobjekt assoziiert ist.
	 *
	 * @param objekt
	 *            ein Systemobjekt eines Umfelddatensensors
	 * @return eine statische Instanz des Online-Sensors, der mit dem
	 *         uebergebenen Systemobjekt assoziiert ist
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 */
	protected abstract AbstraktPlLangSensor<G> getSensorInstanz(final SystemObject objekt)
			throws UmfeldDatenSensorUnbekannteDatenartException;

	/**
	 * Initialisiert dieses Objekt (Instanziierung und Anmeldung der einzelnen
	 * Sensoren auf Daten und Parameter usw.).
	 *
	 * @param dav
	 *            Verbindung zum Datenverteiler
	 * @param messStelle1
	 *            die UFD-Messstelle des Prueflings
	 * @param sensorSelbst
	 *            der Hauptsensor (der ueberprueft wird)
	 * @param sensorVorgaenger
	 *            sein Vorgaenger
	 * @param sensorNachfolger
	 *            sein Nachfolger
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 *             die Datenart des �bergebenen Sensors wird nicht unterst�tzt
	 */
	public final void initialisiere(final ClientDavInterface dav, final DUAUmfeldDatenMessStelle messStelle1,
			final DUAUmfeldDatenSensor sensorSelbst, final DUAUmfeldDatenSensor sensorVorgaenger,
			final DUAUmfeldDatenSensor sensorNachfolger) throws UmfeldDatenSensorUnbekannteDatenartException {
		if (AbstraktPlLangSensorMenge.derDav == null) {
			AbstraktPlLangSensorMenge.derDav = dav;
		}
		this.messStelle = messStelle1.getObjekt();

		this.prueflingSensor = this.getSensorInstanz(sensorSelbst.getObjekt());
		this.vorgaengerSensor = this.getSensorInstanz(sensorVorgaenger.getObjekt());
		this.nachfolgerSensor = this.getSensorInstanz(sensorNachfolger.getObjekt());

		this.prueflingSensor.addListener(this, true);
		this.vorgaengerSensor.addListener(this, true);
		this.nachfolgerSensor.addListener(this, true);
	}

	/**
	 * Sendet eine Betriebsmeldung als Warnung an den Operator. <br>
	 * <b>Achtung:</b> Es koennen nur zwei unterschiedliche Nachrichten in Folge
	 * versendet werden (Zeitstempel)
	 *
	 * @param objekt
	 *            das betroffene Systemobjekt
	 * @param nachricht
	 *            der Nachrichtentext
	 * @param zusatz
	 *            ein Nachrichtenzusatz
	 * @param datenzeit
	 *            der Zeitstempel des Datums, das diese Fehlermeldung provoziert
	 *            hat
	 */
	protected final void sendeBetriebsmeldung(final SystemObject objekt, final String nachricht, final String zusatz,
			final long datenzeit) {

		synchronized (this.zusatzAufLetzteDatenzeit) {
			if ((this.zusatzAufLetzteDatenzeit.get(zusatz) == null)
					|| (this.zusatzAufLetzteDatenzeit.get(zusatz) != datenzeit)) {

				this.zusatzAufLetzteDatenzeit.put(zusatz, datenzeit);
				final MessageSender nachrichtenSender = MessageSender.getInstance();
				nachrichtenSender.setApplicationLabel("PL-Langzeit UFD");
				nachrichtenSender.sendMessage(
						AbstraktPlLangSensorMenge.KONVERTER.konvertiere(new BetriebsmeldungDaten(objekt), null,
								new Object[0]),
						MessageType.APPLICATION_DOMAIN, null, MessageGrade.WARNING, objekt, MessageState.MESSAGE,
						new MessageCauser(AbstraktPlLangSensorMenge.derDav.getLocalUser(), Constants.EMPTY_STRING,
								"Pl-Pr�fung langzeit UFD " + zusatz), //$NON-NLS-1$
								nachricht);
			}
		}
	}

}
