/*
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.13 PL-Pruefung Langzeit UFD
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.fbz.PlLangFbzSensorMenge;
import de.bsvrz.dua.pllangufd.na.PlLangNsSensorMenge;
import de.bsvrz.dua.pllangufd.rest.PlLangNiWfdLtSwSensorMenge;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.adapter.AbstraktVerwaltungsAdapter;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenMessStelle;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.DUAUmfeldDatenSensor;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Das Modul Verwaltung ist die zentrale Steuereinheit der SWE PL-Prüfung
 * Langzeit UFD. Seine Aufgabe besteht in der Auswertung der Aufrufparameter,
 * der Anmeldung beim Datenverteiler und der entsprechenden Initialisierung
 * aller Auswertungsmodule.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class VerwaltungPlLangzeitUFD extends AbstraktVerwaltungsAdapter {

	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialisiere() throws DUAInitialisierungsException {

		UmfeldDatenArt.initialisiere(this.verbindung);

		this.objekte = DUAUtensilien.getBasisInstanzen(
				this.verbindung.getDataModel().getType(
						DUAKonstanten.TYP_UFD_MESSSTELLE), this.verbindung,
				this.getKonfigurationsBereiche()).toArray(new SystemObject[0]);

		DUAUmfeldDatenMessStelle.initialisiere(this.verbindung, this.objekte);

		String infoStr = Constants.EMPTY_STRING;
		for (SystemObject obj : this.objekte) {
			infoStr += obj + "\n"; //$NON-NLS-1$
		}
		LOGGER.config(
				"---\nBetrachtete Objekte:\n" + infoStr + "---\n"); //$NON-NLS-1$ //$NON-NLS-2$

		final Set<UmfeldDatenArt> niWfdSwLt = new HashSet<>();
		niWfdSwLt.add(UmfeldDatenArt.ni);
		niWfdSwLt.add(UmfeldDatenArt.wfd);
		niWfdSwLt.add(UmfeldDatenArt.sw);
		niWfdSwLt.add(UmfeldDatenArt.lt);
		/**
		 * Instanziierung
		 */
		for (DUAUmfeldDatenMessStelle messStelle : DUAUmfeldDatenMessStelle
				.getInstanzen()) {
			for (UmfeldDatenArt datenArt : niWfdSwLt) {
				final DUAUmfeldDatenSensor[] sensoren = this.getSensoren(messStelle,
						datenArt);
				if (sensoren[0] != null && sensoren[1] != null
						&& sensoren[2] != null) {
					final PlLangNiWfdLtSwSensorMenge sensorMenge = new PlLangNiWfdLtSwSensorMenge();
					try {
						sensorMenge.initialisiere(this.verbindung, messStelle,
								sensoren[0], sensoren[1], sensoren[2]);
					} catch (final UmfeldDatenSensorUnbekannteDatenartException e) {
						LOGGER.warning("Messstelle '" + messStelle + "': " + e.getMessage());
					}
				}
			}

			final DUAUmfeldDatenSensor[] sensorenNS = this.getSensoren(messStelle,
					UmfeldDatenArt.ns);
			if (sensorenNS[0] != null && sensorenNS[1] != null
					&& sensorenNS[2] != null) {
				final PlLangNsSensorMenge sensorMenge = new PlLangNsSensorMenge();
				try {
					sensorMenge.initialisiere(this.verbindung, messStelle,
							sensorenNS[0], sensorenNS[1], sensorenNS[2]);
				} catch (final UmfeldDatenSensorUnbekannteDatenartException e) {
					LOGGER.warning("Messstelle '" + messStelle + "': " + e.getMessage());
				}
			}

			final DUAUmfeldDatenSensor[] sensorenFBZ = this.getSensoren(messStelle,
					UmfeldDatenArt.fbz);
			if (sensorenFBZ[0] != null && sensorenFBZ[1] != null
					&& sensorenFBZ[2] != null) {
				final PlLangFbzSensorMenge sensorMenge = new PlLangFbzSensorMenge();
				try {
					sensorMenge.initialisiere(this.verbindung, messStelle,
							sensorenFBZ[0], sensorenFBZ[1], sensorenFBZ[2]);
				} catch (final UmfeldDatenSensorUnbekannteDatenartException e) {
					LOGGER.warning("Messstelle '" + messStelle + "': " + e.getMessage());
				}
			}
		}
	}

	/**
	 * Erfragt eine Liste mit dem Vergleichssensor, dessen Vorgaenger und
	 * Nachfolger in Bezug auf eine bestimmte Messstelle und eine bestimmte
	 * Datenart.
	 * 
	 * @param messStelle
	 *            eine Umfelddatenmessstelle
	 * @param datenArt
	 *            eine Datenart
	 * @return eine Liste mit dem Vergleichssensor, dessen Vorgaenger und
	 *         Nachfolger in Bezug auf eine bestimmte Messstelle und eine
	 *         bestimmte Datenart.<br> - [0] = Vergleichssensor oder
	 *         <code>null</code>, wenn dieser nicht ermittelt werden konnte<br> -
	 *         [1] = Vorgaengersensor oder <code>null</code>, wenn dieser
	 *         nicht ermittelt werden konnte<br> - [2] = Nachfolgersensor oder
	 *         <code>null</code>, wenn dieser nicht ermittelt werden konnte<br>
	 */
	private DUAUmfeldDatenSensor[] getSensoren(
			final DUAUmfeldDatenMessStelle messStelle, final UmfeldDatenArt datenArt) {
		final DUAUmfeldDatenSensor sensor = messStelle.getHauptSensor(datenArt);
		DUAUmfeldDatenSensor sensorVor = null;
		DUAUmfeldDatenSensor sensorNach = null;

		if (sensor != null) {
			final SystemObject vorgaengerObjekt = sensor.getVorgaenger();
			if (vorgaengerObjekt != null) {
				final DUAUmfeldDatenMessStelle messStelleVorher = DUAUmfeldDatenMessStelle
						.getInstanz(vorgaengerObjekt);
				if (messStelleVorher != null) {
					sensorVor = messStelleVorher.getHauptSensor(datenArt);
					if (sensorVor == null) {
						/**
						 * kein Hauptsensor: nehme ersten Nebensensor
						 */
						if (messStelleVorher.getNebenSensoren(datenArt).size() > 0) {
							sensorVor = messStelleVorher.getNebenSensoren(
									datenArt).iterator().next();
						}
					}
				}
			}

			final SystemObject nachfolgerObjekt = sensor.getNachfolger();
			if (nachfolgerObjekt != null) {
				final DUAUmfeldDatenMessStelle messStelleNachher = DUAUmfeldDatenMessStelle
						.getInstanz(nachfolgerObjekt);
				if (messStelleNachher != null) {
					sensorNach = messStelleNachher.getHauptSensor(datenArt);
					if (sensorNach == null) {
						/**
						 * kein Hauptsensor: nehme ersten Nebensensor
						 */
						if (messStelleNachher.getNebenSensoren(datenArt).size() > 0) {
							sensorNach = messStelleNachher.getNebenSensoren(
									datenArt).iterator().next();
						}
					}
				}
			}
		}

		final DUAUmfeldDatenSensor[] ergebnis = new DUAUmfeldDatenSensor[3];
		ergebnis[0] = sensor;
		ergebnis[1] = sensorVor;
		ergebnis[2] = sensorNach;

		return ergebnis;
	}

	/**
	 * Startet diese Applikation.
	 * 
	 * @param argumente
	 *            Argumente der Kommandozeile
	 */
	public static void main(final String[] argumente) {
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
	public void update(final ResultData[] results) {
		// Die Datenverarbeitung findet in den Submodulen statt
	}

}
