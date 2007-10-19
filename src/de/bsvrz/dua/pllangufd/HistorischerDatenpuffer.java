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

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Speichert eine Menge von Datensaetzen, die innerhalb eines bestimmten 
 * Intervalls liegen
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class HistorischerDatenpuffer<G extends IZeitStempel>{
	
	/**
	 * nach Zeitstempeln sortierter Datenpuffer
	 */
	private SortedSet<G> puffer = new TreeSet<G>();
	
	/**
	 * aktelle Maximallaenge des Pufferintervalls
	 */
	private long intervallLaenge = 0;
		
	
	/**
	 * Fuegt ein Datum in diesen Puffer ein und loescht gleichzeitig 
	 * alle Elemente aus dem Puffer, die nicht mehr im Intervall liegen
	 * 
	 * @param datum das Datum
	 */
	public final void addDatum(final G datum){
		synchronized(this){
			this.puffer.add(datum);
		}
		this.aufraeumen();
	}
	
	
	/**
	 * Setzt die Intervalllaenge.<br>
	 * Es duerfen nur Daten im Puffer stehen, die einen Zeitstempel <code>t</code>
	 * mit folgender Eigenschaft haben:<br><br>
	 * 
	 * <code>a - l <= t <= a</code>, mit<br><br>
	 * 
	 * a = aktuellster Zeitstempel im Puffer und<br>
	 * l = Intervalllaenge
	 * 
	 * @param intervallLaenge neue Intervalllaenge
	 */
	public final synchronized void setIntervallLaenge(final long intervallLaenge){
		if(this.intervallLaenge != intervallLaenge){
			this.intervallLaenge = intervallLaenge;
			this.aufraeumen();
		}
	}
	
	
	/**
	 * Erfragt den Teil des Pufferinhalts, der noch innerhalb des durch die
	 * uebergebene Intervalllange beschriebenen verkuerzten Intervalls liegt
	 * 
	 * @param andereIntervallLaenge eine andere Intervalllaenge (kleiner als die
	 * hier eingestellte)
	 * @return der Pufferinhalt im verkuerzten Intervall
	 */
	public final SortedSet<G> getPufferInhalt(long andereIntervallLaenge){
		SortedSet<G> pufferClone = new TreeSet<G>();
		synchronized (this) {
			if(!this.puffer.isEmpty()){
				G aktuellsterDatensatz = this.puffer.first();
				long aeltesterErlaubterZeitStempel = aktuellsterDatensatz.getZeitStempel() - andereIntervallLaenge;
				
				for(G pufferElement:this.puffer){
					pufferClone.add(pufferElement);
					if(pufferElement.getZeitStempel() < aeltesterErlaubterZeitStempel){
						break;
					}
				}
			}		 
		}
		
		return pufferClone;
	}
	
	
	/**
	 * Erfragt den Pufferinhalt
	 * 
	 * @return der Pufferinhalt
	 */
	public final SortedSet<G> getPufferInhalt(){
		return this.puffer;
	}
	
	
	/**
	 * Erfragt die aktelle Maximallaenge des Pufferintervalls
	 * 
	 * @return aktelle Maximallaenge des Pufferintervalls
	 */
	public final long getIntervallLaenge(){
		return this.intervallLaenge;
	}
	
	
	/**
	 * Erfragt den Pufferinhalt als Kopie
	 * 
	 * @return der Pufferinhalt als Kopie
	 */
	public final SortedSet<G> clonePufferInhalt(){
		SortedSet<G> pufferClone = new TreeSet<G>();
		synchronized (this) {
			pufferClone.addAll(this.puffer);			
		}
		return pufferClone;
	}
	
	
	/**
	 * Loescht alle Elemente aus dem Puffer, die nicht mehr im Intervall liegen
	 */
	private final synchronized void aufraeumen(){
		if(!this.puffer.isEmpty()){
			G aktuellsterDatensatz = this.puffer.first();
			long aeltesterErlaubterZeitStempel = aktuellsterDatensatz.getZeitStempel() - this.intervallLaenge;
			
			Collection<G> zuLoeschendeElemente = new ArrayList<G>();
			for(G pufferElement:this.puffer){
				if(pufferElement.getZeitStempel() < aeltesterErlaubterZeitStempel){
					zuLoeschendeElemente.add(pufferElement);
				}
			}
			
			this.puffer.removeAll(zuLoeschendeElemente);
		}		 
	}
	
}
