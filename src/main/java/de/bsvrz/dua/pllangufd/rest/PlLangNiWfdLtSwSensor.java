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


package de.bsvrz.dua.pllangufd.rest;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensor;
import de.bsvrz.dua.pllangufd.VergleichsWert;
import de.bsvrz.dua.pllangufd.historie.HistorischerUfdsWert;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;

import java.util.SortedSet;

/**
 * Sensor, der die aktuellen Daten eines NI-, WFD-, LT-, oder SW-Sensors zu
 * Vergleichswerten im Sinne der Pl-Pruefung langzeit UFD verarbeitet.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class PlLangNiWfdLtSwSensor extends
		AbstraktPlLangSensor<VergleichsWert> {

	/**
	 * Erfragt eine statische Instanz dieser Klasse.
	 * 
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            ein Systemobjekt eines Umfelddatensensors (<code>!= null</code>)
	 * @return eine statische Instanz dieser Klasse
	 * @throws UmfeldDatenSensorUnbekannteDatenartException 
	 */
	public static final PlLangNiWfdLtSwSensor getInstanz(
			final ClientDavInterface dav, final SystemObject objekt) throws UmfeldDatenSensorUnbekannteDatenartException {
		if (objekt == null) {
			throw new NullPointerException("Sensor-Objekt ist <<null>>"); //$NON-NLS-1$
		}
		final PlLangNiWfdLtSwSensor instanz = new PlLangNiWfdLtSwSensor();
		instanz.initialisiere(dav, objekt, dav.getDataModel().getAspect(
				DUAKonstanten.ASP_MESSWERTERSETZUNG));

		return instanz;
	}

	@Override
	public VergleichsWert getAktuellenVergleichsWert(
			final UfdsLangZeitPlPruefungsParameter parameter,
			final long aktuellerZeitStempel) {
		final VergleichsWert onlineWert = new VergleichsWert();

		synchronized (this) {
			/**
			 * berechne Vergleichswert fuer parametriertes Vergleichsintervall
			 */

			if (parameter != null && parameter.isValid()) {
				double ergebnis = Double.NaN;

				final long intervallAnfang = aktuellerZeitStempel
						- parameter.getVergleichsIntervall().getMillis();
				final SortedSet<HistorischerUfdsWert> historieVergleich = this.historie24
						.cloneTeilMenge(intervallAnfang, aktuellerZeitStempel);
				final long intervallLaenge = parameter.getVergleichsIntervall().getMillis();

				ergebnis = getErgebnis(parameter, ergebnis, historieVergleich, intervallLaenge);

				if(historieVergleich.isEmpty()){
					return null;
				}
				HistorischerUfdsWert last = historieVergleich.first();
				if(last.getZeitStempel() != aktuellerZeitStempel) {
					return null;
				}
				onlineWert.setVergleichsWert(ergebnis);

				/**
				 * berechne Vergleichswert fuer letzte 24h
				 */
				double ergebnis24 = Double.NaN;

				final long intervallAnfang24 = aktuellerZeitStempel
						- MILLIS_PER_DAY;
				final SortedSet<HistorischerUfdsWert> historieVergleich24 = this.historie24
						.cloneTeilMenge(intervallAnfang24, aktuellerZeitStempel);

				ergebnis24 = getErgebnis(parameter, ergebnis24, historieVergleich24, MILLIS_PER_DAY);

				onlineWert.setVergleichsWert24(ergebnis24);
			}
		}

		return onlineWert;
	}
	private static double getErgebnis(final UfdsLangZeitPlPruefungsParameter parameter, double ergebnis, final SortedSet<HistorischerUfdsWert> historieVergleich, final long intervallLaenge) {
		if (!historieVergleich.isEmpty()) {
			long summeD = -1;
			long summeWmalD = -1;
			double ergebnis1 = ergebnis;
			for (HistorischerUfdsWert wert : historieVergleich) {
				if (wert.getWert() != null && wert.getWert().isOk()) {
					if (summeD == -1) {
						summeD = 0; // Initialisierung
						summeWmalD = 0; // Initialisierung
					}
					summeD += wert.getT();
					summeWmalD += wert.getWert().getSkaliertenWert()
							* wert.getT();
				}
			}

			if (summeD > 0) {
				if (intervallLaenge - summeD <= parameter.getMaxAusfallZeit()) {
					ergebnis1 = ((double) summeWmalD)
							/ ((double) summeD);
				}
			}
			ergebnis = ergebnis1;
		}
		return ergebnis;
	}


}
