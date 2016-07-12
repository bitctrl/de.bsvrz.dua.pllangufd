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

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensor;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensorMenge;
import de.bsvrz.dua.pllangufd.VergleichsWert;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorDatum;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Assoziator fuer eine Menge von NI-, WFD-, LT-, oder SW-Sensoren der Art:<br> Hauptsensor, Vorgaenger, Nachfolger,<br> wobei der Hauptsensor im Sinne der
 * Pl-Pruefung langzeit UFD ueberprueft wird.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 * @version $Id$
 */
public class PlLangNiWfdLtSwSensorMenge extends
		AbstraktPlLangSensorMenge<VergleichsWert> {

	public static final long MILLIS_PER_DAY = (long) (24 * 60 * 60 * 1000);

	private long lastTestTime = Long.MIN_VALUE;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void aktualisiereDaten(final ResultData datum) {
		UmfeldDatenArt umfeldDatenArt;

		try {
			umfeldDatenArt = UmfeldDatenArt.getUmfeldDatenArtVon(this.prueflingSensor.getObjekt());
		}
		catch(final UmfeldDatenSensorUnbekannteDatenartException e) {
			Debug.getLogger().warning(e.getMessage());
			return;
		}

		synchronized(this) {
			long testTime = datum.getDataTime();

			if(lastTestTime == Long.MIN_VALUE) {
				// Nächsten Test durchführen, sobald genug Daten da sind
				lastTestTime = datum.getDataTime() - new UmfeldDatenSensorDatum(datum).getT();
			}

			UfdsLangZeitPlPruefungsParameter aktuelleParameter = prueflingSensor.getAktuelleParameter();
			
			if(aktuelleParameter == null || !aktuelleParameter.isValid()) {
				// Ungültige Parameter
				return;
			}
			
			if(testTime < lastTestTime + aktuelleParameter.getVergleichsIntervall().getMillis()) {
				return;
			}

			if(aktuelleParameter.getMaxAbweichung().getSkaliertenWert() <= 0) {
				return;
			}

			final VergleichsWert aktuellesSensorDatum = this.prueflingSensor
					.getAktuellenVergleichsWert(aktuelleParameter, testTime);

			final VergleichsWert aktuellesNachfolgerDatum = this.nachfolgerSensor
					.getAktuellenVergleichsWert(aktuelleParameter, testTime);

			final VergleichsWert aktuellesVorgaengerDatum = this.vorgaengerSensor
					.getAktuellenVergleichsWert(aktuelleParameter, testTime);

			if(aktuellesSensorDatum != null
					&& aktuellesNachfolgerDatum != null
					&& aktuellesVorgaengerDatum != null) {

				lastTestTime = testTime;

				final UfdsLangZeitPlPruefungsParameter parameter = this.prueflingSensor
						.getAktuelleParameter();

				if(parameter != null && parameter.isValid()
						&& parameter.getMaxAbweichung().isOk()) {

					if(datum.getDataTime() - this.prueflingSensor.getAktivSeit() >= MILLIS_PER_DAY) {

						final double abweichung24 = this.getAbweichung(true,
						                                               aktuellesSensorDatum, aktuellesVorgaengerDatum,
						                                               aktuellesNachfolgerDatum
						);
						if(!Double.isNaN(abweichung24)) {
							synchronized(this) {
								if(abweichung24 > parameter.getMaxAbweichung()
										.getSkaliertenWert()) {
									sendMessage1(datum, umfeldDatenArt, parameter, abweichung24, MILLIS_PER_DAY);
								}
							}
						}
						else if(parameter.getMaxAusfallZeit() > 0) {
							sendMessage2(umfeldDatenArt);
						}
					}

					final double abweichung = this.getAbweichung(false,
					                                             aktuellesSensorDatum, aktuellesVorgaengerDatum,
					                                             aktuellesNachfolgerDatum
					);
					if(!Double.isNaN(abweichung)) {
						synchronized(this) {
							if(abweichung > parameter.getMaxAbweichung()
									.getSkaliertenWert()) {

								if(datum.getDataTime()
										- this.prueflingSensor.getAktivSeit() >= parameter
										.getVergleichsIntervall().getMillis()) {
									sendMessage1(datum, umfeldDatenArt, parameter, abweichung, parameter.getVergleichsIntervall().getMillis());
								}
							}
						}
					}
					else if(parameter.getMaxAusfallZeit() > 0) {
						if(datum.getDataTime()
								- this.prueflingSensor.getAktivSeit() >= parameter
								.getVergleichsIntervall().getMillis()) {
							sendMessage2(umfeldDatenArt);
						}
					}
				}
			}
		}
	}

	/**
	 * Berechnet die Abweichung analog Afo-4.0, S.108.
	 *
	 * @param intervall24              ob die Abweichung fuer das Bezugsintervall von 24h berechnet werden soll (sonst wird fuer das parametrierbare
	 *                                 Bezugsintervall berechnet)
	 * @param aktuellesSensorDatum     aktuelle Daten des Sensor-Prueflings
	 * @param aktuellesVorgaengerDatum aktuelle Daten des Vorgaengers
	 * @param aktuellesNachfolgerDatum aktuelle Daten des Nachfolgers
	 * @return die Abweichung analog Afo-4.0, S.108
	 */
	private synchronized double getAbweichung(final boolean intervall24,
			final VergleichsWert aktuellesSensorDatum,
			final VergleichsWert aktuellesVorgaengerDatum,
			final VergleichsWert aktuellesNachfolgerDatum) {
		double abweichung = Double.NaN;

		if(aktuellesSensorDatum != null && aktuellesVorgaengerDatum != null
				&& aktuellesNachfolgerDatum != null) {
			double vergleichsWertPruefling = Double.NaN;
			double vergleichsWertVorgaenger = Double.NaN;
			double vergleichsWertNachfolger = Double.NaN;

			if(intervall24) {
				if(aktuellesSensorDatum.isValid24()
						&& aktuellesVorgaengerDatum.isValid24()
						&& aktuellesNachfolgerDatum.isValid24()) {
					vergleichsWertPruefling = aktuellesSensorDatum
							.getVergleichsWert24();
					vergleichsWertVorgaenger = aktuellesVorgaengerDatum
							.getVergleichsWert24();
					vergleichsWertNachfolger = aktuellesNachfolgerDatum
							.getVergleichsWert24();
				}
			}
			else {
				if(aktuellesSensorDatum.isValid()
						&& aktuellesVorgaengerDatum.isValid()
						&& aktuellesNachfolgerDatum.isValid()) {
					vergleichsWertPruefling = aktuellesSensorDatum
							.getVergleichsWert();
					vergleichsWertVorgaenger = aktuellesVorgaengerDatum
							.getVergleichsWert();
					vergleichsWertNachfolger = aktuellesNachfolgerDatum
							.getVergleichsWert();
				}
			}

			if(!Double.isNaN(vergleichsWertPruefling)
					&& !Double.isNaN(vergleichsWertVorgaenger)
					&& !Double.isNaN(vergleichsWertNachfolger)) {
				abweichung = Math
						.abs(vergleichsWertPruefling
								     - ((vergleichsWertVorgaenger + vergleichsWertNachfolger) / 2.0));
			}
		}

		return abweichung;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 */
	@Override
	protected AbstraktPlLangSensor<VergleichsWert> getSensorInstanz(
			final SystemObject objekt) throws UmfeldDatenSensorUnbekannteDatenartException {
		return PlLangNiWfdLtSwSensor.getInstanz(_clientDavInterface, objekt);
	}

}
