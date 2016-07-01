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

import org.junit.Assert;
import org.junit.Test;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.bm.BmClient;
import de.bsvrz.sys.funclib.bitctrl.dua.bm.IBmListener;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.StundenIntervallAnteil12h;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;

/**
 * Abstrakter Test fuer die Pl-Pruefung langzeit UFD der UFD-Arten NI, WFD, LT
 * und SW.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public abstract class AbstraktNiWfdLtSwTest implements IBmListener {

	/**
	 * Zaehlt die Betriebsmeldungen pro Sensor-Art.
	 */
	private static int bmZaehler = 0;

	/**
	 * Erfragt das Systemobjekt des Prueflings-Sensors.
	 *
	 * @return das Systemobjekt des Prueflings-Sensors
	 * @throws Exception
	 *             wird weitergereicht
	 */
	protected abstract SystemObject getPruefling() throws Exception;

	/**
	 * Erfragt das Systemobjekt des Vorgaenger-Sensors.
	 *
	 * @return das Systemobjekt des Vorgaenger-Sensors
	 * @throws Exception
	 *             wird weitergereicht
	 */
	protected abstract SystemObject getVorgaenger() throws Exception;

	/**
	 * Erfragt das Systemobjekt des Nachfolger-Sensors.
	 *
	 * @return das Systemobjekt des Nachfolger-Sensors
	 * @throws Exception
	 *             wird weitergereicht
	 */
	protected abstract SystemObject getNachfolger() throws Exception;

	/**
	 * Erfragt den maximalen Abstand des Vergleichswertes im Test.
	 *
	 * @return den maximalen Abstand des Vergleichswertes im Test
	 */
	protected abstract long getMaxAbweichung();

	/**
	 * Erfragt die geordnete Menge von Betriebsmeldungen, die fuer diesen Test
	 * erwartet werden.
	 *
	 * @return die geordnete Menge von Betriebsmeldungen, die fuer diesen Test
	 *         erwartet werden
	 * @throws Exception
	 *             wird weitergereicht
	 */
	private String[] getBetriebsmeldungen() throws Exception {
		return new String[] {
				"Der Wert " //$NON-NLS-1$
						+ UmfeldDatenArt.getUmfeldDatenArtVon(
								this.getPruefling()).toString()
						+
						" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling)"
				+ " weicht um 1.5 (>1.0) vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 03:00 - 01.01.1970 04:00 (1 Stunde) ab.", //$NON-NLS-1$
				"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$ "
																									//$NON-NLS-1$ +
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling)"
				+ " weicht um 1.5 (>1.0) vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 03:15 - 01.01.1970 04:15 (1 Stunde) ab.", //$NON-NLS-1$
				"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling)"
				+ " weicht um 1.5 (>1.0) vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 03:30 - 01.01.1970 04:30 (1 Stunde) ab.", //$NON-NLS-1$
				"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling)"
				+ " weicht um 1.25 (>1.0) vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 05:00 - 01.01.1970 06:00 (1 Stunde) ab.", //$NON-NLS-1$
				"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling)"
				+ " weicht um 1.25 (>1.0) vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 05:15 - 01.01.1970 06:15 (1 Stunde) ab.", //$NON-NLS-1$
				"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling)"
				+ " weicht um 2.5 (>1.0) vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 05:30 - 01.01.1970 06:30 (1 Stunde) ab.", //$NON-NLS-1$
				"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling)"
				+ " weicht um 1.25 (>1.0) vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 06:45 - 01.01.1970 07:45 (1 Stunde) ab.", //$NON-NLS-1$
				"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling)"
				+ " weicht um 1.67 (>1.0) vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 07:00 - 01.01.1970 08:00 (1 Stunde) ab.", //$NON-NLS-1$
				"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling)"
				+ " weicht um 2.5 (>1.0) vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 07:15 - 01.01.1970 08:15 (1 Stunde) ab.",
				"Der Wert " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling)"
				+ " weicht um 2.5 (>1.0) vom erwarteten Vergleichswert im Vergleichszeitbereich 01.01.1970 07:30 - 01.01.1970 08:30 (1 Stunde) ab.",
				"Die Plausibilitätsprüfung zur " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) konnte nicht durchgeführt werden,"
				+ " da ein Vergleichswert nicht bestimmt werden konnte.", //$NON-NLS-1$
				"Die Plausibilitätsprüfung zur " + UmfeldDatenArt.getUmfeldDatenArtVon(this.getPruefling()).toString() + //$NON-NLS-1$
				" für die Messstelle ufdMessSt.pruefling (ufdMessSt.pruefling) konnte nicht durchgeführt werden,"
				+ " da ein Vergleichswert nicht bestimmt werden konnte." }; //$NON-NLS-1$
	}

	/**
	 * Diese Methode sendet fuer den Sensor, dessen Vorgaenger und dessen
	 * Nachfolger in unterschiedlichen Intervallen Sensorwerte, die bestimmte
	 * vordefinierte Nachrichten provozieren sollen. Die Parameter des Sensors
	 * werden vorher automatisch eingestellt
	 *
	 * @throws Exception
	 *             wird weitergereicht
	 */
	@Test
	public void testAufLangzeitMessfehler() throws Exception {
		AbstraktNiWfdLtSwTest.bmZaehler = 0;

		final ClientDavInterface dav = DAVTest.getDav();

		BmClient.getInstanz(dav).addListener(this);

		final SystemObject prueflingNI = this.getPruefling();
		final SystemObject vorgaengerNI = this.getVorgaenger();
		final SystemObject nachfolgerNI = this.getNachfolger();

		final UfdSensorSender pSender = UfdSensorSender.getInstanz(prueflingNI);
		final UfdSensorSender vSender = UfdSensorSender.getInstanz(vorgaengerNI);
		final UfdSensorSender nSender = UfdSensorSender.getInstanz(nachfolgerNI);

		LzParameterSender.getInstanz(dav, prueflingNI).setParameter(StundenIntervallAnteil12h.STUNDEN_1,
				30 * Constants.MILLIS_PER_MINUTE, this.getMaxAbweichung());

		final long i30min = 30L * Constants.MILLIS_PER_MINUTE;
		final long i15min = 15L * Constants.MILLIS_PER_MINUTE;

		long time = Constants.MILLIS_PER_HOUR;

		Data pDat = null;
		Data vDat = null;
		Data nDat = null;

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		// erste Stunde

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 4);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 4);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		// 2. Stunde

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		// 3. Stunde

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 6);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 6);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		// 2. Stunde

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		// 3. Stunde

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i30min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 6);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		time += i15min;
		vDat = UfdSensorSender.getSensorDatum(vSender.getObjekt(), i15min, 1);
		vSender.sende(new ResultData(vSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(vSender.getObjekt()), time, vDat));
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 6);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		// letzter Test auf die Nachricht, dass ein Messwert nicht ermittelt
		// werden konnte. Fuer den Vorgaenger werden keine Daten mehr versendet

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));
		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		time += i15min;
		pDat = UfdSensorSender.getSensorDatum(pSender.getObjekt(), i15min, 1);
		pSender.sende(new ResultData(pSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(pSender.getObjekt()), time, pDat));

		nDat = UfdSensorSender.getSensorDatum(nSender.getObjekt(), i30min, 1);
		nSender.sende(new ResultData(nSender.getObjekt(),
				AbstraktNiWfdLtSwTest.getDatenBeschreibung(nSender.getObjekt()), time, nDat));

		DAVTest.getDav().disconnect(false, "Ok.");
	}

	/**
	 * Erfragt die Datenbeschreibung der Onlinedaten dieses Systemobjekts.
	 *
	 * @param objekt
	 *            SystemObjekt
	 * @return die DatenBeschreibung
	 * @throws Exception
	 *             wird weitergereicht
	 */
	public static final DataDescription getDatenBeschreibung(final SystemObject objekt) throws Exception {
		final UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(objekt);

		final DataDescription datenBeschreibung = new DataDescription(
				DAVTest.getDav().getDataModel().getAttributeGroup("atg.ufds" + datenArt.getName()), //$NON-NLS-1$
						DAVTest.getDav().getDataModel().getAspect(DUAKonstanten.ASP_MESSWERTERSETZUNG));

		return datenBeschreibung;
	}

	@Override
	public void aktualisiereBetriebsMeldungen(final SystemObject obj, final long zeit, final String meldungsText) {
		System.out.println(meldungsText);

		try {
			Assert.assertEquals("Falsche Betriebsmeldung empfangen", //$NON-NLS-1$
					this.getBetriebsmeldungen()[AbstraktNiWfdLtSwTest.bmZaehler], meldungsText);

			AbstraktNiWfdLtSwTest.bmZaehler++;

			if (AbstraktNiWfdLtSwTest.bmZaehler == this.getBetriebsmeldungen().length) {
				BmClient.getInstanz(DAVTest.getDav()).removeListener(this);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
