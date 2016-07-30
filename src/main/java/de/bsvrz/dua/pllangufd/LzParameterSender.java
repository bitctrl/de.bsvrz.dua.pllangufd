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

import de.bsvrz.dav.daf.main.*;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.StundenIntervallAnteil12h;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;

import java.util.HashMap;
import java.util.Map;

/**
 * Sendet Parameter eines Sensors fuer die Pl-Pruefung langzeit UFD.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class LzParameterSender implements ClientSenderInterface {

	/**
	 * statische Instanzen.
	 */
	private static Map<SystemObject, LzParameterSender> instanzen = new HashMap<SystemObject, LzParameterSender>();

	/**
	 * Datenverteiler-Verbindung.
	 */
	private static ClientDavInterface sDAV = null;

	/**
	 * das Systemobjekt des Sensors.
	 */
	private SystemObject objekt = null;

	/**
	 * Erfragt eine statische Instanz dieser Klasse.
	 * 
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param obj
	 *            Systemobjekt
	 * @return eine statische Instanz dieser Klasse
	 * @throws UmfeldDatenSensorUnbekannteDatenartException 
	 */
	public static LzParameterSender getInstanz(final ClientDavInterface dav,
			final SystemObject obj) throws UmfeldDatenSensorUnbekannteDatenartException {
		if (sDAV == null) {
			sDAV = dav;
		}
		LzParameterSender sender = instanzen.get(obj);

		if (sender == null) {
			sender = new LzParameterSender(dav, obj);
			instanzen.put(obj, sender);
		}

		return sender;
	}

	/**
	 * Standardkonstruktor.
	 * 
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param obj
	 *            Systemobjekt
	 * @throws UmfeldDatenSensorUnbekannteDatenartException 
	 */
	private LzParameterSender(final ClientDavInterface dav, final SystemObject obj) throws UmfeldDatenSensorUnbekannteDatenartException {
		this.objekt = obj;
		final UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(obj);

		final DataDescription dd = new DataDescription(dav.getDataModel()
				.getAttributeGroup(
						"atg.ufdsLangzeitPLPrüfung" + datenArt.getName()), //$NON-NLS-1$
				dav.getDataModel().getAspect(
						DaVKonstanten.ASP_PARAMETER_VORGABE));

		try {
			dav.subscribeSender(this, obj, dd, SenderRole.sender());
		} catch (final OneSubscriptionPerSendData e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sendet Parameter.
	 * 
	 * @param vergleichsIntervall
	 *            der Vergleichsintervall
	 * @param maxAusfallZeit
	 *            die maximale Ausfallzeit
	 * @param maxAbweichung
	 *            die maximale Abweichung
	 * @return ob das Senden erfolgreich war
	 * @throws UmfeldDatenSensorUnbekannteDatenartException 
	 */
	public boolean setParameter(
			final StundenIntervallAnteil12h vergleichsIntervall, final long maxAusfallZeit,
			final long maxAbweichung) throws UmfeldDatenSensorUnbekannteDatenartException {
		boolean erfolg = false;

		final UmfeldDatenArt datenArt = UmfeldDatenArt
				.getUmfeldDatenArtVon(this.objekt);

		final DataDescription dd = new DataDescription(sDAV.getDataModel()
				.getAttributeGroup(
						"atg.ufdsLangzeitPLPrüfung" + datenArt.getName()), //$NON-NLS-1$
				sDAV.getDataModel().getAspect(
						DaVKonstanten.ASP_PARAMETER_VORGABE));

		final Data nutzDatum = sDAV.createData(sDAV.getDataModel().getAttributeGroup(
				"atg.ufdsLangzeitPLPrüfung" + datenArt.getName())); //$NON-NLS-1$
		nutzDatum
				.getUnscaledValue("VergleichsIntervall").set(vergleichsIntervall.getCode()); //$NON-NLS-1$
		nutzDatum.getTimeValue("maxAusfallZeit").setMillis(maxAusfallZeit); //$NON-NLS-1$
		if (datenArt.equals(UmfeldDatenArt.ns)
				|| datenArt.equals(UmfeldDatenArt.fbz)) {

			// nutzDatum.getUnscaledValue("maxAbweichung" +
			// datenArt.getAbkuerzung()).set(0); //$NON-NLS-1$

			nutzDatum
					.getTimeValue("maxAbweichung" + datenArt.getAbkuerzung()).setMillis(maxAbweichung); //$NON-NLS-1$ 

		} else {
			nutzDatum
					.getUnscaledValue(
							"maxAbweichung" + datenArt.getAbkuerzung()).set(maxAbweichung); //$NON-NLS-1$
		}

		try {
			sDAV.sendData(new ResultData(this.objekt, dd, System
					.currentTimeMillis(), nutzDatum));
			erfolg = true;
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return erfolg;
	}

	public void dataRequest(final SystemObject object,
			final DataDescription dataDescription, final byte state) {
		// 		
	}

	public boolean isRequestSupported(final SystemObject object,
			final DataDescription dataDescription) {
		return false;
	}
}
