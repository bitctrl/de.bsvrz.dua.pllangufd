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

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.sys.funclib.bitctrl.dua.AllgemeinerDatenContainer;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorDatum;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorWert;

/**
 * Historischer Wert eines beliebigen Umfelddatensensors.<br>
 * <b>Achtung:</b> Instanzen dieses Typs sind nur nach ihrem Zeitstempel sortierbar
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class HistorischerUfdsWert 
extends AllgemeinerDatenContainer
implements IZeitStempel{
	
	/**
	 * ob dieser Wert als plausibel gekennzeichnet war
	 */
	private boolean plausibel = false;

	/**
	 * Erfassungsintervalldauer
	 */
	private long T = Long.MIN_VALUE;
	
	/**
	 * der historische Wert an sich
	 */
	private UmfeldDatenSensorWert wert = null;
	
	/**
	 * der Zeitstempel dieses Wertes
	 */
	private long zeitStempel = Long.MIN_VALUE;


	
	/**
	 * Standardkonstruktor
	 * 
	 * @param resultat ein Umfelddatenresultat <b>! mit Nutzdaten !</b>
	 */
	public HistorischerUfdsWert(ResultData resultat){
		if(resultat == null || resultat.getData() == null){
			throw new NullPointerException("Keine Daten"); //$NON-NLS-1$
		}
		
		UmfeldDatenSensorDatum datum = new UmfeldDatenSensorDatum(resultat);
		this.plausibel = datum.getStatusMessWertErsetzungImplausibel() == DUAKonstanten.NEIN;
		this.wert = datum.getWert();
		this.T = datum.getT();
		this.zeitStempel = datum.getDatenZeit();
	}
	
	
	/**
	 * Erfragt die Erfassungsintervalldauer
	 * 
	 * @return die Erfassungsintervalldauer
	 */
	public final long getT() {
		return this.T;
	}
	
	
	/**
	 * Erfragt, ob dieser Wert als plausibel gekennzeichnet war
	 * 
	 * @return ob dieser Wert als plausibel gekennzeichnet war
	 */
	public final boolean isPlausibel() {
		return this.plausibel;
	}


	/**
	 * Erfragt den historischen Wert an sich
	 * 
	 * @return der historische Wert an sich
	 */
	public final UmfeldDatenSensorWert getWert() {
		return this.wert;
	}


	/**
	 * {@inheritDoc}
	 */
	public final long getZeitStempel() {
		return this.zeitStempel;
	}


	/**
	 * {@inheritDoc}
	 */
	public int compareTo(IZeitStempel that) {
		return new Long(this.getZeitStempel()).compareTo(that.getZeitStempel());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		boolean ergebnis = false;
		
		if(obj != null && obj instanceof HistorischerUfdsWert){
			HistorischerUfdsWert that = (HistorischerUfdsWert)obj;
			ergebnis = this.zeitStempel == that.zeitStempel;
		}
		
		return ergebnis;
	}

}
