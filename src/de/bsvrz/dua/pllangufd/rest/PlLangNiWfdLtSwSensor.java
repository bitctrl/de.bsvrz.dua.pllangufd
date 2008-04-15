/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.13 Pl-Pruefung langzeit UFD
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
 * Wei�enfelser Stra�e 67<br>
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

/**
 * Sensor, der die aktuellen Daten eines NI-, WFD-, LT-, oder SW-Sensors zu
 * Vergleichswerten im Sinne der Pl-Pruefung langzeit UFD verarbeitet.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public class PlLangNiWfdLtSwSensor extends
		AbstraktPlLangSensor<VergleichsWert> {

	/**
	 * statische Instanzen dieser Klasse.
	 */
	private static final Map<SystemObject, PlLangNiWfdLtSwSensor> INSTANZEN = new HashMap<SystemObject, PlLangNiWfdLtSwSensor>();

	/**
	 * Erfragt eine statische Instanz dieser Klasse.
	 * 
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            ein Systemobjekt eines Umfelddatensensors (<code>!= null</code>)
	 * @return eine statische Instanz dieser Klasse
	 */
	public static final PlLangNiWfdLtSwSensor getInstanz(
			final ClientDavInterface dav, final SystemObject objekt) {
		if (objekt == null) {
			throw new NullPointerException("Sensor-Objekt ist <<null>>"); //$NON-NLS-1$
		}
		PlLangNiWfdLtSwSensor instanz = INSTANZEN.get(objekt);

		if (instanz == null) {
			instanz = new PlLangNiWfdLtSwSensor();
			instanz.initialisiere(dav, objekt, dav.getDataModel().getAspect(
					DUAKonstanten.ASP_MESSWERTERSETZUNG));
			INSTANZEN.put(objekt, instanz);
		}

		return instanz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VergleichsWert getAktuellenVergleichsWert(
			final UfdsLangZeitPlPruefungsParameter parameter,
			long aktuellerZeitStempel) {
		VergleichsWert onlineWert = new VergleichsWert();

		synchronized (this) {
			/**
			 * berechne Vergleichswert fuer parametriertes Vergleichsintervall
			 */

			if (parameter != null && parameter.isValid()) {
				double ergebnis = Double.NaN;

				long intervallAnfang = aktuellerZeitStempel
						- parameter.getVergleichsIntervall().getMillis();
				SortedSet<HistorischerUfdsWert> historieVergleich = this.hitorie24
						.cloneTeilMenge(intervallAnfang, aktuellerZeitStempel);
				long intervallLaenge = aktuellerZeitStempel - intervallAnfang;

				if (!historieVergleich.isEmpty()) {
					long summeD = -1;
					long summeWmalD = -1;
					for (HistorischerUfdsWert wert : historieVergleich) {
						if (wert.getWert() != null && wert.getWert().isOk()) {
							if (summeD == -1) {
								summeD = 0; // Initialisierung
								summeWmalD = 0; // Initialisierung
							}
							summeD += wert.getT();
							summeWmalD += wert.getWert().getWert()
									* wert.getT();
						}
					}

					if (summeD >= 0) {
						if (intervallLaenge - summeD <= parameter
								.getMaxAusfallZeit()) {
							ergebnis = ((double) summeWmalD)
									/ ((double) summeD);
						}
					}
				}

				onlineWert.setVergleichsWert(ergebnis);

				/**
				 * berechne Vergleichswert fuer letzte 24h
				 */
				double ergebnis24 = Double.NaN;

				long intervallAnfang24 = aktuellerZeitStempel
						- Constants.MILLIS_PER_DAY;
				SortedSet<HistorischerUfdsWert> historieVergleich24 = this.hitorie24
						.cloneTeilMenge(intervallAnfang24, aktuellerZeitStempel);
				long intervallLaenge24 = aktuellerZeitStempel
						- intervallAnfang24;

				if (!historieVergleich24.isEmpty()) {
					long summeD24 = -1;
					long summeWmalD24 = -1;
					for (HistorischerUfdsWert wert : historieVergleich24) {
						if (wert.getWert() != null && wert.getWert().isOk()) {
							if (summeD24 == -1) {
								summeD24 = 0; // Initialisierung
								summeWmalD24 = 0; // Initialisierung
							}
							summeD24 += wert.getT();
							summeWmalD24 += wert.getWert().getWert()
									* wert.getT();
						}
					}

					if (summeD24 >= 0) {
						if (intervallLaenge24 - summeD24 <= parameter
								.getMaxAusfallZeit()) {
							ergebnis24 = ((double) summeWmalD24)
									/ ((double) summeD24);
						}
					}
				}

				onlineWert.setVergleichsWert24(ergebnis24);
			}
		}

		return onlineWert;
	}

}