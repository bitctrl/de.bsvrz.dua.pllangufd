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

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ResultData;
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
 * mit aktuellen Parametern und den Online-Daten der letzten 24 Stunden
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktPlLangSensor<G>
extends AbstraktOnlineUfdSensor<ResultData>
implements IUniversalAtgUfdsLangzeitPLPruefungListener{
	
	/**
	 * aktuelle Parameter fuer die Langzeitpruefung
	 */
	protected UfdsLangZeitPlPruefungsParameter aktuelleParameter = null;
	
	/**
	 * Messwerthistorie dieses Sensors fuer die letzten 24 Stunden
	 */
	protected HistorischerDatenpuffer<HistorischerUfdsWert> hitorie24 = 
							new HistorischerDatenpuffer<HistorischerUfdsWert>(Konstante.TAG_24_IN_MS);
		

	/**
	 * Erfragt den aktuellen Vergleichswert, auf Basis der bis jetzt 
	 * (uebergebener Zeitstempel) eingetroffenen Daten
	 * 
	 * @param aktuellerZeitStempel indiziert den Jetzt-Zeitpunkt
	 * @return aktueller Vergleichswert, auf Basis der bis jetzt 
	 * (uebergebener Zeitstempel) eingetroffenen Daten oder <code>null</code>,
	 * wenn dieser nicht errechnet werden konnte (weil noch keine Daten
	 * bzw. Parameter vorlagen)
	 */
	public abstract G getAktuellenVergleichsWert(final long aktuellerZeitStempel);
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialisiere(ClientDavInterface dav,
								 SystemObject objekt,
								 Aspect aspekt) {
		super.initialisiere(dav, objekt, aspekt);
		UniversalAtgUfdsLangzeitPLPruefung parameter = new UniversalAtgUfdsLangzeitPLPruefung(dav, objekt);
		parameter.addListener(this, true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void berechneOnlineWert(ResultData resultat) {
		this.onlineWert = resultat;
		HistorischerUfdsWert historischerWert = new HistorischerUfdsWert(resultat);
		this.hitorie24.addDatum(historischerWert);
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
		
}
