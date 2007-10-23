/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.13 PL-Pruefung Langzeit UFD
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
package de.bsvrz.dua.pllangufd;

import java.util.HashMap;
import java.util.Map;

/**
 * Bildet die Zustaende des Attributs <code>att.ufdsNiederschlagsArt</code> auf
 * innerhalb der Pl-Pruefung langzeit UFD benoetigte Niederschlagsereignisse ab
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class NiederschlagsEreignis {

	/**
	 * die statischen Instanzen dieser Klasse
	 */
	private static Map<Integer, NiederschlagsEreignis> INSTANZEN = new HashMap<Integer, NiederschlagsEreignis>();

	public static NiederschlagsEreignis NICHT_ERMITTELBAR = new NiederschlagsEreignis(-3, -1, "nicht ermittelbar");  //$NON-NLS-1$

	public static NiederschlagsEreignis KEIN_NS = new NiederschlagsEreignis(0, 0, "kein Niederschlag");  //$NON-NLS-1$
	
	public static NiederschlagsEreignis REGEN = new NiederschlagsEreignis(50, 69, "Regen");  //$NON-NLS-1$

	public static NiederschlagsEreignis SCHNEE = new NiederschlagsEreignis(70, 73, "Schnee");  //$NON-NLS-1$

	public static NiederschlagsEreignis HAGEL = new NiederschlagsEreignis(77, 79, "Hagel");  //$NON-NLS-1$

	public static NiederschlagsEreignis GRAUPEL = new NiederschlagsEreignis(74, 76, "Graupel");  //$NON-NLS-1$

	public static NiederschlagsEreignis UNBESTIMMT = new NiederschlagsEreignis(40, 42, "unbestimmter Niederschlag");  //$NON-NLS-1$

	/**
	 * der Name des Niederschlagsereignisses
	 */
	private String name = null;
		
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param intervallAnfang unteres Ende des (beidseitig abgeschlossenen) Intervalls, innerhalb
	 * dem die Werte fuer dieses Niederschlagsereignis liegen
	 * @param intervallEnde oberes Ende des (beidseitig abgeschlossenen) Intervalls, innerhalb
	 * dem die Werte fuer dieses Niederschlagsereignis liegen
	 * @param name der Name des Niederschlagsereignisses
	 */
	private NiederschlagsEreignis(final int intervallAnfang, 
								  final int intervallEnde,
								  final String name){
		this.name = name;
		for(int i=intervallAnfang; i<=intervallEnde; i++){
			INSTANZEN.put(new Integer(i), this);	
		}
	}
	
	
	/**
	 * Erfragt das Niederschlagsereignis, das mit dem uebergebenen Zustand assoziiert ist
	 * 
	 * @param zustand ein NS-Zustand 
	 * @return das Niederschlagsereignis, das mit dem uebergebenen Zustand assoziiert ist
	 */
	public static final NiederschlagsEreignis getEreignis(int zustand){
		NiederschlagsEreignis ergebnis = INSTANZEN.get(new Integer(zustand));
		
		if(ergebnis == null){
			throw new RuntimeException("Der Zustand " + zustand + //$NON-NLS-1$
					" existiert innerhalb von att.ufdsNiederschlagsArt nicht"); //$NON-NLS-1$
		}
		
		return ergebnis;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.name;
	}

}
