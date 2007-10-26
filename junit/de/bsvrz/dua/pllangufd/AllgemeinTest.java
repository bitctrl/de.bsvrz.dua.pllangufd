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

import org.junit.Test;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.app.Pause;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * TODO
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class AllgemeinTest {

	@Test
	public void test1()
	throws Exception{
		ClientDavInterface dav = DAVTest.getDav();
				
		SystemObject prueflingNI =
			DAVTest.getDav().getDataModel().getObject("ufdSensor.pruefling.ni"); //$NON-NLS-1$
		SystemObject vorgaengerNI =
			DAVTest.getDav().getDataModel().getObject("ufdSensor.vorgaenger.ni"); //$NON-NLS-1$
		SystemObject nachfolgerNI =
			DAVTest.getDav().getDataModel().getObject("ufdSensor.nachfolger.ni"); //$NON-NLS-1$
		
		UfdSensorSender pSender = UfdSensorSender.getInstanz(prueflingNI);
		UfdSensorSender vSender = UfdSensorSender.getInstanz(vorgaengerNI);
		UfdSensorSender nSender = UfdSensorSender.getInstanz(nachfolgerNI);		
		
		int FehlerQuote = 10;
		long time = 1000000000;
		for(int i = 0; i<1000; i++){
			Data pDat = null;
			Data vDat = null;
			Data nDat = null;

			if(DAVTest.R.nextInt(FehlerQuote) == 0){
				pDat = getSensorDatum(pSender.getObjekt(), -1);
				vDat = getSensorDatum(pSender.getObjekt(), -1);
				nDat = getSensorDatum(pSender.getObjekt(), -1);				
			}else{
				pDat = getSensorDatum(pSender.getObjekt(), DAVTest.R.nextInt(2000) - 3);
				vDat = getSensorDatum(pSender.getObjekt(), DAVTest.R.nextInt(2000) - 3);
				nDat = getSensorDatum(pSender.getObjekt(), DAVTest.R.nextInt(2000) - 3);
			}
			
			ResultData pRes = new ResultData(pSender.getObjekt(), getDatenBeschreibung(pSender.getObjekt()),
					time, pDat);
			ResultData vRes = new ResultData(pSender.getObjekt(), getDatenBeschreibung(vSender.getObjekt()),
					time, vDat);
			ResultData nRes = new ResultData(pSender.getObjekt(), getDatenBeschreibung(nSender.getObjekt()),
					time, nDat);
			
			pSender.sende(pRes);
			vSender.sende(vRes);
			nSender.sende(nRes);
			
			time += Konstante.MINUTE_IN_MS;
			
			Pause.warte(1000);			
		}
	}
	
	
	/**
	 * Erzeugt einen Messwert mit der Datenbeschreibung <code>asp.messWertErsetzung</code>
	 * 
	 * @param sensor ein Umfelddatensensor, für den ein Messwert erzeugt werden soll
	 * @param wert der zu setzende Sensor-Wert
	 * @return ein (ausgefüllter) Umfelddaten-Messwert der zum übergebenen Systemobjekt passt.
	 * Alle Pl-Prüfungs-Flags sind auf <code>NEIN</code> gesetzt. Der Daten-Intervall beträgt
	 * 1 min.
	 * @throws Exception wird weitergereicht
	 */
	public static final Data getSensorDatum(SystemObject sensor, long wert)
	throws Exception{
		UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(sensor);		
		
		Data datum = DAVTest.getDav().createData(
				DAVTest.getDav().getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName())); //$NON-NLS-1$
		
		datum.getTimeValue("T").setMillis(60L * 1000L); //$NON-NLS-1$
		datum.getItem(datenArt.getName()).getUnscaledValue("Wert").set(wert); //$NON-NLS-1$
		datum.getItem(datenArt.getName()).getItem("Status").getItem("Erfassung"). //$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("NichtErfasst").set(DUAKonstanten.NEIN); //$NON-NLS-1$
		datum.getItem(datenArt.getName()).getItem("Status").getItem("PlFormal"). //$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("WertMax").set(DUAKonstanten.NEIN); //$NON-NLS-1$
		datum.getItem(datenArt.getName()).getItem("Status").getItem("PlFormal"). //$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("WertMin").set(DUAKonstanten.NEIN); //$NON-NLS-1$

		datum.getItem(datenArt.getName()).getItem("Status").getItem("MessWertErsetzung"). //$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("Implausibel").set(DUAKonstanten.NEIN); //$NON-NLS-1$
		datum.getItem(datenArt.getName()).getItem("Status").getItem("MessWertErsetzung"). //$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("Interpoliert").set(DUAKonstanten.NEIN); //$NON-NLS-1$

		datum.getItem(datenArt.getName()).getItem("Güte").getUnscaledValue("Index").set(10000); //$NON-NLS-1$ //$NON-NLS-2$
		datum.getItem(datenArt.getName()).getItem("Güte").getUnscaledValue("Verfahren").set(0); //$NON-NLS-1$ //$NON-NLS-2$
		
		return datum;
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
	
}
