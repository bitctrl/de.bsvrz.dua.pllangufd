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

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dua.pllangufd.historie.HistorischerUfdsWert;

/**
 * Abstrakter Umfelddatensensor fuer die PL-Pruefung langzeit UFD
 * mit aktuellen Parametern fuer Ereignis-getriggerte Pruefung
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktPlLangEreignisSensor 
extends AbstraktPlLangSensor<VergleichsEreignisWerteMitAktuellemDatum>{

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void berechneOnlineWert(ResultData resultat) {
		this.onlineWert = new VergleichsEreignisWerteMitAktuellemDatum(resultat);

		HistorischerUfdsWert historischerWert = new HistorischerUfdsWert(resultat);
		this.hitorie24.addDatum(historischerWert);

		synchronized (this) {
			if(this.aktuelleParameter != null && this.aktuelleParameter.isValid()){
				/**
				 * berechne Vergleichswerte fuer parametriertes Vergleichsintervall
				 */
				SortedSet<HistorischerUfdsWert> historieVergleich = 
					this.hitorie24.getPufferInhalt(this.aktuelleParameter.getVergleichsIntervall().getMillis());
				
				if(!historieVergleich.isEmpty()){
					this.onlineWert.setVergleichsWerte(
							getVergleichsWerte(historieVergleich, getEreignisInstanzen()));
				}

				/**
				 * berechne Vergleichswerte fuer letzte 24h
				 */
				if(!this.hitorie24.getPufferInhalt().isEmpty()){
					this.onlineWert.setVergleichsWerte24(
							getVergleichsWerte(this.hitorie24.getPufferInhalt(),
									getEreignisInstanzen()));
				}
			}
		}
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

	
	/**
	 * Erfragt die Menge von Ereignissen, fuer die die Abweichung bzw. ueber denen
	 * die Vergleichswerte berechnet werden sollen
	 * 
	 * @return die Menge von Ereignissen, fuer die die Abweichung bzw. ueber denen
	 * die Vergleichswerte berechnet werden sollen
	 */
	protected abstract Set<? extends AbstraktEreignis> getEreignisInstanzen();
	
}