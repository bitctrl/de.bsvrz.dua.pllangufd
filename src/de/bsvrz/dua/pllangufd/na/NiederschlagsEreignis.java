/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.13 Pl-Pruefung langzeit UFD
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

package de.bsvrz.dua.pllangufd.na;

import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dua.pllangufd.AbstraktEreignis;

/**
 * Bildet die Zustaende des Attributs <code>att.ufdsNiederschlagsArt</code>
 * auf innerhalb der Pl-Pruefung langzeit UFD benoetigte Niederschlagsereignisse
 * ab.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
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

	/**
	 * die statischen Instanzen dieser Klasse.
	 */
	public static final NiederschlagsEreignis HAGEL = new NiederschlagsEreignis(77,
			79, "Hagel"); //$NON-NLS-1$

	/**
	 * die statischen Instanzen dieser Klasse.
	 */
	public static final NiederschlagsEreignis GRAUPEL = new NiederschlagsEreignis(74,
			76, "Graupel"); //$NON-NLS-1$

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
