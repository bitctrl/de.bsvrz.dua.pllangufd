/*
 * Segment Datenübernahme und Aufbereitung (DUA), SWE PL-Prüfung Langzeit UFD
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
 * Copyright 2016 by Kappich Systemberatung Aachen
 * 
 * This file is part of de.bsvrz.dua.pllangufd.
 * 
 * de.bsvrz.dua.pllangufd is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * de.bsvrz.dua.pllangufd is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with de.bsvrz.dua.pllangufd.  If not, see <http://www.gnu.org/licenses/>.

 * Contact Information:
 * Kappich Systemberatung
 * Martin-Luther-Straße 14
 * 52062 Aachen, Germany
 * phone: +49 241 4090 436 
 * mail: <info@kappich.de>
 */

package de.bsvrz.dua.pllangufd;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorWert;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenMessStelle;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenSensor;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.IOnlineUfdSensorListener;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageTemplate;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;
import de.bsvrz.sys.funclib.operatingMessage.OperatingMessage;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

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
 * @version $Id: AbstraktPlLangSensorMenge.java 54549 2015-04-17 13:40:51Z gieseler $
 */
public abstract class AbstraktPlLangSensorMenge<G> implements
		IOnlineUfdSensorListener<ResultData> {

	/**
	 * Millisekunden in 24 Stunden
	 */
	public static final long MILLIS_PER_DAY = (long) (24 * 60 * 60 * 1000);

	/**
	 * Verbindung zum Datenverteiler.
	 */
	protected ClientDavInterface _clientDavInterface = null;

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
	 * Erfragt eine statische Instanz des Online-Sensors, der mit dem
	 * uebergebenen Systemobjekt assoziiert ist.
	 * 
	 * @param objekt
	 *            ein Systemobjekt eines Umfelddatensensors
	 * @return eine statische Instanz des Online-Sensors, der mit dem
	 *         uebergebenen Systemobjekt assoziiert ist
	 * @throws UmfeldDatenSensorUnbekannteDatenartException 
	 */
	protected abstract AbstraktPlLangSensor<G> getSensorInstanz(
			final SystemObject objekt) throws UmfeldDatenSensorUnbekannteDatenartException;

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
	 */
	public final void initialisiere(final ClientDavInterface dav,
			final DUAUmfeldDatenMessStelle messStelle1,
			final DUAUmfeldDatenSensor sensorSelbst,
			final DUAUmfeldDatenSensor sensorVorgaenger,
			final DUAUmfeldDatenSensor sensorNachfolger) throws UmfeldDatenSensorUnbekannteDatenartException {
		
		_clientDavInterface = dav;
		this.messStelle = messStelle1.getObjekt();

		this.prueflingSensor = this.getSensorInstanz(sensorSelbst.getObjekt());
		this.vorgaengerSensor = this.getSensorInstanz(sensorVorgaenger
				.getObjekt());
		this.nachfolgerSensor = this.getSensorInstanz(sensorNachfolger
				.getObjekt());

		this.prueflingSensor.addListener(this, true);
		this.vorgaengerSensor.addListener(this, true);
		this.nachfolgerSensor.addListener(this, true);
	}

	/**
	 * Betriebsmeldungs-Text für Abweichungs-Meldungen
	 */
	private final MessageTemplate TEMPLATE_ABW = new MessageTemplate(
			MessageGrade.WARNING,
	        MessageType.APPLICATION_DOMAIN,
	        MessageTemplate.fixed("Der Wert "),
	        MessageTemplate.variable("attr"),
	        MessageTemplate.fixed(" für die Messstelle "),
	        MessageTemplate.object(),
	        MessageTemplate.fixed(" weicht um "),
	        MessageTemplate.variable("abw"),
	        MessageTemplate.fixed(" (> "),
			MessageTemplate.variable("maxabw"),
			MessageTemplate.fixed(") vom erwarteten Vergleichswert im Vergleichszeitbereich "),
	        MessageTemplate.variable("range"),
	        MessageTemplate.fixed(" ab. "),
	        MessageTemplate.ids()
	).withIdFactory(message -> message.getObject().getPidOrId() + " [DUA-PP-ULZ]");

	/**
	 * Betriebsmeldungs-Text falls kein Vergleichswert bestimmt werden konnte
	 */
	private final MessageTemplate TEMPLATE_NO_DATA = new MessageTemplate(
			MessageGrade.WARNING,
	        MessageType.APPLICATION_DOMAIN,
	        MessageTemplate.fixed("Die Plausibilitätsprüfung des Attributs "),
	        MessageTemplate.variable("attr"),
	        MessageTemplate.fixed(" für die Messstelle "),
	        MessageTemplate.object(),
	        MessageTemplate.fixed(" konnte nicht durchgeführt werden, da (min.) einer der Vergleichswerte nicht bestimmt werden konnte. "),
	        MessageTemplate.ids()
	).withIdFactory(message -> message.getObject().getPidOrId() + " [DUA-PP-ULZ]");

	/**
	 * Datum- und Zeit-Format
	 */
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.GERMAN);

	/**
	 * Formatiert einen Wert
	 * @param w Wert
	 * @param suffixText Suffix (Einheit)
	 * @return Wert
	 */
	private static String formatValue(final double w, final String suffixText) {
		NumberFormat numberInstance = NumberFormat.getNumberInstance();
		numberInstance.setGroupingUsed(false);
		return numberInstance.format(w) + " " + suffixText;
	}

	/**
	 * Formatiert eine Dauer
	 * @param tmp Dauer in Millisekunden
	 * @return Text wie "1 Stunde 5 Minuten"
	 */
	public static String formatDuration(long tmp) {
		long ms = tmp % 1000;
		tmp /= 1000;
		long sec = tmp % 60;
		tmp /= 60;
		long min = tmp % 60;
		tmp /= 60;
		long h = tmp;
		StringBuilder stringBuilder = new StringBuilder();
		if(h >= 1){
			if(h == 1){
				stringBuilder.append("1 Stunde ");
			}
			else {
				stringBuilder.append(h).append(" Stunden ");
			}
		}
		if(min >= 1){
			if(min == 1){
				stringBuilder.append("1 Minute ");
			}
			else {
				stringBuilder.append(min).append(" Minuten ");
			}
		}
		if(sec >= 1){
			if(sec == 1){
				stringBuilder.append("1 Sekunde ");
			}
			else {
				stringBuilder.append(sec).append(" Sekunden ");
			}
		}
		if(ms >= 1){
			if(ms == 1){
				stringBuilder.append("1 Millisekunde ");
			}
			else {
				stringBuilder.append(ms).append(" Millisekunden ");
			}
		}
		stringBuilder.setLength(stringBuilder.length()-1);
		return stringBuilder.toString();
	}

	/**
	 * Sendet die Betriebsmeldung mit Abweichung
	 * @param datum Implausibles Datum
	 * @param umfeldDatenArt Umfelddatenart
	 * @param parameter Aktueller Parameter
	 * @param abweichung Bestehende Abweichung
	 * @param testIntervall Prüfintervall (in Millisekunden)
	 */
	protected void sendMessage1(final ResultData datum, final UmfeldDatenArt umfeldDatenArt, final UfdsLangZeitPlPruefungsParameter parameter, final double abweichung, final long testIntervall) {
		OperatingMessage message = TEMPLATE_ABW.newMessage(messStelle);
		message.put("attr", umfeldDatenArt.getName() + " (" + umfeldDatenArt.getAbkuerzung() + ")");
		String suffixText = datum.getData().getItem(umfeldDatenArt.getName()).getTextValue("Wert").getSuffixText();
		UmfeldDatenSensorWert maxAbweichung = parameter.getMaxAbweichung();
		if(maxAbweichung != null) {
			message.put("abw", formatValue(abweichung, suffixText));
			message.put("maxabw", formatValue(maxAbweichung.getSkaliertenWert(), suffixText));
		}
		else {
			// Ereignis-Sensor, Abweichung ist Dauer
			message.put("abw", formatDuration((long) abweichung));
			message.put("maxabw", formatDuration(parameter.getMaxAbweichungZeit()));	
		}
		Duration duration = Duration.ofMillis(testIntervall);
		Instant to = Instant.ofEpochMilli(datum.getDataTime());
		Instant from = to.minus(duration);
		message.put("range", DATE_TIME_FORMATTER.format(LocalDateTime.ofInstant(from, ZoneId.systemDefault())) + " - " + DATE_TIME_FORMATTER.format(LocalDateTime.ofInstant(to, ZoneId.systemDefault())) + " (" + formatDuration(duration.toMillis()) + ")");
		switch(umfeldDatenArt.getAbkuerzung()){
			case "NI":
				message.addId("[DUA-PP-ULZ01]");
				break;
			case "WFD":
				message.addId("[DUA-PP-ULZ03]");
				break;
			case "SW":
				message.addId("[DUA-PP-ULZ05]");
				break;
			case "LT":
				message.addId("[DUA-PP-ULZ07]");
				break;
			case "NS":
				message.addId("[DUA-PP-ULZ09]");
				break;
			case "FBZ":
				message.addId("[DUA-PP-ULZ11]");
				break;
		}
		message.send();
	}

	/**
	 * Sendet die Betriebsmeldung wenn der Vergleichswert nicht bestimmt werden konnte
	 * @param umfeldDatenArt Umfelddatenart
	 */
	protected void sendMessage2(final UmfeldDatenArt umfeldDatenArt) {
		OperatingMessage message = TEMPLATE_NO_DATA.newMessage(messStelle);
		message.put("attr", umfeldDatenArt.getName() + " (" + umfeldDatenArt.getAbkuerzung() + ")");
		switch(umfeldDatenArt.getAbkuerzung()){
			case "NI":
				message.addId("[DUA-PP-ULZ02]");
				break;
			case "WFD":
				message.addId("[DUA-PP-ULZ04]");
				break;
			case "SW":
				message.addId("[DUA-PP-ULZ06]");
				break;
			case "LT":
				message.addId("[DUA-PP-ULZ08]");
				break;
			case "NS":
				message.addId("[DUA-PP-ULZ10]");
				break;
			case "FBZ":
				message.addId("[DUA-PP-ULZ12]");
				break;
		}
		message.send();
	}
}
