/*
 * Segment Datenübernahme und Aufbereitung (DUA), SWE PL-Prüfung Langzeit UFD
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
 * Copyright 2016 by Kappich Systemberatung Aachen
 * 
 * This file is part of de.bsvrz.dua.pllangufd.
 * 
 * de.bsvrz.dua.pllangufd is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * de.bsvrz.dua.pllangufd is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with de.bsvrz.dua.pllangufd.  If not, see <http://www.gnu.org/licenses/>.

 * Contact Information:
 * Kappich Systemberatung
 * Martin-Luther-Straße 14
 * 52062 Aachen, Germany
 * phone: +49 241 4090 436 
 * mail: <info@kappich.de>
 */


package de.bsvrz.dua.pllangufd.na;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.AbstraktEreignis;
import de.bsvrz.dua.pllangufd.AbstraktPlLangEreignisSensor;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;

import java.util.Set;

/**
 * Sensor, der die aktuellen Daten eines NS-Sensors zu Vergleichswerten im Sinne
 * der Pl-Pruefung langzeit UFD verarbeitet.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id: PlLangNsSensor.java 54549 2015-04-17 13:40:51Z gieseler $
 */
public class PlLangNsSensor extends AbstraktPlLangEreignisSensor {

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
	public static final PlLangNsSensor getInstanz(
			final ClientDavInterface dav, final SystemObject objekt) throws UmfeldDatenSensorUnbekannteDatenartException {
		if (objekt == null) {
			throw new NullPointerException("Sensor-Objekt ist <<null>>"); //$NON-NLS-1$
		}
		
		final PlLangNsSensor instanz = new PlLangNsSensor();
		instanz.initialisiere(dav, objekt, dav.getDataModel().getAspect(
				DUAKonstanten.ASP_MESSWERTERSETZUNG));

		return instanz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<? extends AbstraktEreignis> getEreignisInstanzen() {
		return NiederschlagsEreignis.getInstanzen();
	}

}
