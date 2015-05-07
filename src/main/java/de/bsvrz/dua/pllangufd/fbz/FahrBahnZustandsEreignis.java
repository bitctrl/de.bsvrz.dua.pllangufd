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


package de.bsvrz.dua.pllangufd.fbz;

import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dua.pllangufd.AbstraktEreignis;

/**
 * Bildet die Zustaende des Attributs
 * <code>att.ufdsFahrBahnOberFlächenZustand</code> auf innerhalb der
 * Pl-Pruefung langzeit UFD benoetigte Ereignisse ab.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class FahrBahnZustandsEreignis extends AbstraktEreignis {

	/**
	 * die statischen Instanzen dieser Klasse.
	 */
	private static Set<FahrBahnZustandsEreignis> instanzen = new HashSet<>();

	/**
	 * Wert <code>nicht ermittelbar/fehlerhaft</code>.
	 */
	public static final FahrBahnZustandsEreignis NICHT_ERMITTELBAR_FEHLERHAFT = new FahrBahnZustandsEreignis(
			-3, -3, "nicht ermittelbar/fehlerhaft"); //$NON-NLS-1$

	/**
	 * Wert <code>fehlerhaft</code>.
	 */
	public static final FahrBahnZustandsEreignis FEHLERHAFT = new FahrBahnZustandsEreignis(
			-2, -2, "fehlerhaft"); //$NON-NLS-1$

	/**
	 * Wert <code>nicht ermittelbar</code>.
	 */
	public static final FahrBahnZustandsEreignis NICHT_ERMITTELBAR = new FahrBahnZustandsEreignis(
			-1, -1, "nicht ermittelbar"); //$NON-NLS-1$

	/**
	 * Wert <code>trocken</code>.
	 */
	public static final FahrBahnZustandsEreignis TROCKEN = new FahrBahnZustandsEreignis(
			0, 0, "trocken"); //$NON-NLS-1$

	/**
	 * Wert <code>feucht</code>.
	 */
	public static final FahrBahnZustandsEreignis FEUCHT = new FahrBahnZustandsEreignis(
			1, 1, "feucht"); //$NON-NLS-1$

	/**
	 * Wert <code>nass</code>.
	 */
	public static final FahrBahnZustandsEreignis NASS = new FahrBahnZustandsEreignis(
			32, 32, "nass"); //$NON-NLS-1$

	/**
	 * Wert <code>gefrorenes Wasser</code>.
	 */
	public static final FahrBahnZustandsEreignis GEFRORENES_WASSER = new FahrBahnZustandsEreignis(
			64, 64, "gefrorenes Wasser"); //$NON-NLS-1$

	/**
	 * Wert <code>Schnee/Schneematsch</code>.
	 */
	public static final FahrBahnZustandsEreignis SCHNEE_SCHNEEMATSCH = new FahrBahnZustandsEreignis(
			65, 65, "Schnee/Schneematsch"); //$NON-NLS-1$

	/**
	 * Wert <code>Eis</code>.
	 */
	public static final FahrBahnZustandsEreignis EIS = new FahrBahnZustandsEreignis(
			66, 66, "Eis"); //$NON-NLS-1$	

	/**
	 * Wert <code>Raureif</code>.
	 */
	public static final FahrBahnZustandsEreignis RAUREIF = new FahrBahnZustandsEreignis(
			67, 67, "Raureif"); //$NON-NLS-1$

	/**
	 * Standardkonstruktor.
	 * 
	 * @param intervallAnfang
	 *            unteres Ende des (beidseitig abgeschlossenen) Intervalls,
	 *            innerhalb dem die Werte fuer dieses Ereignis liegen
	 * @param intervallEnde
	 *            oberes Ende des (beidseitig abgeschlossenen) Intervalls,
	 *            innerhalb dem die Werte fuer dieses Ereignis liegen
	 * @param name
	 *            der Name des Ereignisses
	 */
	private FahrBahnZustandsEreignis(final int intervallAnfang,
			final int intervallEnde, final String name) {
		super(intervallAnfang, intervallEnde, name);
		instanzen.add(this);
	}

	/**
	 * Erfragt die statischen Instanzen dieser Klasse.
	 * 
	 * @return die statischen Instanzen dieser Klasse
	 */
	public static Set<FahrBahnZustandsEreignis> getInstanzen() {
		return instanzen;
	}

}
