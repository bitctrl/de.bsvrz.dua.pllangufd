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

package de.bsvrz.dua.pllangufd.na;

import org.junit.Ignore;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.AbstraktNsFbzTest;
import de.bsvrz.dua.pllangufd.DAVTest;

/**
 * Testet auf Versenden von Betriebsmeldungen fuer NA-Sensoren.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
@Ignore("Testdatenverteiler prüfen")
public class NaTest extends AbstraktNsFbzTest {

	@Override
	protected SystemObject getNachfolger() throws Exception {
		return DAVTest.getDav().getDataModel().getObject("ufdSensor.nachfolger.ns"); //$NON-NLS-1$ ;
	}

	@Override
	protected SystemObject getPruefling() throws Exception {
		return DAVTest.getDav().getDataModel().getObject("ufdSensor.pruefling.ns"); //$NON-NLS-1$ ;
	}

	@Override
	protected SystemObject getVorgaenger() throws Exception {
		return DAVTest.getDav().getDataModel().getObject("ufdSensor.vorgaenger.ns"); //$NON-NLS-1$ ;
	}

}
