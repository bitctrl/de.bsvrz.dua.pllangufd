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


package de.bsvrz.dua.pllangufd.historie;

import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IZeitStempel;

/**
 * Allgemeines <b>sortierbares</b> Objekt mit Zeitstempel (fuer die Klasse
 * <code>HistorischerDatenpuffer</code>).
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class HistPufferElement implements IZeitStempel {

	/**
	 * der Zeitstempel, nach dem dieses Element einsortiert wird.
	 */
	private long zeitStempel = Long.MIN_VALUE;

	/**
	 * Standardkonstruktor.
	 * 
	 * @param zeitStempel
	 *            der Zeitstempel
	 */
	public HistPufferElement(final long zeitStempel) {
		this.zeitStempel = zeitStempel;
	}

	public int compareTo(final IZeitStempel that) {
		return -new Long(this.getZeitStempel())
				.compareTo(that.getZeitStempel());
	}

	@Override
	public boolean equals(final Object obj) {
		boolean gleich = false;

		if (obj != null && obj instanceof IZeitStempel) {
			final IZeitStempel that = (IZeitStempel) obj;
			gleich = this.getZeitStempel() == that.getZeitStempel();
		}

		return gleich;
	}

	@Override
	public String toString() {
		return new Long(this.zeitStempel).toString();
	}

	public long getZeitStempel() {
		return this.zeitStempel;
	}

}
