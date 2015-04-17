/*
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.13 PL-Pruefung Langzeit UFD
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
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.pllangufd;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;

/**
 * Sendet Daten von Umfelddatensensoren.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public final class UfdSensorSender implements ClientSenderInterface {

	/**
	 * statische Instanzen dieser Klasse.
	 */
	private static HashMap<SystemObject, UfdSensorSender> instanzen = new HashMap<SystemObject, UfdSensorSender>();

	/**
	 * Objekt.
	 */
	private SystemObject objekt = null;

	/**
	 * Initialisiert alle Umfelddatensensoren.
	 * 
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param ersteDatenZeit
	 *            erste Datenzeit der Nutzdaten (wird bei der Initialisierung
	 *            versendet)
	 * @throws Exception
	 *             wird weitergereicht
	 */
	public static void initialisiere(final ClientDavInterface dav,
			final long ersteDatenZeit) throws Exception {
		for (SystemObject objekt : dav.getDataModel().getType(
				"typ.umfeldDatenSensor").getElements()) { //$NON-NLS-1$
			instanzen.put(objekt, new UfdSensorSender(dav, objekt));
		}
		try {
			Thread.sleep(1000L);
		} catch (final InterruptedException e) {
			//
		}

		/**
		 * Warte bis alle Anmeldungenen durchgeführt sind
		 */
		for (UfdSensorSender sender : instanzen.values()) {
			final UmfeldDatenArt datenArt = UmfeldDatenArt
					.getUmfeldDatenArtVon(sender.getObjekt());
			final DataDescription dd = new DataDescription(dav.getDataModel()
					.getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
					dav.getDataModel().getAspect(
							DUAKonstanten.ASP_MESSWERTERSETZUNG));
			final Data nutzDaten = getSensorDatum(sender.getObjekt(),
					15L * Constants.MILLIS_PER_MINUTE, datenArt
							.equals(UmfeldDatenArt.ns)
							|| datenArt.equals(UmfeldDatenArt.fbz) ? 0 : 1);
			sender.sende(new ResultData(sender.getObjekt(), dd, ersteDatenZeit,
					nutzDaten));
		}

		for (UfdSensorSender sender : instanzen.values()) {
			final UmfeldDatenArt datenArt = UmfeldDatenArt
					.getUmfeldDatenArtVon(sender.getObjekt());
			final DataDescription dd = new DataDescription(dav.getDataModel()
					.getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
					dav.getDataModel().getAspect(
							DUAKonstanten.ASP_MESSWERTERSETZUNG));
			final Data nutzDaten = getSensorDatum(sender.getObjekt(),
					15L * Constants.MILLIS_PER_MINUTE, datenArt
							.equals(UmfeldDatenArt.ns)
							|| datenArt.equals(UmfeldDatenArt.fbz) ? 0 : 1);
			sender.sende(new ResultData(sender.getObjekt(), dd, ersteDatenZeit
					+ 15L * Constants.MILLIS_PER_MINUTE, nutzDaten));
		}

		for (UfdSensorSender sender : instanzen.values()) {
			final UmfeldDatenArt datenArt = UmfeldDatenArt
					.getUmfeldDatenArtVon(sender.getObjekt());
			final DataDescription dd = new DataDescription(dav.getDataModel()
					.getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
					dav.getDataModel().getAspect(
							DUAKonstanten.ASP_MESSWERTERSETZUNG));
			final Data nutzDaten = getSensorDatum(sender.getObjekt(),
					15L * Constants.MILLIS_PER_MINUTE, datenArt
							.equals(UmfeldDatenArt.ns)
							|| datenArt.equals(UmfeldDatenArt.fbz) ? 0 : 1);
			sender.sende(new ResultData(sender.getObjekt(), dd, ersteDatenZeit
					+ 30L * Constants.MILLIS_PER_MINUTE, nutzDaten));
		}

		for (UfdSensorSender sender : instanzen.values()) {
			final UmfeldDatenArt datenArt = UmfeldDatenArt
					.getUmfeldDatenArtVon(sender.getObjekt());
			final DataDescription dd = new DataDescription(dav.getDataModel()
					.getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
					dav.getDataModel().getAspect(
							DUAKonstanten.ASP_MESSWERTERSETZUNG));
			final Data nutzDaten = getSensorDatum(sender.getObjekt(),
					15L * Constants.MILLIS_PER_MINUTE, datenArt
							.equals(UmfeldDatenArt.ns)
							|| datenArt.equals(UmfeldDatenArt.fbz) ? 0 : 1);
			sender.sende(new ResultData(sender.getObjekt(), dd, ersteDatenZeit
					+ 45L * Constants.MILLIS_PER_MINUTE, nutzDaten));
		}

		for (UfdSensorSender sender : instanzen.values()) {
			final UmfeldDatenArt datenArt = UmfeldDatenArt
					.getUmfeldDatenArtVon(sender.getObjekt());
			final DataDescription dd = new DataDescription(dav.getDataModel()
					.getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
					dav.getDataModel().getAspect(
							DUAKonstanten.ASP_MESSWERTERSETZUNG));
			final Data nutzDaten = getSensorDatum(sender.getObjekt(),
					15L * Constants.MILLIS_PER_MINUTE, datenArt
							.equals(UmfeldDatenArt.ns)
							|| datenArt.equals(UmfeldDatenArt.fbz) ? 0 : 1);
			sender.sende(new ResultData(sender.getObjekt(), dd, ersteDatenZeit
					+ 60L * Constants.MILLIS_PER_MINUTE, nutzDaten));
		}

	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse.
	 * 
	 * @return eine statische Instanz dieser Klasse
	 */
	public static Collection<UfdSensorSender> getInstanzen() {
		return instanzen.values();
	}

	/**
	 * Erfragt eine statische Instanz dieser Klasse.
	 * 
	 * @param obj
	 *            Systemobjekt eines Umfelddatensensors
	 * @return eine statische Instanz dieser Klasse
	 */
	public static UfdSensorSender getInstanz(final SystemObject obj) {
		return instanzen.get(obj);
	}

	/**
	 * Standardkonstruktor.
	 * 
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            Systemobjekt eines Umfelddatensensors
	 * @throws OneSubscriptionPerSendData
	 *             wird weitergereicht
	 * @throws UmfeldDatenSensorUnbekannteDatenartException 
	 */
	private UfdSensorSender(final ClientDavInterface dav, final SystemObject objekt)
			throws OneSubscriptionPerSendData, UmfeldDatenSensorUnbekannteDatenartException {
		this.objekt = objekt;
		final UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(objekt);
		final DataDescription dd = new DataDescription(dav.getDataModel()
				.getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
				dav.getDataModel().getAspect(
						DUAKonstanten.ASP_MESSWERTERSETZUNG));
		dav.subscribeSender(this, objekt, dd, SenderRole.source());
	}

	/**
	 * Sendet Daten.
	 * 
	 * @param resultat
	 *            ein UFD
	 */
	public void sende(final ResultData resultat) {
		try {
			DAVTest.getDav().sendData(resultat);
			final SimpleDateFormat dateFormat = new SimpleDateFormat(DUAKonstanten.ZEIT_FORMAT_GENAU_STR);
			System.out
					.println(dateFormat.format(new Date(
							resultat.getDataTime()))
							+ ", " + //$NON-NLS-1$ 
							resultat.getObject().getPid()
							+ " (" + //$NON-NLS-1$
							(resultat.getData().getTimeValue("T").getMillis() / Constants.MILLIS_PER_MINUTE) + "min): " + //$NON-NLS-1$ //$NON-NLS-2$
							resultat.getData().getItem(
									UmfeldDatenArt.getUmfeldDatenArtVon(
											resultat.getObject()).getName())
									.getUnscaledValue("Wert").longValue()); //$NON-NLS-1$
			try {
				Thread.sleep(50L);
			} catch (final InterruptedException ex) {
				//
			}
		} catch (final DataNotSubscribedException e) {
			e.printStackTrace();
		} catch (final SendSubscriptionNotConfirmed e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Erzeugt einen Messwert mit der Datenbeschreibung
	 * <code>asp.messWertErsetzung</code>.
	 * 
	 * @param intervall
	 *            das Erfassungeintervall
	 * @param sensor
	 *            ein Umfelddatensensor, für den ein Messwert erzeugt werden
	 *            soll
	 * @param wert
	 *            der zu setzende Sensor-Wert
	 * @return ein (ausgefüllter) Umfelddaten-Messwert der zum übergebenen
	 *         Systemobjekt passt. Alle Pl-Prüfungs-Flags sind auf
	 *         <code>NEIN</code> gesetzt. Der Daten-Intervall beträgt 1 min.
	 * @throws Exception
	 *             wird weitergereicht
	 */
	public static Data getSensorDatum(final SystemObject sensor,
			final long intervall, final long wert) throws Exception {
		final UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(sensor);

		final Data datum = DAVTest.getDav().createData(
				DAVTest.getDav().getDataModel().getAttributeGroup(
						"atg.ufds" + datenArt.getName())); //$NON-NLS-1$

		datum.getTimeValue("T").setMillis(intervall); //$NON-NLS-1$
		datum.getItem(datenArt.getName()).getUnscaledValue("Wert").set(wert); //$NON-NLS-1$
		datum.getItem(datenArt.getName())
				.getItem("Status").getItem("Erfassung").
				getUnscaledValue("NichtErfasst").set(DUAKonstanten.NEIN); //$NON-NLS-1$
		datum.getItem(datenArt.getName())
				.getItem("Status").getItem("PlFormal").
				getUnscaledValue("WertMax").set(DUAKonstanten.NEIN); //$NON-NLS-1$
		datum.getItem(datenArt.getName())
				.getItem("Status").getItem("PlFormal").
				getUnscaledValue("WertMin").set(DUAKonstanten.NEIN); //$NON-NLS-1$

		datum.getItem(datenArt.getName())
				.getItem("Status").getItem("MessWertErsetzung").
				getUnscaledValue("Implausibel").set(DUAKonstanten.NEIN); //$NON-NLS-1$
		datum.getItem(datenArt.getName())
				.getItem("Status").getItem("MessWertErsetzung").
				getUnscaledValue("Interpoliert").set(DUAKonstanten.NEIN); //$NON-NLS-1$

		datum.getItem(datenArt.getName())
				.getItem("Güte").getUnscaledValue("Index").set(10000); //$NON-NLS-1$ //$NON-NLS-2$
		datum.getItem(datenArt.getName())
				.getItem("Güte").getUnscaledValue("Verfahren").set(0); //$NON-NLS-1$ //$NON-NLS-2$

		return datum;
	}

	/**
	 * Erfragt das Objekt.
	 * 
	 * @return das Objekt
	 */
	public SystemObject getObjekt() {
		return this.objekt;
	}

	/**
	 * {@inheritDoc}
	 */
	public void dataRequest(final SystemObject object,
			final DataDescription dataDescription, final byte state) {
		// 	
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRequestSupported(final SystemObject object,
			final DataDescription dataDescription) {
		return false;
	}

}
