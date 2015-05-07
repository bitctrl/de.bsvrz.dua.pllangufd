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

package de.bsvrz.dua.pllangufd.historie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Speichert eine Menge von Datensaetzen, die innerhalb eines bestimmten
 * Intervalls liegen. Dabei gibt das Datum innerhalb dieses Puffers mit dem
 * aeltesten Zeitstempel den Anfang bzw. das obere Ende des (abgeschlossenen)
 * Intervalls an, dessen Laenge mit <code>intervallLaenge</code> beschrieben
 * ist
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @param <G>
 *            Pufferelement
 */
public class HistorischerDatenpuffer<G extends HistPufferElement> implements
		Iterable<G> {

	/**
	 * nach Zeitstempeln sortierter Datenpuffer.
	 */
	private SortedSet<G> puffer = Collections
			.synchronizedSortedSet(new TreeSet<G>());

	/**
	 * aktuelle Maximallaenge des Pufferintervalls.
	 */
	private long intervallLaenge = Long.MIN_VALUE;

	/**
	 * Standardkonstruktor.
	 */
	public HistorischerDatenpuffer() {
	}

	/**
	 * Standardkonstruktor.
	 * 
	 * @param intervallLaenge
	 *            aktuelle Maximallaenge des Pufferintervalls
	 */
	public HistorischerDatenpuffer(final long intervallLaenge) {
		this.intervallLaenge = intervallLaenge;
	}

	/**
	 * Fuegt ein Datum in diesen Puffer ein und loescht gleichzeitig alle
	 * Elemente aus dem Puffer, die nicht mehr im Intervall liegen.
	 * 
	 * @param datum
	 *            das Datum
	 */
	public final void addDatum(final G datum) {
		if (this.intervallLaenge >= Long.MIN_VALUE) {
			synchronized (this.puffer) {
				this.puffer.add(datum);
			}
			this.aufraeumen();
		}
	}

	/**
	 * Setzt die Intervalllaenge.<br>
	 * Es duerfen nur Daten im Puffer stehen, die einen Zeitstempel
	 * <code>t</code> mit folgender Eigenschaft haben:<br>
	 * <br>
	 * 
	 * <code>a - l &lt;= t &lt;= a</code>, mit<br>
	 * <br>
	 * 
	 * a = aktuellster Zeitstempel im Puffer und<br>
	 * l = Intervalllaenge
	 * 
	 * @param intervallLaenge
	 *            neue Intervalllaenge
	 */
	public final void setIntervallLaenge(final long intervallLaenge) {
		if (this.intervallLaenge != intervallLaenge) {
			this.intervallLaenge = intervallLaenge;
			this.aufraeumen();
		}
	}

	/**
	 * Erfragt die Elemente im Puffer, deren Datenzeitstempel im Bereich
	 * [anfang, ende] liegen.
	 * 
	 * @param anfang
	 *            Anfang des abgeschlossenen Intervalls
	 * @param ende
	 *            Ende des abgeschlossenen Intervalls
	 * @return eine (ggf. leere) Kopie der Menge mit den Elementen im Puffer,
	 *         deren Datenzeitstempel im Bereich [anfang, ende] liegen
	 */
	@SuppressWarnings("unchecked")
	public final SortedSet<G> cloneTeilMenge(final long anfang, final long ende) {
		final SortedSet<G> kopie = new TreeSet<>();

		final G groesstesElement = (G) new HistPufferElement(anfang);
		final G kleinstesElement = (G) new HistPufferElement(ende);
		synchronized (this.puffer) {
			kopie
					.addAll(this.puffer.subSet(kleinstesElement,
							groesstesElement));
		}

		return kopie;
	}

	/**
	 * Erfragt die Elemente im Puffer, deren Datenzeitstempel im Bereich
	 * [anfang, ende] liegen.
	 * 
	 * @param anfang
	 *            Anfang des abgeschlossenen Intervalls
	 * @param ende
	 *            Ende des abgeschlossenen Intervalls
	 * @return eine (ggf. leere) Menge mit den Elementen im Puffer, deren
	 *         Datenzeitstempel im Bereich [anfang, ende] liegen
	 */
	@SuppressWarnings("unchecked")
	public final SortedSet<G> getTeilMenge(final long anfang, final long ende) {
		final G groesstesElement = (G) new HistPufferElement(anfang);
		final G kleinstesElement = (G) new HistPufferElement(ende);
		synchronized (this.puffer) {
			return this.puffer.subSet(kleinstesElement, groesstesElement);
		}
	}

	/**
	 * Erfragt den Teil des Pufferinhalts, der noch innerhalb des durch die
	 * uebergebene Intervalllange beschriebenen verkuerzten Intervalls liegt.
	 * 
	 * @param andereIntervallLaenge
	 *            eine andere Intervalllaenge (kleiner als die hier
	 *            eingestellte)
	 * @return der Pufferinhalt im verkuerzten Intervall
	 */
	public final SortedSet<G> getPufferInhalt(final long andereIntervallLaenge) {
		final SortedSet<G> pufferClone = new TreeSet<>();
		synchronized (this.puffer) {
			if (!this.puffer.isEmpty()) {
				final G aktuellsterDatensatz = this.puffer.first();
				final long aeltesterErlaubterZeitStempel = aktuellsterDatensatz
						.getZeitStempel()
						- andereIntervallLaenge;

				for (G pufferElement : this.puffer) {
					if (pufferElement.getZeitStempel() < aeltesterErlaubterZeitStempel) {
						break;
					}
					pufferClone.add(pufferElement);
				}
			}
		}

		return pufferClone;
	}

	/**
	 * Erfragt den Pufferinhalt.
	 * 
	 * @return der Pufferinhalt
	 */
	public final SortedSet<G> getPufferInhalt() {
		return this.puffer;
	}

	/**
	 * Erfragt die aktelle Maximallaenge des Pufferintervalls.
	 * 
	 * @return aktelle Maximallaenge des Pufferintervalls
	 */
	public final long getIntervallLaenge() {
		return this.intervallLaenge;
	}

	/**
	 * Erfragt den Pufferinhalt als Kopie.
	 * 
	 * @return der Pufferinhalt als Kopie
	 */
	public final SortedSet<G> clonePufferInhalt() {
		final SortedSet<G> pufferClone = new TreeSet<>();
		synchronized (this.puffer) {
			pufferClone.addAll(this.puffer);
		}
		return pufferClone;
	}

	/**
	 * Loescht alle Elemente aus dem Puffer, die nicht mehr im Intervall liegen.
	 */
	private void aufraeumen() {
		synchronized (this.puffer) {
			if (!this.puffer.isEmpty()) {
				this.setJetzt(this.puffer.first().getZeitStempel());
			}
		}
	}

	/**
	 * Setzt den Jetzt-Zeitpunkt und bereinigt danach den Puffer, so dass nur
	 * noch Elemente im Puffer sind, deren Datenzeitstempel im Intervall<br>
	 * <code>[jetzt-intervallMax, jetzt]</code><br>
	 * liegen.
	 * 
	 * @param jetzt
	 *            der Jetzt-Zeitpunkt
	 */
	public final void setJetzt(final long jetzt) {
		synchronized (this.puffer) {
			if (!this.puffer.isEmpty()) {
				final long aeltesterErlaubterZeitStempel = jetzt
						- this.intervallLaenge;

				final Collection<G> zuLoeschendeElemente = new ArrayList<>();
				for (G pufferElement : this.puffer) {
					if (pufferElement.getZeitStempel() < aeltesterErlaubterZeitStempel) {
						zuLoeschendeElemente.add(pufferElement);
					}
				}

				this.puffer.removeAll(zuLoeschendeElemente);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<G> iterator() {
		return this.puffer.iterator();
	}

}
