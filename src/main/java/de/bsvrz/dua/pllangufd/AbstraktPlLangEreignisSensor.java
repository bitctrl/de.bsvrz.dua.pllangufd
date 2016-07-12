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

import de.bsvrz.dua.pllangufd.historie.HistorischerUfdsWert;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Abstrakter Umfelddatensensor fuer die PL-Pruefung langzeit UFD mit aktuellen
 * Parametern fuer Ereignis-getriggerte Pruefung.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public abstract class AbstraktPlLangEreignisSensor extends
		AbstraktPlLangSensor<VergleichsEreignisWerte> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VergleichsEreignisWerte getAktuellenVergleichsWert(
			final UfdsLangZeitPlPruefungsParameter parameter,
			final long aktuellerZeitStempel) {
		VergleichsEreignisWerte ergebnis = null;

		synchronized (this) {
			if (!this.historie24.getPufferInhalt().isEmpty()
					&& parameter != null && parameter.isValid()) {

				ergebnis = new VergleichsEreignisWerte();
				/**
				 * berechne Vergleichswerte fuer parametriertes
				 * Vergleichsintervall
				 */
				final long intervallAnfang = aktuellerZeitStempel
						- parameter.getVergleichsIntervall().getMillis();
				final SortedSet<HistorischerUfdsWert> historieVergleich = this.historie24
						.cloneTeilMenge(intervallAnfang, aktuellerZeitStempel);

				if(historieVergleich.isEmpty()){
					return null;
				}
				HistorischerUfdsWert last = historieVergleich.first();
				if(last.getZeitStempel() != aktuellerZeitStempel) {
					return null;
				}
				this.setVergleichsWerte(false, historieVergleich, ergebnis);

				/**
				 * berechne Vergleichswerte fuer letzte 24h
				 */
				final long intervallAnfang24 = aktuellerZeitStempel
						- MILLIS_PER_DAY;
				final SortedSet<HistorischerUfdsWert> historieVergleich24 = this.historie24
						.cloneTeilMenge(intervallAnfang24, aktuellerZeitStempel);

				if (!historieVergleich24.isEmpty()) {
					this
							.setVergleichsWerte(true, historieVergleich24,
									ergebnis);
				}
			}
		}

		return ergebnis;
	}

	/**
	 * Berechnet aus den uebergebenen historischen Werten die Vergleichswerte
	 * pro Ereignis und die gesamte Datenzeit.
	 * 
	 * @param intervall24
	 *            indiziert, dass die Werte fuer das Vergleichsintervall von 24
	 *            Stunden gesendet gesetzt werden sollen
	 * @param historischeWerte
	 *            historische Umfelddatenwerte eines bestimmten Bezugszeitraums
	 * @param vergleichsEreignisWerte
	 *            Puffer fuer die Vergleichswerte pro Ereignis und die gesamte
	 *            Ausfallzeit
	 */
	protected final void setVergleichsWerte(final boolean intervall24,
			final SortedSet<HistorischerUfdsWert> historischeWerte,
			final VergleichsEreignisWerte vergleichsEreignisWerte) {
		final Map<AbstraktEreignis, Double> ergebnisse = new HashMap<AbstraktEreignis, Double>();
		long datenzeitGesamt = 0;

		for (HistorischerUfdsWert historischerWert : historischeWerte) {
			if (historischerWert.getWert() != null
					&& historischerWert.getWert().isOk()) {
				datenzeitGesamt += historischerWert.getT();
				for (AbstraktEreignis ereignis : this.getEreignisInstanzen()) {
					if (ereignis.isZustandInEreignis((int) historischerWert
							.getWert().getWert())) {
						final Double ergebnisWert = ergebnisse.get(ereignis);
						if (ergebnisWert == null) {
							ergebnisse.put(ereignis, (double) historischerWert.getT());
						} else {
							ergebnisse.put(ereignis, ergebnisWert
									+ (double) historischerWert.getT());
						}
						break;
					}
				}
			}
		}

		vergleichsEreignisWerte.setInhalt(intervall24, datenzeitGesamt,
				ergebnisse);
	}

	/**
	 * Erfragt die Menge von Ereignissen, fuer die die Abweichung bzw. ueber
	 * denen die Vergleichswerte berechnet werden sollen
	 * 
	 * @return die Menge von Ereignissen, fuer die die Abweichung bzw. ueber
	 *         denen die Vergleichswerte berechnet werden sollen
	 */
	protected abstract Set<? extends AbstraktEreignis> getEreignisInstanzen();

}
