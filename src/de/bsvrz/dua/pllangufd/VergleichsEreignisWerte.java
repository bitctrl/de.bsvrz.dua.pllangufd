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
	 * der gemessene Ausfall am Sensor
	 */
	private double ausfallGemessen = Double.NaN;
	
	/**
	 * der gemessene Ausfall am Sensor auf 24h
	 */
	private double ausfallGemessen24 = Double.NaN;


	/**
	 * Setzt die Vergleichswerte der Pl-Pruefung langzeit UFD
	 * nach Afo-4.0 (6.6.2.4.7.6, S.108)
	 * 
	 * @param vergleichsWerte Vergleichswerte in Bezug auf das
	 * parametrierbare Bezugsintervall
	 */
	public final void setVergleichsWerte(Map<AbstraktEreignis, Double> vergleichsWerte){
		this.vergleichsWerte = vergleichsWerte;		
	}

	/**
	 * Setzt die Vergleichswerte der Pl-Pruefung langzeit UFD
	 * nach Afo-4.0 (24h) (6.6.2.4.7.6, S.108)
	 * 
	 * @param vergleichsWerte Vergleichswerte in Bezug auf das
	 * 24h-Bezugsintervall
	 */
	public final void setVergleichsWerte24(Map<AbstraktEreignis, Double> vergleichsWerte24){
		this.vergleichsWerte24 = vergleichsWerte24;		
	}


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
	 * Setzt den Ausfall
	 * 
	 * @param ausfall der Ausfall
	 */
	public final void setAusfall(final double ausfall){
		this.ausfallGemessen = ausfall;
	}
	
	
	/**
	 * Erfragt den gemessenen Ausfall
	 * 
	 * @return der gemessene Ausfall
	 */
	public final double getAusfall(){
		return this.ausfallGemessen;
	}
	

	/**
	 * Setzt den Ausfall (24h)
	 * 
	 * @param ausfall der Ausfall (24h)
	 */
	public final void setAusfall24(final double ausfall){
		this.ausfallGemessen24 = ausfall;
	}
	
	
	/**
	 * Erfragt den gemessenen Ausfall (24h)
	 * 
	 * @return der gemessene Ausfall (24h)
	 */
	public final double getAusfall24(){
		return this.ausfallGemessen24;
	}
	
}
