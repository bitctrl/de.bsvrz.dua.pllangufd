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
 * Ein Ereignis ist eine Kumulation von DAV-Zustaenden wie sie innerhalb der SWE
 * PL-Pruefung langzeit UFD benoetigt wird.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id: AbstraktEreignis.java 53825 2015-03-18 09:36:42Z peuker $
 */
public class AbstraktEreignis {

	/**
	 * Begin des abgeschlossenen Intervalls, innerhalb dem die Zustaende dieses
	 * Ereignisses liegen.
	 */
	public int intervallBegin = 0;

	/**
	 * Ende des abgeschlossenen Intervalls, innerhalb dem die Zustaende dieses
	 * Ereignisses liegen.
	 */
	public int intervallEnde = 0;

	/**
	 * der Name des Ereignisses.
	 */
	private String name = null;

	/**
	 * Standardkonstruktor.
	 * 
	 * @param intervallBegin
	 *            Begin des abgeschlossenen Intervalls, innerhalb dem die
	 *            Zustaende dieses Ereignisses liegen.
	 * @param intervallEnde
	 *            Ende des abgeschlossenen Intervalls, innerhalb dem die
	 *            Zustaende dieses Ereignisses liegen.
	 * @param name
	 *            der Name des Ereignisses.
	 */
	protected AbstraktEreignis(final int intervallBegin,
			final int intervallEnde, final String name) {
		if (intervallBegin > intervallEnde) {
			throw new IllegalArgumentException(
					"Intervallanfang darf nicht (echt) nach Intervallende liegen"); //$NON-NLS-1$
		}
		this.intervallBegin = intervallBegin;
		this.intervallEnde = intervallEnde;
		this.name = name;
	}

	/**
	 * Erfragt, ob ein bestimmter Zustand innerhalb des abgeschlossenen
	 * Intervalls liegt, durch das dieses Ereignis beschrieben ist.
	 * 
	 * @param zustand
	 *            ein DAV-Zustand
	 * @return ob ein bestimmter Zustand innerhalb des abgeschlossenen
	 *         Intervalls liegt, durch das dieses Ereignis beschrieben ist
	 */
	public final boolean isZustandInEreignis(final int zustand) {
		return this.intervallBegin <= zustand && zustand <= this.intervallEnde;
	}

	/**
	 * Erfragt den Begin des abgeschlossenen Intervalls, innerhalb dem die
	 * Zustaende dieses Ereignisses liegen.
	 * 
	 * @return der Begin des abgeschlossenen Intervalls, innerhalb dem die
	 *         Zustaende dieses Ereignisses liegen
	 */
	public final int getIntervallBegin() {
		return this.intervallBegin;
	}

	/**
	 * Erfragt das Ende des abgeschlossenen Intervalls, innerhalb dem die
	 * Zustaende dieses Ereignisses liegen.
	 * 
	 * @return das Ende des abgeschlossenen Intervalls, innerhalb dem die
	 *         Zustaende dieses Ereignisses liegen
	 */
	public final int getIntervallEnde() {
		return this.intervallEnde;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.name;
	}

}
