/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.x
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
package de.bsvrz.dua.pllangufd.bm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;

/**
 * Empfaengt Betriebsmeldungen
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class BmClient
implements ClientReceiverInterface{

	/**
	 * statische Instanz dieser Klasse
	 */
	private static BmClient INSTANZ = null;
	
	/**
	 * Beobachter
	 */
	private Set<IBmListener> listeners = Collections.synchronizedSet(new HashSet<IBmListener>());
	
	
	/**
	 * Erfragt die statische Instanz dieser Klasse
	 * 
	 * @param dav Datenverteiler-Verbindung
	 * @return die statische Instanz dieser Klasse
	 */
	public static final BmClient getInstanz(final ClientDavInterface dav){		
		if(INSTANZ == null){
			INSTANZ = new BmClient(dav);
		}
		return INSTANZ;
	}
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param dav Datenverteiler-Verbindung
	 */
	private BmClient(final ClientDavInterface dav){
		DataDescription datenBeschreibung = new DataDescription(
				dav.getDataModel().getAttributeGroup("atg.betriebsMeldung"), //$NON-NLS-1$
				dav.getDataModel().getAspect("asp.information"), //$NON-NLS-1$
				(short)0);
		dav.subscribeReceiver(this, dav.getDataModel().getConfigurationAuthority(),
				datenBeschreibung, ReceiveOptions.normal(), ReceiverRole.receiver());
	}
	
	
	/**
	 * Fuegt dieser Klasse einen Listener hinzu
	 * 
	 * @param listener ein neuer Listener
	 */
	public void addListener(final IBmListener listener){
		synchronized (this.listeners) {
			this.listeners.add(listener);
		}
	}


	/**
	 * Loescht einen Listener 
	 * 
	 * @param listener ein alter Listener
	 */
	public void removeListener(final IBmListener listener){
		synchronized (this.listeners) {
			this.listeners.remove(listener);
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] results) {
		if(results != null){
			for(ResultData result:results){
				if(result != null && result.getData() != null){
					final long zeit = result.getDataTime();
					final String text = result.getData().getTextValue("MeldungsText").getText(); //$NON-NLS-1$
					synchronized (this.listeners) {
						for(IBmListener listener:this.listeners){
							listener.aktualisiere(zeit, text);
						}
					}
				}
			}
		}		
	}
	
}
