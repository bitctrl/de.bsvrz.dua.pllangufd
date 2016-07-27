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

import java.util.HashMap;
import java.util.Map;

/**
 * Diese Klasse repraesentiert eine Menge von Vergleichswerten (jeweils pro
 * Umfelddatenereignis) der Pl-Pruefung langzeit UFD nach Afo-4.0 (6.6.2.4.7.6,
 * S.108)<br>
 * Die Vergleichswerte werden fuer zwei Intervalle (parametrierbar und 24h)
 * bereitsgestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id: VergleichsEreignisWerte.java 53825 2015-03-18 09:36:42Z peuker $
 */
public class VergleichsEreignisWerte {

	/**
	 * Das Ereignis mit seinem Vergleichswert in Bezug auf das parametrierbare
	 * Bezugsintervall.
	 */
	private Map<AbstraktEreignis, Double> vergleichsWerte = new HashMap<AbstraktEreignis, Double>();

	/**
	 * Das Ereignis mit seinem Vergleichswert in Bezug auf die letzten 24h.
	 */
	private Map<AbstraktEreignis, Double> vergleichsWerte24 = new HashMap<AbstraktEreignis, Double>();

	/**
	 * die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf
	 * Vergleichsintervall).
	 */
	private long datenZeitGesamt = Long.MIN_VALUE;

	/**
	 * die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf 24h).
	 */
	private long datenZeitGesamt24 = Long.MIN_VALUE;

	/**
	 * Erfragt, ob die Vergleichswerte schon gesetzt wurden.
	 * 
	 * @return ob die Vergleichswerte schon gesetzt wurde
	 */
	public final boolean isValid() {
		return this.vergleichsWerte.values().size() > 0;
	}

	/**
	 * Erfragt, ob die Vergleichswerte fuer den Bezugszeitraum von 24h schon
	 * gesetzt wurden.
	 * 
	 * @return ob die Vergleichswerte fuer den Bezugszeitraum von 24h schon
	 *         gesetzt wurden
	 */
	public final boolean isValid24() {
		return this.vergleichsWerte24.values().size() > 0;
	}

	/**
	 * Erfragt die Vergleichswerte fuer die Pl-Pruefung langzeit UFD.
	 * 
	 * @return die Vergleichswerte fuer die Pl-Pruefung langzeit UFD
	 */
	public final Map<AbstraktEreignis, Double> getVergleichsWerte() {
		return this.vergleichsWerte;
	}

	/**
	 * Erfragt die Vergleichswerte berechnet fuer einen Bezugszeitraum von 24h
	 * fuer die Pl-Pruefung langzeit UFD.
	 * 
	 * @return die Vergleichswerte berechnet fuer einen Bezugszeitraum von 24h
	 *         fuer die Pl-Pruefung langzeit UFD
	 */
	public final Map<AbstraktEreignis, Double> getVergleichsWerte24() {
		return this.vergleichsWerte24;
	}

	/**
	 * Erfragt die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf
	 * Vergleichsintervall).
	 * 
	 * @return die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf
	 *         Vergleichsintervall)
	 */
	public final long getDatenzeitGesamt() {
		return this.datenZeitGesamt;
	}

	/**
	 * Erfragt die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf
	 * 24h).
	 * 
	 * @return die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf
	 *         24h)
	 */
	public final long getDatenzeitGesamt24() {
		return this.datenZeitGesamt24;
	}

	/**
	 * Setzte den Inhalt dieses Objektes.
	 * 
	 * @param intervall24
	 *            indiziert, dass die Werte fuer das Vergleichsintervall von 24
	 *            Stunden gesendet gesetzt werden sollen
	 * @param gesamtDatenzeit
	 *            die absolute Zeit, innerhalb der Daten vorliegen
	 * @param vergleichsWerte1
	 *            die Ausfallwerte in ms pro Ereignis
	 */
	public final void setInhalt(final boolean intervall24,
			final long gesamtDatenzeit,
			final Map<AbstraktEreignis, Double> vergleichsWerte1) {
		if (intervall24) {
			this.datenZeitGesamt24 = gesamtDatenzeit;
			this.vergleichsWerte24 = vergleichsWerte1;
		} else {
			this.datenZeitGesamt = gesamtDatenzeit;
			this.vergleichsWerte = vergleichsWerte1;
		}
	}

}
