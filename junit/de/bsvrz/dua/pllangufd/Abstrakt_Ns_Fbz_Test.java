/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.13 Pl-Pruefung langzeit UFD
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
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */
package de.bsvrz.dua.pllangufd;

import org.junit.Assert;
import org.junit.Test;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.bm.BmClient;
import de.bsvrz.dua.pllangufd.bm.IBmListener;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.StundenIntervallAnteil12h;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Abstrakter Test fuer die Pl-Pruefung langzeit UFD der UFD-Arten
 * NS und FBZ
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public abstract class Abstrakt_Ns_Fbz_Test
implements IBmListener{

	/**
	 * Zaehlt die Betriebsmeldungen pro Sensor-Art
	 */
	private static int BM_ZAEHLER = 0;
	

	/**
	 * Erfragt das Systemobjekt des Prueflings-Sensors
	 * 
	 * @return das Systemobjekt des Prueflings-Sensors
	 * @throws Exception wird weitergereicht
	 */
	protected abstract SystemObject getPruefling()
	throws Exception;
	
	/**
	 * Erfragt das Systemobjekt des Vorgaenger-Sensors
	 * 
	 * @return das Systemobjekt des Vorgaenger-Sensors
	 * @throws Exception wird weitergereicht
	 */
	protected abstract SystemObject getVorgaenger()
	throws Exception;
	
	/**
	 * Erfragt das Systemobjekt des Nachfolger-Sensors
	 * 
	 * @return das Systemobjekt des Nachfolger-Sensors
	 * @throws Exception wird weitergereicht
	 */
	protected abstract SystemObject getNachfolger()
	throws Exception;
	
	
	/**
	 * Erfragt die geordnete Menge von Betriebsmeldungen, die fuer diesen
	 * Test erwartet werden
	 * 
	 * @return die geordnete Menge von Betriebsmeldungen, die fuer diesen
	 * Test erwartet werden
	 * @throws Exception wird weitergereicht
	 */
	private String[] getBetriebsmeldungen()
	throws Exception{
		return new String[]{"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:22:30 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 03:00 - 01.01.1970 04:00(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:30:00 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 03:15 - 01.01.1970 04:15(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:30:00 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 03:30 - 01.01.1970 04:30(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:22:30 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 03:45 - 01.01.1970 04:45(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
		 		" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:22:30 (>00:15:00)" + //$NON-NLS-1$
		 		" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 05:00 - 01.01.1970 06:00(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:30:00 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 05:15 - 01.01.1970 06:15(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:30:00 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 05:30 - 01.01.1970 06:30(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:22:30 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 05:45 - 01.01.1970 06:45(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:22:30 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 06:30 - 01.01.1970 07:30(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:22:30 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 06:45 - 01.01.1970 07:45(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:22:30 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 07:00 - 01.01.1970 08:00(1 Stunde) ab.", //$NON-NLS-1$
		"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) weicht um 00:22:30 (>00:15:00)" + //$NON-NLS-1$
				" vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 08:30 - 01.01.1970 09:30(1 Stunde) ab.", //$NON-NLS-1$
		"Die Plausibilit�tspr�fung zur " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) konnte nicht durchgef�hrt werden, da keine ausreichende Datenbasis vorlag", //$NON-NLS-1$
		"Die Plausibilit�tspr�fung zur " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" f�r die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) konnte nicht durchgef�hrt werden, da keine ausreichende Datenbasis vorlag"}; //$NON-NLS-1$
	}
			
	
	/**
	 * Diese Methode sendet fuer den Sensor, dessen Vorgaenger und dessen Nachfolger
	 * in unterschiedlichen Intervallen Sensorwerte, die bestimmte vordefinierte Nachrichten
	 * provozieren sollen. Die Parameter des Sensors werden vorher automatisch eingestellt
	 * 
	 * @throws Exception wird weitergereicht
	 */
	@Test
	public void testAufLangzeitMessfehler()
	throws Exception{
		BM_ZAEHLER = 0;
		
		ClientDavInterface dav = DAVTest.getDav();
		
		BmClient.getInstanz(dav).addListener(this);
				
		SystemObject prueflingNI = this.getPruefling();
		SystemObject vorgaengerNI = this.getVorgaenger();
		SystemObject nachfolgerNI = this.getNachfolger();
		
		UfdSensorSender pSender = UfdSensorSender.getInstanz(prueflingNI);
		UfdSensorSender vSender = UfdSensorSender.getInstanz(vorgaengerNI);
		UfdSensorSender nSender = UfdSensorSender.getInstanz(nachfolgerNI);
		
		LzParameterSender.getInstanz(dav, prueflingNI).setParameter(StundenIntervallAnteil12h.STUNDEN_1,
																	30 * Konstante.MINUTE_IN_MS, 15L * Konstante.MINUTE_IN_MS);
		
		long i15min = 15L * Konstante.MINUTE_IN_MS;
		
		long time = Konstante.STUNDE_IN_MS;
		
		Data pDat = null;
		Data vDat = null;
		Data nDat = null;

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		// 2. Stunde
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 64);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 64);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));
		
		// 3. Stunde
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		// 4. Stunde
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 64);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 64);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));
	
		// 5. Stunde
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));
		
		// 6. Stunde
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 64);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 64);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		// 7. Stunde
				
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		// Ausfall

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, -1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));
		

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, -1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, -1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 0);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 0);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i15min, 0);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

	}
	
	
	/**
	 * Erfragt die Datenbeschreibung der Onlinedaten dieses Systemobjekts
	 * 
	 * @param objekt SystemObjekt
	 * @return die DatenBeschreibung
	 * @throws Exception wird weitergereicht 
	 */
	public static final DataDescription getDatenBeschreibung(SystemObject objekt)
	throws Exception{
		UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(objekt);
		
		DataDescription datenBeschreibung = new DataDescription(
				DAVTest.getDav().getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
				DAVTest.getDav().getDataModel().getAspect(DUAKonstanten.ASP_MESSWERTERSETZUNG),
				(short)0);
		
		return datenBeschreibung;
	}


	/**
	 * {@inheritDoc}
	 */
	public void aktualisiere(long zeit, String meldungsText){
		System.out.println(meldungsText);
		
		try {
			Assert.assertEquals("Falsche Betriebsmeldung empfangen", //$NON-NLS-1$
					this.getBetriebsmeldungen()[BM_ZAEHLER], meldungsText);
		
			BM_ZAEHLER++;
			
			if(BM_ZAEHLER == this.getBetriebsmeldungen().length){
				BmClient.getInstanz(DAVTest.getDav()).removeListener(this);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
