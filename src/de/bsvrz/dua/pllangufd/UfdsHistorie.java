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


/**
 * Stellt den Vergleichswert fuer die Pl-Pruefung langzeit auf Basis der
 * in diesem Puffer gespeicherten Daten zur Verfuegung wie in Afo-4.0
 * 6.6.2.4.7.6, S.108 beschrieben
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class UfdsHistorie 
extends HistorischerDatenpuffer<HistorischerUfdsWert>{

	/**
	 * Erfragt den Vergleichswert fuer die Pl-Preufung langzeit auf Basis der
	 * in diesem Puffer gespeicherten Daten
	 *  
	 * @param maxAusfall der maximale Ausfall im Bezugszeitraum
	 * @return Vergleichswert fuer die Pl-Preufung langzeit auf Basis der
	 * in diesem Puffer gespeicherten Daten oder <code>Double.NaN</code>, wenn
	 * der Vergleichswert nicht berechnet werden konnte 
	 */
	public final double getVergleichsWert(final long maxAusfall){
		double ergebnis = Double.NaN;

		if(this.getIntervallLaenge() > 0){
			if(!this.getPufferInhalt().isEmpty()){	
				long summeD = -1;
				long summeWmalD = -1;
				long summeA = -1;
				for(HistorischerUfdsWert wert:this.getPufferInhalt()){
					if(wert.getWert().isOk()){
						if(summeD == -1){
							summeD = 0;	// Initialisierung
							summeWmalD = 0;	// Initialisierung
						}
						summeD += wert.getT();
						summeWmalD += wert.getWert().getWert() * wert.getT(); 
					}
				}
				
				if(summeD >= 0){
					summeA = this.getIntervallLaenge() - summeD;
					if(summeA < 0){
						summeA = 0;
						
						/**
						 * TODO: wieder raus
						 */
						throw new RuntimeException("---------------------------"); //$NON-NLS-1$
					}
					
					double anteilAusfall = ((double)summeA) / ((double)this.getIntervallLaenge());
					
					if(anteilAusfall <= (double)anteilAusfall){
						ergebnis = ((double)summeWmalD)/((double)summeD);
					}
				}
			}
		}		
		
		return ergebnis;		
	}
	
}
