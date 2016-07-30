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


package de.bsvrz.dua.pllangufd.na;

import de.bsvrz.dua.pllangufd.AbstraktEreignis;

import java.util.HashSet;
import java.util.Set;

/**
 * Bildet die Zustaende des Attributs <code>att.ufdsNiederschlagsArt</code>
 * auf innerhalb der Pl-Pruefung langzeit UFD benoetigte Niederschlagsereignisse
 * ab.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class NiederschlagsEreignis extends AbstraktEreignis {

	/**
	 * die statischen Instanzen dieser Klasse.
	 */
	private static final Set<NiederschlagsEreignis> INSTANZEN = new HashSet<NiederschlagsEreignis>();

	/**
	 * die statischen Instanzen dieser Klasse.
	 */
	public static final NiederschlagsEreignis NICHT_ERMITTELBAR = new NiederschlagsEreignis(
			-3, -1, "nicht ermittelbar"); //$NON-NLS-1$

	/**
	 * die statischen Instanzen dieser Klasse.
	 */
	public static final NiederschlagsEreignis KEIN_NS = new NiederschlagsEreignis(0,
			0, "kein Niederschlag"); //$NON-NLS-1$

	/**
	 * die statischen Instanzen dieser Klasse.
	 */
	public static final NiederschlagsEreignis REGEN = new NiederschlagsEreignis(50,
			69, "Regen"); //$NON-NLS-1$

	/**
	 * die statischen Instanzen dieser Klasse.
	 */
	public static final NiederschlagsEreignis SCHNEE = new NiederschlagsEreignis(70,
			73, "Schnee"); //$NON-NLS-1$

//	/**
//	 * die statischen Instanzen dieser Klasse.
//	 */
//	public static final NiederschlagsEreignis HAGEL = new NiederschlagsEreignis(77,
//			79, "Hagel"); //$NON-NLS-1$
//
//	/**
//	 * die statischen Instanzen dieser Klasse.
//	 */
//	public static final NiederschlagsEreignis GRAUPEL = new NiederschlagsEreignis(74,
//			76, "Graupel"); //$NON-NLS-1$

	/**
	 * die statischen Instanzen dieser Klasse.
	 */
	public static final NiederschlagsEreignis UNBESTIMMT = new NiederschlagsEreignis(
			40, 42, "unbestimmter Niederschlag"); //$NON-NLS-1$

	/**
	 * Standardkonstruktor.
	 * 
	 * @param intervallAnfang
	 *            unteres Ende des (beidseitig abgeschlossenen) Intervalls,
	 *            innerhalb dem die Werte fuer dieses Niederschlagsereignis
	 *            liegen
	 * @param intervallEnde
	 *            oberes Ende des (beidseitig abgeschlossenen) Intervalls,
	 *            innerhalb dem die Werte fuer dieses Niederschlagsereignis
	 *            liegen
	 * @param name
	 *            der Name des Niederschlagsereignisses
	 */
	private NiederschlagsEreignis(final int intervallAnfang,
			final int intervallEnde, final String name) {
		super(intervallAnfang, intervallEnde, name);
		INSTANZEN.add(this);
	}

	/**
	 * Erfragt die statischen Instanzen dieser Klasse.
	 * 
	 * @return die statischen Instanzen dieser Klasse
	 */
	public static Set<NiederschlagsEreignis> getInstanzen() {
		return INSTANZEN;
	}

}
