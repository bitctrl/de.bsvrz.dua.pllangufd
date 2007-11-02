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
package de.bsvrz.dua.pllangufd;

import java.util.Date;

import org.junit.Test;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.bm.BmClient;
import de.bsvrz.dua.pllangufd.bm.IBmListener;
import de.bsvrz.sys.funclib.bitctrl.app.Pause;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.StundenIntervallAnteil12h;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * TODO
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class AllgemeinTest 
implements IBmListener{

	@Test
	public void test1()
	throws Exception{
		ClientDavInterface dav = DAVTest.getDav();
		
		BmClient.getInstanz(dav).addListener(this);
				
		SystemObject prueflingNI =
			DAVTest.getDav().getDataModel().getObject("ufdSensor.pruefling.ni"); //$NON-NLS-1$
		SystemObject vorgaengerNI =
			DAVTest.getDav().getDataModel().getObject("ufdSensor.vorgaenger.ni"); //$NON-NLS-1$
		SystemObject nachfolgerNI =
			DAVTest.getDav().getDataModel().getObject("ufdSensor.nachfolger.ni"); //$NON-NLS-1$
		
		UfdSensorSender pSender = UfdSensorSender.getInstanz(prueflingNI);
		UfdSensorSender vSender = UfdSensorSender.getInstanz(vorgaengerNI);
		UfdSensorSender nSender = UfdSensorSender.getInstanz(nachfolgerNI);
		
		LzParameterSender.getInstanz(dav, prueflingNI).setParameter(StundenIntervallAnteil12h.STUNDEN_1,
																	Konstante.MINUTE_IN_MS, 10);
		
		long i30min = 30L * Konstante.MINUTE_IN_MS;
		long i15min = 15L * Konstante.MINUTE_IN_MS;
		
		long time = Konstante.STUNDE_IN_MS;
		
		Data pDat = null;
		Data vDat = null;
		Data nDat = null;

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		// erste Stunde

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 4);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 4);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		// 2. Stunde
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		// 3. Stunde
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 6);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 6);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));


		
		// 2. Stunde
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		// 3. Stunde
		
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 6);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
				time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 6);
		pSender.sende(new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
				time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
				time, nDat));

		for(int i = 0; i<100000; i++){
			Pause.warte(1000);
		}
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
	public void aktualisiere(long zeit, String text) {
		//System.out.println(DUAKonstanten.ZEIT_FORMAT_GENAU.format(new Date(zeit)) + ": " + text); //$NON-NLS-1$
		System.out.println(text);
	}
	
}
