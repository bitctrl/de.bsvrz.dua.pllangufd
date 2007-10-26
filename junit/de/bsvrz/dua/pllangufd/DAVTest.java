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

import java.util.Random;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Stellt eine Datenverteiler-Verbindung
 * zur Verfügung.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class DAVTest {

	/**
	 * Verbindungsdaten
	 */
	private static final String[] CON_DATA = new String[] {
			"-datenverteiler=localhost:8083", //$NON-NLS-1$ 
			"-benutzer=Tester", //$NON-NLS-1$
			"-authentifizierung=c:\\passwd1", //$NON-NLS-1$
			"-debugLevelStdErrText=CONFIG", //$NON-NLS-1$
			"-debugLevelFileText=CONFIG" }; //$NON-NLS-1$

	/**
	 * Verbindung zum Datenverteiler
	 */
	protected static ClientDavInterface VERBINDUNG = null;

	/**
	 * Randomizer
	 */
	public static Random R = new Random(System.currentTimeMillis());

	
	/**
	 * Erfragt bzw. initialisiert eine
	 * Datenverteiler-Verbindung
	 * 
	 * @return die Datenverteiler-Verbindung
	 * @throws Exception falls die Verbindung nicht
	 * hergestellt werden konnte
	 */
	public static final ClientDavInterface getDav()
	throws Exception {
		
		if(VERBINDUNG == null) {
			StandardApplicationRunner.run(new StandardApplication() {
	
				public void initialize(ClientDavInterface connection)
						throws Exception {
					DAVTest.VERBINDUNG = connection;
					UmfeldDatenArt.initialisiere(VERBINDUNG);
					UfdSensorSender.init(VERBINDUNG);
				}
	
				public void parseArguments(ArgumentList argumentList)
						throws Exception {
					//
				}
	
			}, CON_DATA);			
		}
				
		return VERBINDUNG;
	}

}
