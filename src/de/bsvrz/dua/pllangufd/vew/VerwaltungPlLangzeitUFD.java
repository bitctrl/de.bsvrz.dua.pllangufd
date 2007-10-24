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
package de.bsvrz.dua.pllangufd.vew;

import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.fbz.PlLang_Fbz_SensorMenge;
import de.bsvrz.dua.pllangufd.na.PlLang_Ns_SensorMenge;
import de.bsvrz.dua.pllangufd.rest.PlLang_Ni_Wfd_Lt_Sw_SensorMenge;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.adapter.AbstraktVerwaltungsAdapter;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenMessStelle;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenSensor;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Das Modul Verwaltung ist die zentrale Steuereinheit der SWE PL-Prüfung Langzeit UFD.
 * Seine Aufgabe besteht in der Auswertung der Aufrufparameter, der Anmeldung beim
 * Datenverteiler und der entsprechenden Initialisierung aller Auswertungsmodule.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class VerwaltungPlLangzeitUFD
extends AbstraktVerwaltungsAdapter{

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialisiere()
	throws DUAInitialisierungsException {
		
		UmfeldDatenArt.initialisiere(this.verbindung);

		this.objekte = DUAUtensilien.getBasisInstanzen(
				this.verbindung.getDataModel().getType(DUAKonstanten.TYP_UFD_MESSSTELLE),
				this.verbindung, this.getKonfigurationsBereiche()).toArray(new SystemObject[0]);

		DUAUmfeldDatenMessStelle.initialisiere(this.verbindung, this.objekte);
		
		String infoStr = Konstante.LEERSTRING;
		for(SystemObject obj:this.objekte){
			infoStr += obj + "\n"; //$NON-NLS-1$
		}
		LOGGER.config("---\nBetrachtete Objekte:\n" + infoStr + "---\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		Set<UmfeldDatenArt> ni_wfd_sw_lt = new HashSet<UmfeldDatenArt>();
		ni_wfd_sw_lt.add(UmfeldDatenArt.NI);
		ni_wfd_sw_lt.add(UmfeldDatenArt.WFD);
		ni_wfd_sw_lt.add(UmfeldDatenArt.SW);
		ni_wfd_sw_lt.add(UmfeldDatenArt.LT);
		/**
		 * Instanziierung
		 */
		for(DUAUmfeldDatenMessStelle messStelle:DUAUmfeldDatenMessStelle.getInstanzen()){
			for(UmfeldDatenArt datenArt:ni_wfd_sw_lt){
				DUAUmfeldDatenSensor[] sensoren = this.getSensoren(messStelle, datenArt);
				if(sensoren[0] != null && sensoren[1] != null && sensoren[2] != null){
					new PlLang_Ni_Wfd_Lt_Sw_SensorMenge(this.verbindung, messStelle, sensoren[0], sensoren[1], sensoren[2]);
				}
			}
			
			DUAUmfeldDatenSensor[] sensorenNS = this.getSensoren(messStelle, UmfeldDatenArt.NS);
			if(sensorenNS[0] != null && sensorenNS[1] != null && sensorenNS[2] != null){
				new PlLang_Ns_SensorMenge(this.verbindung, messStelle, sensorenNS[0], sensorenNS[1], sensorenNS[2]);
			}

			DUAUmfeldDatenSensor[] sensorenFBZ = this.getSensoren(messStelle, UmfeldDatenArt.FBZ);
			if(sensorenFBZ[0] != null && sensorenFBZ[1] != null && sensorenFBZ[2] != null){
				new PlLang_Fbz_SensorMenge(this.verbindung, messStelle, sensorenFBZ[0], sensorenFBZ[1], sensorenFBZ[2]);
			}
		}
	}
	
	
	/**
	 * Erfragt eine Liste mit dem Vergleichssensor, dessen Vorgaenger und
	 * Nachfolger in Bezug auf eine bestimmte Messstelle und eine bestimmte 
	 * Datenart
	 * 
	 * @param messStelle eine Umfelddatenmessstelle
	 * @param datenArt eine Datenart
	 * @return eine Liste mit dem Vergleichssensor, dessen Vorgaenger und
	 * Nachfolger in Bezug auf eine bestimmte Messstelle und eine bestimmte 
	 * Datenart.<br>
	 * - [0] = Vergleichssensor oder <code>null</code>, wenn dieser nicht ermittelt
	 * werden konnte<br>
	 * - [1] = Vorgaengersensor oder <code>null</code>, wenn dieser nicht ermittelt
	 * werden konnte<br>
	 * - [2] = Nachfolgersensor oder <code>null</code>, wenn dieser nicht ermittelt
	 * werden konnte<br>
	 */
	private DUAUmfeldDatenSensor[] getSensoren(DUAUmfeldDatenMessStelle messStelle, UmfeldDatenArt datenArt){
		DUAUmfeldDatenSensor sensor = messStelle.getHauptSensor(datenArt);
		DUAUmfeldDatenSensor sensorVor = null;
		DUAUmfeldDatenSensor sensorNach = null;
		
		if(sensor != null){
			SystemObject vorgaengerObjekt = sensor.getVorgaenger();   
			if(vorgaengerObjekt != null){
				DUAUmfeldDatenMessStelle messStelleVorher = 
					DUAUmfeldDatenMessStelle.getInstanz(vorgaengerObjekt);
				if(messStelleVorher != null){
					sensorVor = messStelleVorher.getHauptSensor(datenArt);
				}
			}
			
			SystemObject nachfolgerObjekt = sensor.getNachfolger();   
			if(nachfolgerObjekt != null){
				DUAUmfeldDatenMessStelle messStelleNachher = 
					DUAUmfeldDatenMessStelle.getInstanz(nachfolgerObjekt);
				if(messStelleNachher != null){
					sensorNach = messStelleNachher.getHauptSensor(datenArt);
				}
			}
		}
		
		DUAUmfeldDatenSensor[] ergebnis = new DUAUmfeldDatenSensor[3];
		ergebnis[0] = sensor;
		ergebnis[1] = sensorVor;
		ergebnis[2] = sensorNach;
		
		return ergebnis;
	}

	
	/**
	 * Startet diese Applikation
	 * 
	 * @param argumente Argumente der Kommandozeile
	 */
	public static void main(String argumente[]){
        Thread.setDefaultUncaughtExceptionHandler(new Thread.
        				UncaughtExceptionHandler(){
            public void uncaughtException(@SuppressWarnings("unused")
			Thread t, Throwable e) {
                LOGGER.error("Applikation wird wegen" +  //$NON-NLS-1$
                		" unerwartetem Fehler beendet", e);  //$NON-NLS-1$
            	e.printStackTrace();
                Runtime.getRuntime().exit(0);
            }
        });
		StandardApplicationRunner.run(new VerwaltungPlLangzeitUFD(), argumente);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public SWETyp getSWETyp() {
		return SWETyp.SWE_PL_PRUEFUNG_LANGZEIT_UFD;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] results) {
		// Die Datenverarbeitung findet in den Submodulen statt		
	}

}
