/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.13 Pl-Pruefung langzeit UFD
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

package de.bsvrz.dua.pllangufd.historie;

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorDatum;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorWert;

/**
 * Historischer Wert eines beliebigen Umfelddatensensors.<br>
 * <b>Achtung:</b> Instanzen dieses Typs sind nur nach ihrem Zeitstempel
 * sortierbar
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public class HistorischerUfdsWert extends HistPufferElement {

	/**
	 * ob dieser Wert als plausibel gekennzeichnet war.
	 */
	private boolean plausibel = false;

	/**
	 * Erfassungsintervalldauer.
	 */
	private long tT = Long.MIN_VALUE;

	/**
	 * der historische Wert an sich.
	 */
	private UmfeldDatenSensorWert wert = null;

	/**
	 * Standardkonstruktor.
	 * 
	 * @param resultat
	 *            ein Umfelddatenresultat
	 */
	public HistorischerUfdsWert(ResultData resultat) {
		super(resultat.getDataTime());

		if (resultat.getData() != null) {
			UmfeldDatenSensorDatum datum = new UmfeldDatenSensorDatum(resultat);
			this.plausibel = datum.getStatusMessWertErsetzungImplausibel() == DUAKonstanten.NEIN;
			this.wert = datum.getWert();
			this.tT = datum.getT();
		}
	}

	/**
	 * Erfragt die Erfassungsintervalldauer.
	 * 
	 * @return die Erfassungsintervalldauer
	 */
	public final long getT() {
		return this.tT;
	}

	/**
	 * Erfragt, ob dieser Wert als plausibel gekennzeichnet war.
	 * 
	 * @return ob dieser Wert als plausibel gekennzeichnet war
	 */
	public final boolean isPlausibel() {
		return this.plausibel;
	}

	/**
	 * Erfragt den historischen Wert an sich.
	 * 
	 * @return der historische Wert an sich
	 */
	public final UmfeldDatenSensorWert getWert() {
		return this.wert;
	}

}
