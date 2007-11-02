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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.app.Pause;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Sendet Daten von Umfelddatensensoren
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class UfdSensorSender 
implements ClientSenderInterface{
	
	/**
	 * statische Instanzen dieser Klasse
	 */
	private static HashMap<SystemObject, UfdSensorSender> INSTANZEN = 
									new HashMap<SystemObject, UfdSensorSender>();
	
	/**
	 * Objekt
	 */
	private SystemObject objekt = null;
	
	
	/**
	 * Initialisiert alle Umfelddatensensoren
	 * 
	 * @param dav Datenverteiler-Verbindung
	 * @param ersteDatenZeit erste Datenzeit der Nutzdaten (wird bei der
	 * Initialisierung versendet)
	 * @throws OneSubscriptionPerSendData wird weitergereicht 
	 */
	public static final void initialisiere(ClientDavInterface dav, long ersteDatenZeit)
	throws Exception{
		for(SystemObject objekt:dav.getDataModel().getType("typ.umfeldDatenSensor").getElements()){ //$NON-NLS-1$
			INSTANZEN.put(objekt, new UfdSensorSender(dav, objekt));
		}
		Pause.warte(1000L);
		/**
		 * Warte bis alle Anmeldungenen durchgeführt sind
		 */
		for(UfdSensorSender sender:INSTANZEN.values()){
			UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(sender.getObjekt());
			DataDescription dd = new DataDescription(
					dav.getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
					dav.getDataModel().getAspect(DUAKonstanten.ASP_MESSWERTERSETZUNG),
					(short)0);
			Data nutzDaten = getSensorDatum(sender.getObjekt(), 15L * Konstante.MINUTE_IN_MS, 1);
			sender.sende(new ResultData(sender.getObjekt(), dd, ersteDatenZeit, nutzDaten));
		}
		
		for(UfdSensorSender sender:INSTANZEN.values()){
			UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(sender.getObjekt());
			DataDescription dd = new DataDescription(
					dav.getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
					dav.getDataModel().getAspect(DUAKonstanten.ASP_MESSWERTERSETZUNG),
					(short)0);
			Data nutzDaten = getSensorDatum(sender.getObjekt(), 15L * Konstante.MINUTE_IN_MS, 1);
			sender.sende(new ResultData(sender.getObjekt(), dd, ersteDatenZeit + 15L * Konstante.MINUTE_IN_MS, nutzDaten));
		}

		for(UfdSensorSender sender:INSTANZEN.values()){
			UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(sender.getObjekt());
			DataDescription dd = new DataDescription(
					dav.getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
					dav.getDataModel().getAspect(DUAKonstanten.ASP_MESSWERTERSETZUNG),
					(short)0);
			Data nutzDaten = getSensorDatum(sender.getObjekt(), 15L * Konstante.MINUTE_IN_MS, 1);
			sender.sende(new ResultData(sender.getObjekt(), dd, ersteDatenZeit + 30L * Konstante.MINUTE_IN_MS, nutzDaten));
		}

		for(UfdSensorSender sender:INSTANZEN.values()){
			UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(sender.getObjekt());
			DataDescription dd = new DataDescription(
					dav.getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
					dav.getDataModel().getAspect(DUAKonstanten.ASP_MESSWERTERSETZUNG),
					(short)0);
			Data nutzDaten = getSensorDatum(sender.getObjekt(), 15L * Konstante.MINUTE_IN_MS, 1);
			sender.sende(new ResultData(sender.getObjekt(), dd, ersteDatenZeit + 45L * Konstante.MINUTE_IN_MS, nutzDaten));
		}

		for(UfdSensorSender sender:INSTANZEN.values()){
			UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(sender.getObjekt());
			DataDescription dd = new DataDescription(
					dav.getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
					dav.getDataModel().getAspect(DUAKonstanten.ASP_MESSWERTERSETZUNG),
					(short)0);
			Data nutzDaten = getSensorDatum(sender.getObjekt(), 15L * Konstante.MINUTE_IN_MS, 1);
			sender.sende(new ResultData(sender.getObjekt(), dd, ersteDatenZeit + 60L * Konstante.MINUTE_IN_MS, nutzDaten));
		}

	}

	
	/**
	 * Erfragt alle statischen Instanzen dieser Klasse
	 * 
	 * @return eine statische Instanz dieser Klasse
	 */
	public static final Collection<UfdSensorSender> getInstanzen(){
		return INSTANZEN.values();
	}
	
	
	/**
	 * Erfragt eine statische Instanz dieser Klasse
	 * 
	 * @param obj Systemobjekt eines Umfelddatensensors
	 * @return eine statische Instanz dieser Klasse
	 */
	public static final UfdSensorSender getInstanz(SystemObject obj){
		return INSTANZEN.get(obj);
	}
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param dav Datenverteiler-Verbindung
	 * @param objekt Systemobjekt eines Umfelddatensensors
	 * @throws OneSubscriptionPerSendData wird weitergereicht
	 */
	private UfdSensorSender(ClientDavInterface dav, SystemObject objekt)
	throws OneSubscriptionPerSendData{
		this.objekt = objekt;
		UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(objekt);
		DataDescription dd = new DataDescription(
				dav.getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
				dav.getDataModel().getAspect(DUAKonstanten.ASP_MESSWERTERSETZUNG),
				(short)0);
		dav.subscribeSender(this, objekt, dd, SenderRole.source());
	}
	
	
	/**
	 * Sendet Daten
	 * 
	 * @param resultat ein UFD
	 */
	public void sende(ResultData resultat){
		try {			
			DAVTest.getDav().sendData(resultat);
			System.out.println(DUAKonstanten.ZEIT_FORMAT_GENAU.format(new Date(resultat.getDataTime())) + ", " + //$NON-NLS-1$ 
					resultat.getObject().getPid() + " (" +  //$NON-NLS-1$
					(resultat.getData().getTimeValue("T").getMillis() / 1000) + "s): " +  //$NON-NLS-1$ //$NON-NLS-2$
					resultat.getData().getItem(UmfeldDatenArt.getUmfeldDatenArtVon(resultat.getObject()).getName()).getUnscaledValue("Wert").longValue()); //$NON-NLS-1$
			Pause.warte(100);
		} catch (DataNotSubscribedException e) {
			e.printStackTrace();
		} catch (SendSubscriptionNotConfirmed e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Erzeugt einen Messwert mit der Datenbeschreibung <code>asp.messWertErsetzung</code>
	 * 
	 * @param intervall das Erfassungeintervall
	 * @param sensor ein Umfelddatensensor, für den ein Messwert erzeugt werden soll
	 * @param wert der zu setzende Sensor-Wert
	 * @return ein (ausgefüllter) Umfelddaten-Messwert der zum übergebenen Systemobjekt passt.
	 * Alle Pl-Prüfungs-Flags sind auf <code>NEIN</code> gesetzt. Der Daten-Intervall beträgt
	 * 1 min.
	 * @throws Exception wird weitergereicht
	 */
	public static final Data getSensorDatum(SystemObject sensor, long intervall, long wert)
	throws Exception{
		UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(sensor);		
		
		Data datum = DAVTest.getDav().createData(
				DAVTest.getDav().getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName())); //$NON-NLS-1$
		
		datum.getTimeValue("T").setMillis(intervall); //$NON-NLS-1$
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
	 * Erfragt das Objekt
	 * 
	 * @return das Objekt
	 */
	public final SystemObject getObjekt(){
		return this.objekt;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void dataRequest(SystemObject object,
			DataDescription dataDescription, byte state) {
		// 	
	}

	
	/**
	 * {@inheritDoc}
	 */
	public boolean isRequestSupported(SystemObject object,
			DataDescription dataDescription) {
		return false;
	}
	
}
