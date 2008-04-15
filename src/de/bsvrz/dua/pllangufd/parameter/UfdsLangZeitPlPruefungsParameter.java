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

package de.bsvrz.dua.pllangufd.parameter;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.sys.funclib.bitctrl.dua.AllgemeinerDatenContainer;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorWert;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.StundenIntervallAnteil12h;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;

/**
 * Wrapper-Klasse fuer die Daten aller Parameter-Attributgruppen
 * <code>atg.ufdsLangzeitPLPrüfungXXX</code>.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public class UfdsLangZeitPlPruefungsParameter extends AllgemeinerDatenContainer {

	/**
	 * Maximal zulässige Ausfallzeitdauer. Ist die Summer der Ausfallzeiten für
	 * diesen Sensor, bei dem keine Daten im Vergleichszeitraum vorlagen, größer
	 * als msxAusfallZeit, so wird für dieses Intervall keine PL-Langzeitprüfung
	 * durchgeführt.
	 */
	private long maxAusfallZeit = Long.MIN_VALUE;

	/**
	 * Vergleichsintervall, für das die Langzeit-PL-Prüfung durchgeführt wird.
	 */
	private StundenIntervallAnteil12h vergleichsIntervall = null;

	/**
	 * Maximal zulässige Abweichung der Werte des Sensors im Vergleich zu den
	 * Nachbarsensoren über das Vergleichsintervall.
	 */
	private UmfeldDatenSensorWert maxAbweichung = null;

	/**
	 * Maximal zulässige (zeitliche) Abweichung der Werte des Sensors im
	 * Vergleich zu den Nachbarsensoren über das Vergleichsintervall.
	 */
	private long maxAbweichungZeit = Long.MIN_VALUE;

	/**
	 * Standardkonstruktor.
	 * 
	 * @param resultat
	 *            ein Parameter-Datensart der Attributgruppe
	 *            <code>atg.ufdsLangzeitPLPrüfungXXX</code>
	 */
	public UfdsLangZeitPlPruefungsParameter(final ResultData resultat) {
		if (resultat == null || resultat.getData() == null) {
			vergleichsIntervall = null;
		} else {
			final UmfeldDatenArt datenArt = UmfeldDatenArt
					.getUmfeldDatenArtVon(resultat.getObject());

			final String attMaxAbweichungName = "maxAbweichung" + datenArt.getAbkuerzung(); //$NON-NLS-1$
			final Data datum = resultat.getData();

			this.vergleichsIntervall = StundenIntervallAnteil12h
					.getZustand(datum
							.getUnscaledValue("VergleichsIntervall").intValue()); //$NON-NLS-1$
			this.maxAusfallZeit = datum
					.getTimeValue("maxAusfallZeit").getMillis(); //$NON-NLS-1$

			if (datenArt.equals(UmfeldDatenArt.ns)
					|| datenArt.equals(UmfeldDatenArt.fbz)) {
				this.maxAbweichungZeit = datum.getTimeValue(
						attMaxAbweichungName).getMillis();
			} else {
				this.maxAbweichung = new UmfeldDatenSensorWert(datenArt);
				this.maxAbweichung.setWert(datum.getUnscaledValue(
						attMaxAbweichungName).longValue());
			}

			// this.maxAbweichung = new UmfeldDatenSensorWert(datenArt);
			// this.maxAbweichung.setWert(datum.getUnscaledValue(attMaxAbweichungName).longValue());

		}
	}

	/**
	 * Erfragt die maximal zulässige Ausfallzeitdauer.<br>
	 * Ist die Summer der Ausfallzeiten für diesen Sensor, bei dem keine Daten
	 * im Vergleichszeitraum vorlagen, größer als msxAusfallZeit, so wird für
	 * dieses Intervall keine PL-Langzeitprüfung durchgeführt.
	 * 
	 * @return die maximal zulässige Ausfallzeitdauer
	 */
	public final long getMaxAusfallZeit() {
		return this.maxAusfallZeit;
	}

	/**
	 * Erfragt das Vergleichsintervall, für das die Langzeit-PL-Prüfung
	 * durchgeführt wird.
	 * 
	 * @return das Vergleichsintervall, für das die Langzeit-PL-Prüfung
	 *         durchgeführt wird.
	 */
	public final StundenIntervallAnteil12h getVergleichsIntervall() {
		return this.vergleichsIntervall;
	}

	/**
	 * Erfragt die maximal zulässige Abweichung der Werte des Sensors im
	 * Vergleich zu den Nachbarsensoren über das Vergleichsintervall.
	 * 
	 * @return maximal zulässige Abweichung der Werte des Sensors im Vergleich
	 *         zu den Nachbarsensoren über das Vergleichsintervall
	 */
	public final UmfeldDatenSensorWert getMaxAbweichung() {
		return this.maxAbweichung;
	}

	/**
	 * Erfragt die maximal zulässige (zeitliche) Abweichung der Werte des
	 * Sensors im Vergleich zu den Nachbarsensoren über das Vergleichsintervall.
	 * 
	 * @return maximal zulässige (zeitliche) Abweichung der Werte des Sensors im
	 *         Vergleich zu den Nachbarsensoren über das Vergleichsintervall
	 *         (int ms)
	 */
	public final long getMaxAbweichungZeit() {
		return this.maxAbweichungZeit;
		// return 15L * Konstante.MINUTE_IN_MS;
	}

	/**
	 * Erfragt, ob dieses Objekt sinnvolle Daten enthaelt (bzw. ob es schon
	 * initialisiert wurde)
	 * 
	 * @return ob dieses Objekt sinnvolle Daten enthaelt
	 */
	public final boolean isValid() {
		return vergleichsIntervall != null;
	}

}
