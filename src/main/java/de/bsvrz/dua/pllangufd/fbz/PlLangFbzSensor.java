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


package de.bsvrz.dua.pllangufd.fbz;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.AbstraktEreignis;
import de.bsvrz.dua.pllangufd.AbstraktPlLangEreignisSensor;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;

/**
 * Sensor, der die aktuellen Daten eines FBZ-Sensors zu Vergleichswerten im
 * Sinne der Pl-Pruefung langzeit UFD verarbeitet.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class PlLangFbzSensor extends AbstraktPlLangEreignisSensor {

	/**
	 * statische Instanzen dieser Klasse.
	 */
	private static final Map<SystemObject, PlLangFbzSensor> INSTANZEN = new HashMap<>();

	/**
	 * Erfragt eine statische Instanz dieser Klasse.
	 * 
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            ein Systemobjekt eines Umfelddatensensors (<code>!= null</code>)
	 * @return eine statische Instanz dieser Klasse
	 * @throws UmfeldDatenSensorUnbekannteDatenartException 
	 */
	public static final PlLangFbzSensor getInstanz(
			final ClientDavInterface dav, final SystemObject objekt) throws UmfeldDatenSensorUnbekannteDatenartException {
		if (objekt == null) {
			throw new NullPointerException("Sensor-Objekt ist <<null>>"); //$NON-NLS-1$
		}
		PlLangFbzSensor instanz = INSTANZEN.get(objekt);

		if (instanz == null) {
			instanz = new PlLangFbzSensor();
			instanz.initialisiere(dav, objekt, dav.getDataModel().getAspect(
					DUAKonstanten.ASP_MESSWERTERSETZUNG));
			INSTANZEN.put(objekt, instanz);
		}

		return instanz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<? extends AbstraktEreignis> getEreignisInstanzen() {
		return FahrBahnZustandsEreignis.getInstanzen();
	}

}
