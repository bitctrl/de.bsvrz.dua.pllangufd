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

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.AbstraktOnlineUfdSensor;

/**
 * Allgemeiner Umfelddatensensor fuer die PL-Pruefung Langzeit UFD
 * mit aktuellen Werten.<br>
 * Hier wird aktuell immer auch der aktuelle Vergleichswert fuer die
 * PL-Pruefung Langzeit UFD zur Verfuegung gestellt
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PlLangUfdSensor
extends AbstraktOnlineUfdSensor<VergleichsWertMitAktuellemDatum>
implements IUniversalAtgUfdsLangzeitPLPruefungListener{

	/**
	 * statische Instanzen dieser Klasse
	 */
	private static final Map<SystemObject, PlLangUfdSensor> INSTANZEN = new HashMap<SystemObject, PlLangUfdSensor>();
	
	/**
	 * aktuelle Parameter fuer die Langzeitpruefung
	 */
	private UfdsLangZeitPlPruefungsParameter aktuelleParameter = null;
	
	/**
	 * Messwerthistorie dieses Sensors fuer die letzten 24 Stunden
	 */
	private UfdsHistorie hitorie24 = new UfdsHistorie();
	
	
	/**
	 * Erfragt eine statische Instanz dieser Klasse
	 * 
	 * @param dav Datenverteiler-Verbindung
	 * @param objekt ein Systemobjekt eines Umfelddatensensors (<code>!= null</code>)
	 * @return eine statische Instanz dieser Klasse
	 */
	public static final PlLangUfdSensor getInstanz(final ClientDavInterface dav, 
												final SystemObject objekt){
		if(objekt == null){
			throw new NullPointerException("Sensor-Objekt ist <<null>>"); //$NON-NLS-1$
		}
		PlLangUfdSensor instanz = INSTANZEN.get(objekt);
		
		if(instanz == null){
			instanz = new PlLangUfdSensor();
			instanz.initialisiere(dav, objekt);
			INSTANZEN.put(objekt, instanz);
		}
		
		return instanz;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialisiere(ClientDavInterface dav, SystemObject objekt) {
		super.initialisiere(dav, objekt);
		
		UniversalAtgUfdsLangzeitPLPruefung parameter = new UniversalAtgUfdsLangzeitPLPruefung(dav, objekt);
		parameter.addListener(this, true);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void berechneOnlineWert(ResultData resultat) {
		this.onlineWert = new VergleichsWertMitAktuellemDatum(resultat);
		
		if(resultat != null && resultat.getData() != null){
			HistorischerUfdsWert historischerWert = new HistorischerUfdsWert(resultat);
			this.hitorie24.addDatum(historischerWert);
			synchronized (this.aktuelleParameter) {
				if(this.aktuelleParameter.isValid()){
					this.onlineWert.setVergleichsWert( this.hitorie24.getVergleichsWert(
							this.aktuelleParameter.getMaxAusfallZeit()) );	
				}				
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereParameter(
			UfdsLangZeitPlPruefungsParameter aktuelleParameter) {
		this.aktuelleParameter = aktuelleParameter;
	}
	
}
