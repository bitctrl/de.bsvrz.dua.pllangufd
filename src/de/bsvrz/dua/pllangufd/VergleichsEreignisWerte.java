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
 * Diese Klasse repraesentiert eine Menge von Vergleichswerten (jeweils
 * pro Umfelddatenereignis) der Pl-Pruefung langzeit UFD nach Afo-4.0 (6.6.2.4.7.6, S.108)<br>
 * Die Vergleichswerte werden fuer zwei Intervalle (parametrierbar und 24h) bereitsgestellt 
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class VergleichsEreignisWerte{
	
	/**
	 * Das Ereignis mit seinem Vergleichswert in Bezug auf das
	 * parametrierbare Bezugsintervall
	 */
	private Map<AbstraktEreignis, Double> vergleichsWerte = new HashMap<AbstraktEreignis, Double>();
	
	/**
	 * Das Ereignis mit seinem Vergleichswert in Bezug auf die
	 * letzten 24h
	 */
	private Map<AbstraktEreignis, Double> vergleichsWerte24 = new HashMap<AbstraktEreignis, Double>();
	
	/**
	 * die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf Vergleichsintervall)
	 */
	private long datenZeitGesamt = Long.MIN_VALUE;
	
	/**
	 * die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf 24h)
	 */
	private long datenZeitGesamt24 = Long.MIN_VALUE;


	/**
	 * Erfragt, ob die Vergleichswerte schon gesetzt wurden
	 * 
	 * @return ob die Vergleichswerte schon gesetzt wurde
	 */
	public final boolean isValid(){
		return this.vergleichsWerte.values().size() > 0;
	}


	/**
	 * Erfragt, ob die Vergleichswerte fuer den Bezugszeitraum von 24h 
	 * schon gesetzt wurden
	 * 
	 * @return ob die Vergleichswerte fuer den Bezugszeitraum von 24h 
	 * schon gesetzt wurden
	 */
	public final boolean isValid24(){
		return this.vergleichsWerte24.values().size() > 0;
	}


	/**
	 * Erfragt die Vergleichswerte fuer die Pl-Pruefung langzeit UFD 
	 * 
	 * @return die Vergleichswerte fuer die Pl-Pruefung langzeit UFD
	 */
	public final Map<AbstraktEreignis, Double> getVergleichsWerte(){
		return this.vergleichsWerte;
	}


	/**
	 * Erfragt die Vergleichswerte berechnet fuer einen
	 * Bezugszeitraum von 24h fuer die Pl-Pruefung langzeit UFD 
	 * 
	 * @return die Vergleichswerte berechnet fuer einen
	 * Bezugszeitraum von 24h fuer die Pl-Pruefung langzeit UFD
	 */
	public final Map<AbstraktEreignis, Double> getVergleichsWerte24(){
		return this.vergleichsWerte24;
	}

	
	/**
	 * Erfragt die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf Vergleichsintervall)
	 * 
	 * @return die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf Vergleichsintervall)
	 */
	public final long getDatenzeitGesamt(){
		return this.datenZeitGesamt;
	}

	
	/**
	 * Erfragt die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf 24h)
	 * 
	 * @return die Zeit, innerhalb der (sinnvolle) Daten vorliegen (bezogen auf 24h)
	 */
	public final long getDatenzeitGesamt24(){
		return this.datenZeitGesamt24;
	}
	
	
	/**
	 * Setzte den Inhalt dieses Objektes
	 * 
	 * @param intervall24 indiziert, dass die Werte fuer das Vergleichsintervall von 24 Stunden 
	 * gesendet gesetzt werden sollen
	 * @param gesamtDatenzeit die absolute Zeit, innerhalb der Daten vorliegen
	 * @param vergleichsWerte die Ausfallwerte in ms pro Ereignis
	 */
	public final void setInhalt(final boolean intervall24,
								final long gesamtDatenzeit,
								final Map<AbstraktEreignis, Double> vergleichsWerte){
		if(intervall24){
			this.datenZeitGesamt24 = gesamtDatenzeit;
			this.vergleichsWerte24 = vergleichsWerte;
		}else{
			this.datenZeitGesamt = gesamtDatenzeit;
			this.vergleichsWerte = vergleichsWerte;
		}
	}
	
}
