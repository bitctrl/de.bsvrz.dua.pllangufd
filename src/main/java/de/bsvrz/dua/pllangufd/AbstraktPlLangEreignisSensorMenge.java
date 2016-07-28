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

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorDatum;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.debug.Debug;

import java.util.Map;
import java.util.Set;

/**
 * Abstrakter Assoziator fuer eine Menge von NS- bzw. FBZ-Sensoren der Art:<br> Hauptsensor, Vorgaenger, Nachfolger,<br> wobei der Hauptsensor im Sinne der
 * Pl-Pruefung langzeit UFD ueberprueft wird
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public abstract class AbstraktPlLangEreignisSensorMenge extends
		AbstraktPlLangSensorMenge<VergleichsEreignisWerte> {

	private long lastTestTime = Long.MIN_VALUE;

	@Override
	public void aktualisiereDaten(final ResultData datum) {
		synchronized(this) {
			UmfeldDatenArt umfeldDatenArt;

			try {
				umfeldDatenArt = UmfeldDatenArt.getUmfeldDatenArtVon(this.prueflingSensor.getObjekt());
			}
			catch(final UmfeldDatenSensorUnbekannteDatenartException e) {
				Debug.getLogger().warning(e.getMessage());
				return;
			}

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

			if(aktuelleParameter.getMaxAbweichungZeit() <= 0) {
				return;
			}

			final VergleichsEreignisWerte aktuellesSensorDatum = this.prueflingSensor
					.getAktuellenVergleichsWert(this.prueflingSensor
							                            .getAktuelleParameter(), testTime);

			final VergleichsEreignisWerte aktuellesNachfolgerDatum = this.nachfolgerSensor
					.getAktuellenVergleichsWert(this.prueflingSensor
							                            .getAktuelleParameter(), testTime);

			final VergleichsEreignisWerte aktuellesVorgaengerDatum = this.vorgaengerSensor
					.getAktuellenVergleichsWert(this.prueflingSensor
							                            .getAktuelleParameter(), testTime);

			if(aktuellesSensorDatum != null
					&& aktuellesNachfolgerDatum != null
					&& aktuellesVorgaengerDatum != null) {
				lastTestTime = testTime;

				final UfdsLangZeitPlPruefungsParameter parameter = this.prueflingSensor
						.getAktuelleParameter();

				if(parameter != null && parameter.isValid()
						&& parameter.getMaxAbweichungZeit() > 0) {

					/**
					 * parametrierter Bezugszeitraum
					 */
					if(aktuellesSensorDatum.getDatenzeitGesamt() > 0
							&& parameter.getMaxAusfallZeit() > 0
							&& parameter.getVergleichsIntervall().getMillis()
							- aktuellesSensorDatum.getDatenzeitGesamt() > parameter
							.getMaxAusfallZeit()) {

						if(datum.getDataTime()
								- this.prueflingSensor.getAktivSeit() >= parameter
								.getVergleichsIntervall().getMillis()) {
							sendMessage2(umfeldDatenArt);
						}
					}
					else {
						final double abweichung = this.getAbweichung(false,
						                                             aktuellesSensorDatum, aktuellesVorgaengerDatum,
						                                             aktuellesNachfolgerDatum
						);

						if(abweichung >= 0
								&& abweichung > parameter
								.getMaxAbweichungZeit()) {
							if(datum.getDataTime()
									- this.prueflingSensor.getAktivSeit() >= parameter
									.getVergleichsIntervall().getMillis()) {
								sendMessage1(datum, umfeldDatenArt, parameter, abweichung, parameter
										.getVergleichsIntervall().getMillis());
							}
						}
					}

					/**
					 * 24h Bezugszeitraum
					 */
					if(aktuellesSensorDatum.getDatenzeitGesamt24() > 0
							&& parameter.getMaxAusfallZeit() > 0
							&& MILLIS_PER_DAY
							- aktuellesSensorDatum
							.getDatenzeitGesamt24() > parameter
							.getMaxAusfallZeit()) {
						if(datum.getDataTime()
								- this.prueflingSensor.getAktivSeit() >= MILLIS_PER_DAY) {
							sendMessage2(umfeldDatenArt);
						}
					}
					else {
						final double abweichung = this.getAbweichung(true,
						                                             aktuellesSensorDatum, aktuellesVorgaengerDatum,
						                                             aktuellesNachfolgerDatum
						);
						if(abweichung >= 0
								&& abweichung > parameter
								.getMaxAbweichungZeit()) {
							if(datum.getDataTime()
									- this.prueflingSensor.getAktivSeit() >= MILLIS_PER_DAY) {
								sendMessage1(datum, umfeldDatenArt, parameter, abweichung, MILLIS_PER_DAY);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Errechnet den Wert AbweichungXY auf Basis der aktuellen Vergleichswerte pro XY-Ereignis aller assoziierten Sensoren.<br> Siehe Afo-4.0 6.6.2.4.7.6 (NS,
	 * S. 109)
	 *
	 * @param intervall24              ob die Abweichung fuer das Bezugsintervall von 24h berechnet werden soll (sonst wird fuer das parametrierbare
	 *                                 Bezugsintervall berechnet)
	 * @param aktuellesSensorDatum     aktuelle Daten des Sensor-Prueflings
	 * @param aktuellesVorgaengerDatum aktuelle Daten des Vorgaengers
	 * @param aktuellesNachfolgerDatum aktuelle Daten des Nachfolgers
	 * @return der Wert AbweichungXY (&gt;= 0) auf Basis der aktuellen Vergleichswerte pro XY-Ereignis aller assoziierten Sensoren oder ein Wert &lt; 0, wenn die
	 * AbweichungXY nicht ermittelt werden konnte
	 */
	private double getAbweichung(final boolean intervall24,
			final VergleichsEreignisWerte aktuellesSensorDatum,
			final VergleichsEreignisWerte aktuellesVorgaengerDatum,
			final VergleichsEreignisWerte aktuellesNachfolgerDatum) {
		double ergebnis = Double.MIN_VALUE;

		if(aktuellesSensorDatum != null && aktuellesNachfolgerDatum != null
				&& aktuellesVorgaengerDatum != null) {

			Map<AbstraktEreignis, Double> sensorVergleichsWerte = null;
			Map<AbstraktEreignis, Double> nachfolgerVergleichsWerte = null;
			Map<AbstraktEreignis, Double> vorgaengerVergleichsWerte = null;
			if(intervall24) {
				sensorVergleichsWerte = aktuellesSensorDatum
						.getVergleichsWerte24();
				nachfolgerVergleichsWerte = aktuellesNachfolgerDatum
						.getVergleichsWerte24();
				vorgaengerVergleichsWerte = aktuellesVorgaengerDatum
						.getVergleichsWerte24();
			}
			else {
				sensorVergleichsWerte = aktuellesSensorDatum
						.getVergleichsWerte();
				nachfolgerVergleichsWerte = aktuellesNachfolgerDatum
						.getVergleichsWerte();
				vorgaengerVergleichsWerte = aktuellesVorgaengerDatum
						.getVergleichsWerte();
			}

			for(AbstraktEreignis ereignis : getEreignisInstanzen()) {
				final Double sensorVergleichsWert = sensorVergleichsWerte
						.get(ereignis);
				final Double vorgaengerVergleichsWert = vorgaengerVergleichsWerte
						.get(ereignis);
				final Double nachfolgerVergleichsWert = nachfolgerVergleichsWerte
						.get(ereignis);

				if(sensorVergleichsWert != null
						&& vorgaengerVergleichsWert != null
						&& nachfolgerVergleichsWert != null) {
					final double ereignisAbweichung = Math
							.abs(sensorVergleichsWert
									     - ((vorgaengerVergleichsWert + nachfolgerVergleichsWert) / 2));
					if(ereignisAbweichung > ergebnis) {
						ergebnis = ereignisAbweichung;
					}
				}
			}
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Menge von Ereignissen, fuer die die Abweichung bzw. ueber denen die Vergleichswerte berechnet werden sollen
	 *
	 * @return die Menge von Ereignissen, fuer die die Abweichung bzw. ueber denen die Vergleichswerte berechnet werden sollen
	 */
	protected abstract Set<? extends AbstraktEreignis> getEreignisInstanzen();

}
