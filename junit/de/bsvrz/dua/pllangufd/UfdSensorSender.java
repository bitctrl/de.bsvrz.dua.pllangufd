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
import java.util.HashMap;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
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
	 * @throws OneSubscriptionPerSendData wird weitergereicht 
	 */
	public static final void init(ClientDavInterface dav)
	throws OneSubscriptionPerSendData{
		for(SystemObject objekt:dav.getDataModel().getType("typ.umfeldDatenSensor").getElements()){ //$NON-NLS-1$
			INSTANZEN.put(objekt, new UfdSensorSender(dav, objekt));
		}
		Pause.warte(1000L);
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
		} catch (DataNotSubscribedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SendSubscriptionNotConfirmed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
