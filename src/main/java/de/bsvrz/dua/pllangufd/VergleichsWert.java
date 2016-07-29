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

/**
 * Diese Klasse repraesentiert das aktuelle Datum eines Umfelddatensensors mit
 * dem Vergleichswert der Pl-Pruefung langzeit UFD nach Afo-4.0 (6.6.2.4.7.6,
 * S.108).
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class VergleichsWert {

	/**
	 * der Vergleichswert.
	 */
	private double vergleichsWert = Double.NaN;

	/**
	 * der Vergleichswert berechnet fuer einen Bezugszeitraum von 24h.
	 */
	private double vergleichsWert24 = Double.NaN;

	/**
	 * Setzt den Vergleichswert der Pl-Pruefung langzeit UFD nach Afo-4.0
	 * (6.6.2.4.7.6, S.108).
	 * 
	 * @param vergleichsWert
	 *            der Vergleichswert
	 */
	public final void setVergleichsWert(final double vergleichsWert) {
		this.vergleichsWert = vergleichsWert;
	}

	/**
	 * Setzt den Vergleichswert fuer einen Bezugszeitraum von 24h der
	 * Pl-Pruefung langzeit UFD nach Afo-4.0 (6.6.2.4.7.6, S.108).
	 * 
	 * @param vergleichsWert24
	 *            der Vergleichswert berechnet fuer einen Bezugszeitraum von 24h
	 */
	public final void setVergleichsWert24(final double vergleichsWert24) {
		this.vergleichsWert24 = vergleichsWert24;
	}

	/**
	 * Erfragt, ob der Vergleichswert schon gesetzt wurde.
	 * 
	 * @return ob der Vergleichswert schon gesetzt wurde
	 */
	public final boolean isValid() {
		return !Double.isNaN(this.vergleichsWert);
	}

	/**
	 * Erfragt, ob der Vergleichswert fuer den Bezugszeitraum von 24h schon
	 * gesetzt wurde.
	 * 
	 * @return ob der Vergleichswert fuer den Bezugszeitraum von 24h schon
	 *         gesetzt wurde
	 */
	public final boolean isValid24() {
		return !Double.isNaN(this.vergleichsWert24);
	}

	/**
	 * Erfragt den Vergleichswert fuer die Pl-Pruefung langzeit UFD.
	 * 
	 * @return der Vergleichswert fuer die Pl-Pruefung langzeit UFD
	 */
	public final double getVergleichsWert() {
		return this.vergleichsWert;
	}

	/**
	 * Erfragt den Vergleichswert berechnet fuer einen Bezugszeitraum von 24h
	 * fuer die Pl-Pruefung langzeit UFD.
	 * 
	 * @return der Vergleichswert berechnet fuer einen Bezugszeitraum von 24h
	 *         fuer die Pl-Pruefung langzeit UFD
	 */
	public final double getVergleichsWert24() {
		return this.vergleichsWert24;
	}

	@Override
	public String toString() {
		return String.valueOf(vergleichsWert);
	}
}
