/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.13 PL-Pruefung Langzeit UFD
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
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */



package de.bsvrz.dua.pllangufd;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.bitctrl.Constants;

import de.bsvrz.dua.pllangufd.historie.HistorischerUfdsWert;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;

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
			long aktuellerZeitStempel) {
		VergleichsEreignisWerte ergebnis = null;

		synchronized (this) {
			if (!this.hitorie24.getPufferInhalt().isEmpty()
					&& parameter != null && parameter.isValid()) {

				ergebnis = new VergleichsEreignisWerte();
				/**
				 * berechne Vergleichswerte fuer parametriertes
				 * Vergleichsintervall
				 */
				long intervallAnfang = aktuellerZeitStempel
						- parameter.getVergleichsIntervall().getMillis();
				SortedSet<HistorischerUfdsWert> historieVergleich = this.hitorie24
						.cloneTeilMenge(intervallAnfang, aktuellerZeitStempel);

				if (!historieVergleich.isEmpty()) {
					this.setVergleichsWerte(false, historieVergleich, ergebnis);
				}

				/**
				 * berechne Vergleichswerte fuer letzte 24h
				 */
				long intervallAnfang24 = aktuellerZeitStempel
						- Constants.MILLIS_PER_DAY;
				SortedSet<HistorischerUfdsWert> historieVergleich24 = this.hitorie24
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
			SortedSet<HistorischerUfdsWert> historischeWerte,
			VergleichsEreignisWerte vergleichsEreignisWerte) {
		Map<AbstraktEreignis, Double> ergebnisse = new HashMap<AbstraktEreignis, Double>();
		long datenzeitGesamt = 0;

		for (HistorischerUfdsWert historischerWert : historischeWerte) {
			if (historischerWert.getWert() != null
					&& historischerWert.getWert().isOk()) {
				datenzeitGesamt += historischerWert.getT();
				for (AbstraktEreignis ereignis : this.getEreignisInstanzen()) {
					if (ereignis.isZustandInEreignis((int) historischerWert
							.getWert().getWert())) {
						Double ergebnisWert = ergebnisse.get(ereignis);
						if (ergebnisWert == null) {
							ergebnisse.put(ereignis, new Double(
									historischerWert.getT()));
						} else {
							ergebnisse.put(ereignis, ergebnisWert
									+ new Double(historischerWert.getT()));
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
