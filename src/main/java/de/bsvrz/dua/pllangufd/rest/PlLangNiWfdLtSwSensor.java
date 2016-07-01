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

package de.bsvrz.dua.pllangufd.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensor;
import de.bsvrz.dua.pllangufd.VergleichsWert;
import de.bsvrz.dua.pllangufd.historie.HistorischerUfdsWert;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;

/**
 * Sensor, der die aktuellen Daten eines NI-, WFD-, LT-, oder SW-Sensors zu
 * Vergleichswerten im Sinne der Pl-Pruefung langzeit UFD verarbeitet.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class PlLangNiWfdLtSwSensor extends AbstraktPlLangSensor<VergleichsWert> {

	/**
	 * statische Instanzen dieser Klasse.
	 */
	private static final Map<SystemObject, PlLangNiWfdLtSwSensor> INSTANZEN = new HashMap<>();

	/**
	 * Erfragt eine statische Instanz dieser Klasse.
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            ein Systemobjekt eines Umfelddatensensors (
	 *            <code>!= null</code>)
	 * @return eine statische Instanz dieser Klasse
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 */
	public static final PlLangNiWfdLtSwSensor getInstanz(final ClientDavInterface dav, final SystemObject objekt)
			throws UmfeldDatenSensorUnbekannteDatenartException {
		if (objekt == null) {
			throw new NullPointerException("Sensor-Objekt ist <<null>>"); //$NON-NLS-1$
		}
		PlLangNiWfdLtSwSensor instanz = PlLangNiWfdLtSwSensor.INSTANZEN.get(objekt);

		if (instanz == null) {
			instanz = new PlLangNiWfdLtSwSensor();
			instanz.initialisiere(dav, objekt, dav.getDataModel().getAspect(DUAKonstanten.ASP_MESSWERTERSETZUNG));
			PlLangNiWfdLtSwSensor.INSTANZEN.put(objekt, instanz);
		}

		return instanz;
	}

	@Override
	public VergleichsWert getAktuellenVergleichsWert(final UfdsLangZeitPlPruefungsParameter parameter,
			final long aktuellerZeitStempel) {
		final VergleichsWert onlineWert = new VergleichsWert();

		synchronized (this) {
			/**
			 * berechne Vergleichswert fuer parametriertes Vergleichsintervall
			 */

			if ((parameter != null) && parameter.isValid()) {
				double ergebnis = Double.NaN;

				final long intervallAnfang = aktuellerZeitStempel - parameter.getVergleichsIntervall().getMillis();
				final SortedSet<HistorischerUfdsWert> historieVergleich = this.hitorie24.cloneTeilMenge(intervallAnfang,
						aktuellerZeitStempel);
				final long intervallLaenge = aktuellerZeitStempel - intervallAnfang;

				if (!historieVergleich.isEmpty()) {
					long summeD = -1;
					long summeWmalD = -1;
					for (final HistorischerUfdsWert wert : historieVergleich) {
						if ((wert.getWert() != null) && wert.getWert().isOk()) {
							if (summeD == -1) {
								summeD = 0; // Initialisierung
								summeWmalD = 0; // Initialisierung
							}
							summeD += wert.getT();
							summeWmalD += wert.getWert().getWert() * wert.getT();
						}
					}

					if (summeD >= 0) {
						if ((intervallLaenge - summeD) <= parameter.getMaxAusfallZeit()) {
							ergebnis = ((double) summeWmalD) / ((double) summeD);
						}
					}
				}

				onlineWert.setVergleichsWert(ergebnis);

				/**
				 * berechne Vergleichswert fuer letzte 24h
				 */
				double ergebnis24 = Double.NaN;

				final long intervallAnfang24 = aktuellerZeitStempel - Constants.MILLIS_PER_DAY;
				final SortedSet<HistorischerUfdsWert> historieVergleich24 = this.hitorie24
						.cloneTeilMenge(intervallAnfang24, aktuellerZeitStempel);
				final long intervallLaenge24 = aktuellerZeitStempel - intervallAnfang24;

				if (!historieVergleich24.isEmpty()) {
					long summeD24 = -1;
					long summeWmalD24 = -1;
					for (final HistorischerUfdsWert wert : historieVergleich24) {
						if ((wert.getWert() != null) && wert.getWert().isOk()) {
							if (summeD24 == -1) {
								summeD24 = 0; // Initialisierung
								summeWmalD24 = 0; // Initialisierung
							}
							summeD24 += wert.getT();
							summeWmalD24 += wert.getWert().getWert() * wert.getT();
						}
					}

					if (summeD24 >= 0) {
						if ((intervallLaenge24 - summeD24) <= parameter.getMaxAusfallZeit()) {
							ergebnis24 = ((double) summeWmalD24) / ((double) summeD24);
						}
					}
				}

				onlineWert.setVergleichsWert24(ergebnis24);
			}
		}

		return onlineWert;
	}

}
