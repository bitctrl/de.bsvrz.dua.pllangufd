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

package de.bsvrz.dua.pllangufd;

import java.util.Random;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dua.pllangufd.vew.VerwaltungPlLangzeitUFD;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Stellt eine Datenverteiler-Verbindung zur Verfügung.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class DAVTest {

	/**
	 * Standardkonstruktor.
	 */
	private DAVTest() {
		
	}
	
	/**
	 * Verbindungsdaten.
	 */
	private static final String[] CON_DATA = new String[] {
			"-datenverteiler=localhost:8083", //$NON-NLS-1$ 
			"-benutzer=Tester", //$NON-NLS-1$
			"-authentifizierung=passwd", //$NON-NLS-1$
			"-debugLevelStdErrText=OFF", //$NON-NLS-1$
			"-debugLevelFileText=OFF" }; //$NON-NLS-1$

	/**
	 * Verbindung zum Datenverteiler.
	 */
	protected static ClientDavInterface dieVerbindung = null;

	/**
	 * Randomizer.
	 */
	public static Random r = new Random(System.currentTimeMillis());

	/**
	 * Erste Datenzeit der Testdaten.
	 */
	public static final long START_ZEIT = 0;

	/**
	 * Erfragt bzw. initialisiert eine Datenverteiler-Verbindung
	 * 
	 * @return die Datenverteiler-Verbindung
	 * @throws Exception
	 *             falls die Verbindung nicht hergestellt werden konnte
	 */
	public static ClientDavInterface getDav() throws Exception {

		if (dieVerbindung == null) {

			final String[] conDataApp = new String[CON_DATA.length + 1];
			int i = 0;
			for (String str : CON_DATA) {
				conDataApp[i++] = new String(str.getBytes());
			}
			conDataApp[i] = "-KonfigurationsBereichsPid=kb.plLangUfdTestModell"; //$NON-NLS-1$

			StandardApplicationRunner.run(new StandardApplication() {

				public void initialize(final ClientDavInterface connection)
						throws Exception {
					DAVTest.dieVerbindung = connection;
					UmfeldDatenArt.initialisiere(dieVerbindung);
					UfdSensorSender.initialisiere(dieVerbindung, START_ZEIT);
				}

				public void parseArguments(final ArgumentList argumentList)
						throws Exception {
					//
				}

			}, CON_DATA);
			StandardApplicationRunner.run(new VerwaltungPlLangzeitUFD(),
					conDataApp);
		}

		return dieVerbindung;
	}

}
