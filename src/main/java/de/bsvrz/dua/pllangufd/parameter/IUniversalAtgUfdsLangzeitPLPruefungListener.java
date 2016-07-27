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



package de.bsvrz.dua.pllangufd.parameter;

/**
 * Hoert auf Aenderungen innerhalb der Parameter-Attributgruppen
 * <code>atg.ufdsLangzeitPLPrüfungXXX</code>.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id: IUniversalAtgUfdsLangzeitPLPruefungListener.java 53825 2015-03-18 09:36:42Z peuker $
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
