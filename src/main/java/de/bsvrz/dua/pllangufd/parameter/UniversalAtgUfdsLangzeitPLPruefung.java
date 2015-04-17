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


package de.bsvrz.dua.pllangufd.parameter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Klasse zum Auslesen aller der Parameter-Attributgruppen
 * <code>atg.ufdsLangzeitPLPrüfungXXX</code>.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public class UniversalAtgUfdsLangzeitPLPruefung implements
		ClientReceiverInterface {

	/**
	 * aktueller Parametersatz.
	 */
	private UfdsLangZeitPlPruefungsParameter parameterSatz;

	/**
	 * beobachter dieses Objektes (werden ueber aktuelle Parameter informiert).
	 */
	private Set<IUniversalAtgUfdsLangzeitPLPruefungListener> listenerMenge = Collections
			.synchronizedSet(new HashSet<IUniversalAtgUfdsLangzeitPLPruefungListener>());

	/**
	 * Standardkonstruktor.
	 * 
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            Systemobjekt eines beliebigen Umfelddatensensors
	 * @throws UmfeldDatenSensorUnbekannteDatenartException 
	 */
	public UniversalAtgUfdsLangzeitPLPruefung(final ClientDavInterface dav,
			final SystemObject objekt) throws UmfeldDatenSensorUnbekannteDatenartException {
		final UmfeldDatenArt datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(objekt);

		final DataDescription parameterBeschreibung = new DataDescription(
				dav.getDataModel().getAttributeGroup(
						"atg.ufdsLangzeitPLPrüfung" + datenArt.getName()), //$NON-NLS-1$
				dav.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL));
		dav.subscribeReceiver(this, objekt, parameterBeschreibung,
				ReceiveOptions.normal(), ReceiverRole.receiver());
	}

	/**
	 * Fuegt der Menge aller Listener einen Listener hinzu.
	 * 
	 * @param listener
	 *            ein neuer Listener
	 * @param sofortInformieren
	 *            ob der neue Listener sofort ueber das aktuelle Datum
	 *            informiert werden soll
	 */
	public final synchronized void addListener(
			final IUniversalAtgUfdsLangzeitPLPruefungListener listener,
			final boolean sofortInformieren) {
		if (this.listenerMenge.add(listener) && this.parameterSatz != null) {
			listener.aktualisiereParameter(this.parameterSatz);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(final ResultData[] resultate) {
		if (resultate != null) {
			for (ResultData resultat : resultate) {
				if (resultat != null) {
					synchronized (this) {
						try {
							this.parameterSatz = new UfdsLangZeitPlPruefungsParameter(
									resultat);
						} catch (final UmfeldDatenSensorUnbekannteDatenartException e) {
							Debug.getLogger().warning(e.getMessage());
							continue;
						}
						for (IUniversalAtgUfdsLangzeitPLPruefungListener listener : listenerMenge) {
							listener.aktualisiereParameter(this.parameterSatz);
						}
					}
				}
			}
		}
	}

}
