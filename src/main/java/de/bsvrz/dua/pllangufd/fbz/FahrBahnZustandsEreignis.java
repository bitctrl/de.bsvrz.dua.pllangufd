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


package de.bsvrz.dua.pllangufd.fbz;

import de.bsvrz.dua.pllangufd.AbstraktEreignis;

import java.util.HashSet;
import java.util.Set;

/**
 * Bildet die Zustaende des Attributs
 * <code>att.ufdsFahrBahnOberFlächenZustand</code> auf innerhalb der
 * Pl-Pruefung langzeit UFD benoetigte Ereignisse ab.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id: FahrBahnZustandsEreignis.java 53825 2015-03-18 09:36:42Z peuker $
 */
public final class FahrBahnZustandsEreignis extends AbstraktEreignis {

	/**
	 * die statischen Instanzen dieser Klasse.
	 */
	private static Set<FahrBahnZustandsEreignis> instanzen = new HashSet<FahrBahnZustandsEreignis>();

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
