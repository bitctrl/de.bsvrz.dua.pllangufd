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

package de.bsvrz.dua.pllangufd;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.pllangufd.historie.HistorischerDatenpuffer;
import de.bsvrz.dua.pllangufd.historie.HistorischerUfdsWert;
import de.bsvrz.dua.pllangufd.parameter.IUniversalAtgUfdsLangzeitPLPruefungListener;
import de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter;
import de.bsvrz.dua.pllangufd.parameter.UniversalAtgUfdsLangzeitPLPruefung;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
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

	public static final long MILLIS_PER_DAY = (long) (24 * 60 * 60 * 1000);
	/**
	 * erste fuer diesen Umfelddatensensor empfangene Datenzeit.
	 */
	private long aktivSeit = Long.MIN_VALUE;

	/**
	 * aktuelle Parameter fuer die Langzeitpruefung.
	 */
	protected UfdsLangZeitPlPruefungsParameter aktuelleParameter;

	/**
	 * Messwerthistorie dieses Sensors fuer die letzten 24 Stunden.
	 */
	protected HistorischerDatenpuffer<HistorischerUfdsWert> historie24 = new HistorischerDatenpuffer<HistorischerUfdsWert>(
			(long) (24 * 60 * 60 * 1000));

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
	 * @throws UmfeldDatenSensorUnbekannteDatenartException 
	 */
	@Override
	protected void initialisiere(final ClientDavInterface dav, final SystemObject objekt,
			final Aspect aspekt) throws UmfeldDatenSensorUnbekannteDatenartException {
		super.initialisiere(dav, objekt, aspekt);
		final UniversalAtgUfdsLangzeitPLPruefung parameter = new UniversalAtgUfdsLangzeitPLPruefung(
				dav, objekt);
		parameter.addListener(this, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void berechneOnlineWert(final ResultData resultat) {
		if(resultat.hasData()) {
			if (this.aktivSeit == Long.MIN_VALUE) {
				this.aktivSeit = resultat.getDataTime();
			}
			this.onlineWert = resultat;
			final HistorischerUfdsWert historischerWert = new HistorischerUfdsWert(
					resultat);
			this.historie24.addDatum(historischerWert);
		}
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
	@Override
	public void aktualisiereParameter(
			final UfdsLangZeitPlPruefungsParameter aktuelleParameter1) {
		this.aktuelleParameter = aktuelleParameter1;
	}

	public boolean hasData(final long testTime) {
		return !historie24.getTeilMenge(testTime, testTime + 1).isEmpty();
	}
}
