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

import java.util.Date;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensor;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensorMenge;
import de.bsvrz.dua.pllangufd.VergleichsWert;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Assoziator fuer eine Menge von NI-, WFD-, LT-, oder SW-Sensoren der Art:<br>
 * Hauptsensor, Vorgaenger, Nachfolger,<br>
 * wobei der Hauptsensor im Sinne der Pl-Pruefung langzeit UFD ueberprueft wird.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class PlLangNiWfdLtSwSensorMenge extends AbstraktPlLangSensorMenge<VergleichsWert> {

	@Override
	public void aktualisiereDaten(final ResultData datum) {
		UmfeldDatenArt umfeldDatenArt;

		try {
			umfeldDatenArt = UmfeldDatenArt.getUmfeldDatenArtVon(this.prueflingSensor.getObjekt());
		} catch (final UmfeldDatenSensorUnbekannteDatenartException e) {
			Debug.getLogger().warning(e.getMessage());
			return;
		}

		synchronized (this) {
			final VergleichsWert aktuellesSensorDatum = this.prueflingSensor
					.getAktuellenVergleichsWert(prueflingSensor.getAktuelleParameter(), datum.getDataTime());

			final VergleichsWert aktuellesNachfolgerDatum = this.nachfolgerSensor
					.getAktuellenVergleichsWert(prueflingSensor.getAktuelleParameter(), datum.getDataTime());

			final VergleichsWert aktuellesVorgaengerDatum = this.vorgaengerSensor
					.getAktuellenVergleichsWert(prueflingSensor.getAktuelleParameter(), datum.getDataTime());

			final UfdsLangZeitPlPruefungsParameter parameter = this.prueflingSensor.getAktuelleParameter();

			if ((parameter != null) && parameter.isValid() && parameter.getMaxAbweichung().isOk()) {

				if ((datum.getDataTime() - this.prueflingSensor.getAktivSeit()) >= Constants.MILLIS_PER_DAY) {

					final double abweichung24 = this.getAbweichung(true, aktuellesSensorDatum, aktuellesVorgaengerDatum,
							aktuellesNachfolgerDatum);
					if (!Double.isNaN(abweichung24)) {
						synchronized (this) {
							if (abweichung24 > parameter.getMaxAbweichung().getSkaliertenWert()) {
								final String vergleichsZeitBereich = DUAKonstanten.BM_ZEIT_FORMAT
										.format(new Date(datum.getDataTime() - Constants.MILLIS_PER_DAY)) + " - " + //$NON-NLS-1$
										DUAKonstanten.BM_ZEIT_FORMAT.format(new Date(datum.getDataTime()))
										+ " (24 Stunden)"; //$NON-NLS-1$

								this.sendeBetriebsmeldung(this.prueflingSensor.getObjekt(), "Der Wert " + //$NON-NLS-1$
										umfeldDatenArt + " für die Messstelle " + this.messStelle + " weicht um " //$NON-NLS-1$ //$NON-NLS-2$
										+ DUAUtensilien.runde(abweichung24, 2) + " (>" //$NON-NLS-1$
										+ parameter.getMaxAbweichung().getSkaliertenWert()
										+ ") vom erwarteten Vergleichswert im Vergleichszeitbereich " + ""
										+ vergleichsZeitBereich + " ab.", //$NON-NLS-1$
										AbstraktPlLangSensorMenge.LZMF_UFD24, datum.getDataTime());
							}
						}
					} else {
						this.sendeBetriebsmeldung(this.prueflingSensor.getObjekt(),
								"Die Plausibilitätsprüfung zur " + //$NON-NLS-1$
										umfeldDatenArt + " für die Messstelle " + this.messStelle //$NON-NLS-1$
										+ " konnte nicht durchgeführt werden," + //$NON-NLS-1$
										" da ein Vergleichswert nicht bestimmt werden konnte.",
										AbstraktPlLangSensorMenge.LZ_PL_PR24, datum.getDataTime());
					}
				}

				final double abweichung = this.getAbweichung(false, aktuellesSensorDatum, aktuellesVorgaengerDatum,
						aktuellesNachfolgerDatum);
				if (!Double.isNaN(abweichung)) {
					synchronized (this) {
						if (abweichung > parameter.getMaxAbweichung().getSkaliertenWert()) {
							final String vergleichsZeitBereich = DUAKonstanten.BM_ZEIT_FORMAT.format(
									new Date(datum.getDataTime() - parameter.getVergleichsIntervall().getMillis()))
									+ " - " + //$NON-NLS-1$
									DUAKonstanten.BM_ZEIT_FORMAT.format(new Date(datum.getDataTime())) + " (" + //$NON-NLS-1$
									parameter.getVergleichsIntervall() + ")"; //$NON-NLS-1$

							if ((datum.getDataTime() - this.prueflingSensor.getAktivSeit()) >= parameter
									.getVergleichsIntervall().getMillis()) {
								this.sendeBetriebsmeldung(this.prueflingSensor.getObjekt(), "Der Wert " + //$NON-NLS-1$
										umfeldDatenArt + " für die Messstelle " + this.messStelle + " weicht um " //$NON-NLS-1$ //$NON-NLS-2$
										+ DUAUtensilien.runde(abweichung, 2) + " (>" //$NON-NLS-1$
										+ parameter.getMaxAbweichung().getSkaliertenWert()
										+ ") vom erwarteten Vergleichswert im Vergleichszeitbereich " + ""
										+ vergleichsZeitBereich + " ab.", //$NON-NLS-1$
										AbstraktPlLangSensorMenge.LZMF_UFD, datum.getDataTime());
							}
						}
					}
				} else {
					if ((datum.getDataTime() - this.prueflingSensor.getAktivSeit()) >= parameter
							.getVergleichsIntervall().getMillis()) {
						this.sendeBetriebsmeldung(this.prueflingSensor.getObjekt(),
								"Die Plausibilitätsprüfung zur " + //$NON-NLS-1$
										umfeldDatenArt + " für die Messstelle " + this.messStelle //$NON-NLS-1$
										+ " konnte nicht durchgeführt werden," + //$NON-NLS-1$
										" da ein Vergleichswert nicht bestimmt werden konnte.",
										AbstraktPlLangSensorMenge.LZ_PL_PR, datum.getDataTime());
					}
				}
			}
		}
	}

	/**
	 * Berechnet die Abweichung analog Afo-4.0, S.108.
	 *
	 * @param intervall24
	 *            ob die Abweichung fuer das Bezugsintervall von 24h berechnet
	 *            werden soll (sonst wird fuer das parametrierbare
	 *            Bezugsintervall berechnet)
	 * @param aktuellesSensorDatum
	 *            aktuelle Daten des Sensor-Prueflings
	 * @param aktuellesVorgaengerDatum
	 *            aktuelle Daten des Vorgaengers
	 * @param aktuellesNachfolgerDatum
	 *            aktuelle Daten des Nachfolgers
	 * @return die Abweichung analog Afo-4.0, S.108
	 */
	private synchronized double getAbweichung(final boolean intervall24, final VergleichsWert aktuellesSensorDatum,
			final VergleichsWert aktuellesVorgaengerDatum, final VergleichsWert aktuellesNachfolgerDatum) {
		double abweichung = Double.NaN;

		if ((aktuellesSensorDatum != null) && (aktuellesVorgaengerDatum != null)
				&& (aktuellesNachfolgerDatum != null)) {
			double vergleichsWertPruefling = Double.NaN;
			double vergleichsWertVorgaenger = Double.NaN;
			double vergleichsWertNachfolger = Double.NaN;

			if (intervall24) {
				if (aktuellesSensorDatum.isValid24() && aktuellesVorgaengerDatum.isValid24()
						&& aktuellesNachfolgerDatum.isValid24()) {
					vergleichsWertPruefling = aktuellesSensorDatum.getVergleichsWert24();
					vergleichsWertVorgaenger = aktuellesVorgaengerDatum.getVergleichsWert24();
					vergleichsWertNachfolger = aktuellesNachfolgerDatum.getVergleichsWert24();
				}
			} else {
				if (aktuellesSensorDatum.isValid() && aktuellesVorgaengerDatum.isValid()
						&& aktuellesNachfolgerDatum.isValid()) {
					vergleichsWertPruefling = aktuellesSensorDatum.getVergleichsWert();
					vergleichsWertVorgaenger = aktuellesVorgaengerDatum.getVergleichsWert();
					vergleichsWertNachfolger = aktuellesNachfolgerDatum.getVergleichsWert();
				}
			}

			if (!Double.isNaN(vergleichsWertPruefling) && !Double.isNaN(vergleichsWertVorgaenger)
					&& !Double.isNaN(vergleichsWertNachfolger)) {
				abweichung = Math
						.abs(vergleichsWertPruefling - ((vergleichsWertVorgaenger + vergleichsWertNachfolger) / 2.0));
			}
		}

		return abweichung;
	}

	@Override
	protected AbstraktPlLangSensor<VergleichsWert> getSensorInstanz(final SystemObject objekt)
			throws UmfeldDatenSensorUnbekannteDatenartException {
		return PlLangNiWfdLtSwSensor.getInstanz(AbstraktPlLangSensorMenge.derDav, objekt);
	}

}
