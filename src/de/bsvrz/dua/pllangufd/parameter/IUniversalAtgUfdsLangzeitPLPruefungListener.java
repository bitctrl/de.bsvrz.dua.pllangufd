/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.13 Pl-Pruefung langzeit UFD
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
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */


package de.bsvrz.dua.pllangufd.parameter;

/**
 * Hoert auf Aenderungen innerhalb der Parameter-Attributgruppen
 * <code>atg.ufdsLangzeitPLPr�fungXXX</code>.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public interface IUniversalAtgUfdsLangzeitPLPruefungListener {

	/**
	 * Aktualisiert die Parameter.
	 * 
	 * @param aktuelleParameter
	 *            aktuelle Parameter
	 */
	void aktualisiereParameter(
			UfdsLangZeitPlPruefungsParameter aktuelleParameter);

}
