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


package de.bsvrz.dua.pllangufd.fbz;

import java.util.Set;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.AbstraktEreignis;
import de.bsvrz.dua.pllangufd.AbstraktPlLangEreignisSensorMenge;
import de.bsvrz.dua.pllangufd.AbstraktPlLangSensor;
import de.bsvrz.dua.pllangufd.VergleichsEreignisWerte;

/**
 * Assoziator fuer eine Menge von FBZ-Sensoren der Art:<br>
 * Hauptsensor, Vorgaenger, Nachfolger,<br>
 * wobei der Hauptsensor im Sinne der Pl-Pruefung langzeit UFD ueberprueft wird.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public class PlLangFbzSensorMenge extends AbstraktPlLangEreignisSensorMenge {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<? extends AbstraktEreignis> getEreignisInstanzen() {
		return FahrBahnZustandsEreignis.getInstanzen();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstraktPlLangSensor<VergleichsEreignisWerte> getSensorInstanz(
			SystemObject objekt) {
		return PlLangFbzSensor.getInstanz(derDav, objekt);
	}

}
