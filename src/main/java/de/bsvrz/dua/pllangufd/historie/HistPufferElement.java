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

	@Override
	public int compareTo(final IZeitStempel that) {
		return -new Long(this.getZeitStempel()).compareTo(that.getZeitStempel());
	}

	@Override
	public boolean equals(final Object obj) {
		boolean gleich = false;

		if ((obj != null) && (obj instanceof IZeitStempel)) {
			final IZeitStempel that = (IZeitStempel) obj;
			gleich = this.getZeitStempel() == that.getZeitStempel();
		}

		return gleich;
	}

	@Override
	public String toString() {
		return new Long(this.zeitStempel).toString();
	}

	@Override
	public long getZeitStempel() {
		return this.zeitStempel;
	}

}
