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

package de.bsvrz.dua.pllangufd.parameter;

import java.util.Objects;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.sys.funclib.bitctrl.dua.AllgemeinerDatenContainer;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorWert;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.StundenIntervallAnteil12h;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;

/**
 * Wrapper-Klasse fuer die Daten aller Parameter-Attributgruppen
 * <code>atg.ufdsLangzeitPLPrüfungXXX</code>.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class UfdsLangZeitPlPruefungsParameter {

	/**
	 * Maximal zulässige Ausfallzeitdauer. Ist die Summer der Ausfallzeiten für
	 * diesen Sensor, bei dem keine Daten im Vergleichszeitraum vorlagen, größer
	 * als msxAusfallZeit, so wird für dieses Intervall keine PL-Langzeitprüfung
	 * durchgeführt.
	 */
	private final long maxAusfallZeit;

	/**
	 * Vergleichsintervall, für das die Langzeit-PL-Prüfung durchgeführt wird.
	 */
	private final StundenIntervallAnteil12h vergleichsIntervall;

	/**
	 * Maximal zulässige Abweichung der Werte des Sensors im Vergleich zu den
	 * Nachbarsensoren über das Vergleichsintervall.
	 */
	private final UmfeldDatenSensorWert maxAbweichung;

	/**
	 * Maximal zulässige (zeitliche) Abweichung der Werte des Sensors im
	 * Vergleich zu den Nachbarsensoren über das Vergleichsintervall.
	 */
	private final long maxAbweichungZeit;

	/**
	 * Standardkonstruktor.
	 * 
	 * @param resultat
	 *            ein Parameter-Datensart der Attributgruppe
	 *            <code>atg.ufdsLangzeitPLPrüfungXXX</code>
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 */
	public UfdsLangZeitPlPruefungsParameter(final ResultData resultat)
			throws UmfeldDatenSensorUnbekannteDatenartException {
		if (resultat == null || resultat.getData() == null) {
			vergleichsIntervall = null;
			maxAusfallZeit = Long.MIN_VALUE;
			maxAbweichungZeit = Long.MIN_VALUE;
			maxAbweichung = null;
		} else {
			final UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(resultat.getObject());

			final String attMaxAbweichungName = "maxAbweichung" + datenArt.getAbkuerzung(); //$NON-NLS-1$
			final Data datum = resultat.getData();

			this.vergleichsIntervall = StundenIntervallAnteil12h
					.getZustand(datum.getUnscaledValue("VergleichsIntervall").intValue()); //$NON-NLS-1$
			this.maxAusfallZeit = datum.getTimeValue("maxAusfallZeit").getMillis(); //$NON-NLS-1$

			if (datenArt.equals(UmfeldDatenArt.ns) || datenArt.equals(UmfeldDatenArt.fbz)) {
				this.maxAbweichungZeit = datum.getTimeValue(attMaxAbweichungName).getMillis();
				this.maxAbweichung = null;
			} else {
				this.maxAbweichung = new UmfeldDatenSensorWert(datenArt);
				this.maxAbweichung.setWert(datum.getUnscaledValue(attMaxAbweichungName).longValue());
				maxAbweichungZeit = Long.MIN_VALUE;
			}
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

	@Override
	public int hashCode() {
		return Objects.hash(maxAbweichung, maxAbweichungZeit, maxAusfallZeit, vergleichsIntervall);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UfdsLangZeitPlPruefungsParameter other = (UfdsLangZeitPlPruefungsParameter) obj;
		if (maxAbweichung == null) {
			if (other.maxAbweichung != null)
				return false;
		} else if (!maxAbweichung.equals(other.maxAbweichung))
			return false;
		if (maxAbweichungZeit != other.maxAbweichungZeit)
			return false;
		if (maxAusfallZeit != other.maxAusfallZeit)
			return false;
		if (vergleichsIntervall == null) {
			if (other.vergleichsIntervall != null)
				return false;
		} else if (!vergleichsIntervall.equals(other.vergleichsIntervall))
			return false;
		return true;
	}

}
