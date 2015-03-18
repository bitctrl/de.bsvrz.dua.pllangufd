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

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.historie.HistorischerDatenpuffer;
import de.bsvrz.dua.pllangufd.historie.HistorischerUfdsWert;
import de.bsvrz.dua.pllangufd.parameter.IUniversalAtgUfdsLangzeitPLPruefungListener;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.dua.pllangufd.parameter.UniversalAtgUfdsLangzeitPLPruefung;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell.AbstraktOnlineUfdSensor;

/**
 * Abstrakter Umfelddatensensor fuer die PL-Pruefung Langzeit UFD mit aktuellen
 * Parametern und den Online-Daten der letzten 24 Stunden.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @param <G> Sensor
 * 
 * @version $Id$
 */
public abstract class AbstraktPlLangSensor<G> extends
		AbstraktOnlineUfdSensor<ResultData> implements
		IUniversalAtgUfdsLangzeitPLPruefungListener {

	/**
	 * erste fuer diesen Umfelddatensensor empfangene Datenzeit.
	 */
	private long aktivSeit = Long.MIN_VALUE;

	/**
	 * aktuelle Parameter fuer die Langzeitpruefung.
	 */
	protected UfdsLangZeitPlPruefungsParameter aktuelleParameter = null;

	/**
	 * Messwerthistorie dieses Sensors fuer die letzten 24 Stunden.
	 */
	protected HistorischerDatenpuffer<HistorischerUfdsWert> hitorie24 = new HistorischerDatenpuffer<HistorischerUfdsWert>(
			Constants.MILLIS_PER_DAY);

	/**
	 * Erfragt den aktuellen Vergleichswert, auf Basis der bis jetzt
	 * (uebergebener Zeitstempel) eingetroffenen Daten.
	 * 
	 * @param parameter
	 *            aktuelle Pl-langzeit-Parameter des Sensor-Prueflings
	 * @param aktuellerZeitStempel
	 *            indiziert den Jetzt-Zeitpunkt
	 * @return aktueller Vergleichswert, auf Basis der bis jetzt (uebergebener
	 *         Zeitstempel) eingetroffenen Daten oder <code>null</code>, wenn
	 *         dieser nicht errechnet werden konnte (weil noch keine Daten bzw.
	 *         Parameter vorlagen)
	 */
	public abstract G getAktuellenVergleichsWert(
			final UfdsLangZeitPlPruefungsParameter parameter,
			final long aktuellerZeitStempel);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialisiere(ClientDavInterface dav, SystemObject objekt,
			Aspect aspekt) {
		super.initialisiere(dav, objekt, aspekt);
		UniversalAtgUfdsLangzeitPLPruefung parameter = new UniversalAtgUfdsLangzeitPLPruefung(
				dav, objekt);
		parameter.addListener(this, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void berechneOnlineWert(ResultData resultat) {
		if (this.aktivSeit == Long.MIN_VALUE) {
			this.aktivSeit = resultat.getDataTime();
		}
		this.onlineWert = resultat;
		HistorischerUfdsWert historischerWert = new HistorischerUfdsWert(
				resultat);
		this.hitorie24.addDatum(historischerWert);
	}

	/**
	 * Erfragt seit wann Daten fuer diesen Umfelddatensensor empfangen werden.
	 * 
	 * @return seit wann Daten fuer diesen Umfelddatensensor empfangen werden
	 */
	public final long getAktivSeit() {
		return this.aktivSeit;
	}

	/**
	 * Erfragt die aktuellen Parameter dieses Sensors.
	 * 
	 * @return die aktuellen Parameter dieses Sensors
	 */
	public final UfdsLangZeitPlPruefungsParameter getAktuelleParameter() {
		return this.aktuelleParameter;
	}

	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereParameter(
			UfdsLangZeitPlPruefungsParameter aktuelleParameter1) {
		this.aktuelleParameter = aktuelleParameter1;
	}

}
