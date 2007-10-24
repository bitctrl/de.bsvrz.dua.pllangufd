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
import java.util.Set;
import java.util.SortedSet;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.historie.HistorischerDatenpuffer;
import de.bsvrz.dua.pllangufd.historie.HistorischerUfdsWert;
import de.bsvrz.dua.pllangufd.parameter.IUniversalAtgUfdsLangzeitPLPruefungListener;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.dua.pllangufd.parameter.UniversalAtgUfdsLangzeitPLPruefung;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.AbstraktOnlineUfdSensor;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Abstrakter Umfelddatensensor fuer die PL-Pruefung Langzeit UFD
 * mit aktuellen Parametern
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktPlLangSensor<G>
extends AbstraktOnlineUfdSensor<G>
implements IUniversalAtgUfdsLangzeitPLPruefungListener{
	
	/**
	 * aktuelle Parameter fuer die Langzeitpruefung
	 */
	protected UfdsLangZeitPlPruefungsParameter aktuelleParameter = null;
	
	/**
	 * Messwerthistorie dieses Sensors fuer die letzten 24 Stunden
	 */
	protected HistorischerDatenpuffer<HistorischerUfdsWert> hitorie24 = new HistorischerDatenpuffer<HistorischerUfdsWert>();
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialisiere(ClientDavInterface dav, SystemObject objekt, Aspect aspekt) {
		super.initialisiere(dav, objekt, aspekt);
		
		this.hitorie24.setIntervallLaenge(Konstante.TAG_24_IN_MS);
		UniversalAtgUfdsLangzeitPLPruefung parameter = new UniversalAtgUfdsLangzeitPLPruefung(dav, objekt);
		parameter.addListener(this, true);
	}

	
	/**
	 * Erfragt die aktuellen Parameter dieses Sensors
	 * 
	 * @return die aktuellen Parameter dieses Sensors
	 */
	public final UfdsLangZeitPlPruefungsParameter getAktuelleParameter(){
		return this.aktuelleParameter;
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereParameter(
			UfdsLangZeitPlPruefungsParameter aktuelleParameter) {
		this.aktuelleParameter = aktuelleParameter;	
	}
	

	/**
	 * Erfragt eine Map mit den Vergleichswerten, die in Bezug auf die 
	 * uebergebenen historischen Werte pro Ereignis (der uebergebenen 
	 * Ereignisse) ausgerechnet werden koennen
	 * 
	 * @param historischeWerte historische Umfelddatenwerte eines bestimmten
	 * Bezugszeitraums
	 * @param ereignisse alle Ereignisse, denen die Zustaende innerhalb der
	 * uebergebenen historischen Werte zugeordnet werden sollen 
	 * @return eine Menge von (Ereignis, Vergleichswert)-Paaren. Diese Liste
	 * ist ggf. leer, wenn fuer keines der uebergebenen Ereignisse ein
	 * Vergleichswert errechnet werden konnte
	 */
	protected Map<AbstraktEreignis, Double> getVergleichsWerte(
												SortedSet<HistorischerUfdsWert> historischeWerte,
												Set<? extends AbstraktEreignis> ereignisse){
		Map<AbstraktEreignis, Double> ergebnisse = new HashMap<AbstraktEreignis, Double>();
		
		for(HistorischerUfdsWert historischerWert:historischeWerte){
			if(historischerWert.getWert().isOk()){
				for(AbstraktEreignis ereignis:ereignisse){
					if(ereignis.isZustandInEreignis((int)historischerWert.getWert().getWert())){
						Double ergebnisWert = ergebnisse.get(ereignis);
						if(ergebnisWert == null){
							ergebnisse.put(ereignis, new Double(historischerWert.getT()));
						}else{
							ergebnisse.put(ereignis, ergebnisWert + new Double(historischerWert.getT()));
						}
						break;						
					}
				}
			}
		}
		
		return ergebnisse;
	}
	
}
