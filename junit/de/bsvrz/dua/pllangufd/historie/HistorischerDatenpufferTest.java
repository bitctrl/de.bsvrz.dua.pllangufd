/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.x
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
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.pllangufd.historie;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testet den historischen Datenpuffer
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class HistorischerDatenpufferTest {

	/**
	 * Testet die Einfuegemethode
	 */
	@Test
	public void testAddDatum()
	throws Exception{
		HistorischerDatenpuffer<HistPufferElement> puffer = new HistorischerDatenpuffer<HistPufferElement>();
		
		Assert.assertEquals(0, puffer.getPufferInhalt().size());
		Assert.assertEquals(0, puffer.getPufferInhalt(10).size());
		
		puffer.addDatum(new HistPufferElement(10));
		puffer.addDatum(new HistPufferElement(11));
		
		Assert.assertEquals(0, puffer.getPufferInhalt().size());
		Assert.assertEquals(0, puffer.getPufferInhalt(10).size());
		
		puffer.setIntervallLaenge(1);
		
		Assert.assertEquals(1, puffer.getIntervallLaenge());
		
		puffer.addDatum(new HistPufferElement(3));
		Assert.assertEquals(3, puffer.getPufferInhalt().iterator().next().getZeitStempel());
		Assert.assertEquals(3, puffer.getPufferInhalt(0).iterator().next().getZeitStempel());
		Assert.assertEquals(1, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(0).size());

		puffer.addDatum(new HistPufferElement(2));
		Assert.assertEquals(3, puffer.getPufferInhalt().iterator().next().getZeitStempel());
		Assert.assertEquals(3, puffer.getPufferInhalt(0).iterator().next().getZeitStempel());
		Assert.assertEquals(2, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(0).size());

		puffer.addDatum(new HistPufferElement(1));
		Assert.assertEquals(3, puffer.getPufferInhalt().iterator().next().getZeitStempel());
		Assert.assertEquals(3, puffer.getPufferInhalt(0).iterator().next().getZeitStempel());
		Assert.assertEquals(2, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(0).size());
		
		puffer.addDatum(new HistPufferElement(4));
		Assert.assertEquals(4, puffer.getPufferInhalt().iterator().next().getZeitStempel());
		Assert.assertEquals(4, puffer.getPufferInhalt(0).iterator().next().getZeitStempel());
		Assert.assertEquals(2, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(0).size());

		puffer.addDatum(new HistPufferElement(5));
		Assert.assertEquals(5, puffer.getPufferInhalt().iterator().next().getZeitStempel());
		Assert.assertEquals(5, puffer.getPufferInhalt(0).iterator().next().getZeitStempel());
		Assert.assertEquals(2, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(0).size());

		puffer.addDatum(new HistPufferElement(8));
		Assert.assertEquals(8, puffer.getPufferInhalt().iterator().next().getZeitStempel());
		Assert.assertEquals(8, puffer.getPufferInhalt(0).iterator().next().getZeitStempel());
		Assert.assertEquals(1, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(0).size());

		puffer.addDatum(new HistPufferElement(9));
		Assert.assertEquals(9, puffer.getPufferInhalt().iterator().next().getZeitStempel());
		Assert.assertEquals(9, puffer.getPufferInhalt(0).iterator().next().getZeitStempel());
		Assert.assertEquals(2, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(0).size());
		
		puffer.addDatum(new HistPufferElement(9));
		Assert.assertEquals(9, puffer.getPufferInhalt().iterator().next().getZeitStempel());
		Assert.assertEquals(9, puffer.getPufferInhalt(0).iterator().next().getZeitStempel());
		Assert.assertEquals(2, puffer.getPufferInhalt().size());
		Assert.assertEquals(2, puffer.getPufferInhalt(1).size());
		
		puffer.addDatum(new HistPufferElement(20));
		Assert.assertEquals(20, puffer.getPufferInhalt().iterator().next().getZeitStempel());
		Assert.assertEquals(20, puffer.getPufferInhalt(0).iterator().next().getZeitStempel());
		Assert.assertEquals(1, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(1).size());
		
		puffer.setIntervallLaenge(10);
		Assert.assertEquals(10, puffer.getIntervallLaenge());
		puffer.addDatum(new HistPufferElement(21));
		puffer.addDatum(new HistPufferElement(22));
		puffer.addDatum(new HistPufferElement(25));
		puffer.addDatum(new HistPufferElement(30));

		Assert.assertEquals(30, puffer.getPufferInhalt().iterator().next().getZeitStempel());
		Assert.assertEquals(30, puffer.getPufferInhalt(5).iterator().next().getZeitStempel());
		Assert.assertEquals(5, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(0).size());
		Assert.assertEquals(1, puffer.getPufferInhalt(1).size());
		Assert.assertEquals(1, puffer.getPufferInhalt(2).size());
		Assert.assertEquals(1, puffer.getPufferInhalt(3).size());
		Assert.assertEquals(1, puffer.getPufferInhalt(4).size());
		Assert.assertEquals(2, puffer.getPufferInhalt(5).size());
		Assert.assertEquals(2, puffer.getPufferInhalt(6).size());
		Assert.assertEquals(2, puffer.getPufferInhalt(7).size());
		Assert.assertEquals(3, puffer.getPufferInhalt(8).size());
		Assert.assertEquals(4, puffer.getPufferInhalt(9).size());
		Assert.assertEquals(5, puffer.getPufferInhalt(10).size());
		Assert.assertEquals(5, puffer.getPufferInhalt(11).size());
	
		puffer.addDatum(new HistPufferElement(40));
		Assert.assertEquals(2, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(5).size());

		puffer.addDatum(new HistPufferElement(60));
		Assert.assertEquals(1, puffer.getPufferInhalt().size());
		Assert.assertEquals(1, puffer.getPufferInhalt(5).size());
		
		puffer.addDatum(new HistPufferElement(61));
		puffer.addDatum(new HistPufferElement(62));
		puffer.addDatum(new HistPufferElement(63));
		puffer.addDatum(new HistPufferElement(64));
		
		long[] werte = new long[]{64, 63, 62, 61, 60};
		int i=0;
		for(HistPufferElement elem:puffer){
			Assert.assertEquals(werte[i++], elem.getZeitStempel());
		}
		
		long[] werte2 = new long[]{64, 63, 62};
		i=0;
		for(HistPufferElement elem:puffer.getPufferInhalt(2)){
			Assert.assertEquals(werte2[i++], elem.getZeitStempel());
		}
		
		Assert.assertEquals(64, puffer.iterator().next().getZeitStempel());
	}

	
	/**
	 * Testet extraktion von Teilmengen
	 */
	@Test
	public void testGetTeilMenge()
	throws Exception{
		HistorischerDatenpuffer<HistPufferElement> puffer = new HistorischerDatenpuffer<HistPufferElement>();
		
		Assert.assertEquals(0, puffer.getPufferInhalt().size());
		Assert.assertEquals(0, puffer.getPufferInhalt(10).size());
		
		puffer.setIntervallLaenge(15);
		puffer.addDatum(new HistPufferElement(10));
		puffer.addDatum(new HistPufferElement(11));
		
		Assert.assertEquals(1, puffer.getTeilMenge(10, 20).size());
		Assert.assertEquals(0, puffer.getTeilMenge(11, 20).size());
		Assert.assertEquals(0, puffer.getTeilMenge(12, 20).size());

		long[] werte = new long[]{11, 10};
		int i=0;
		for(HistPufferElement elem:puffer.getTeilMenge(9, 20)){
			Assert.assertEquals(werte[i++], elem.getZeitStempel());
		}

		puffer.addDatum(new HistPufferElement(20));
		puffer.addDatum(new HistPufferElement(21));

		Assert.assertEquals(2, puffer.getTeilMenge(10, 20).size());
		Assert.assertEquals(1, puffer.getTeilMenge(11, 20).size());
		Assert.assertEquals(1, puffer.getTeilMenge(12, 20).size());
		Assert.assertEquals(2, puffer.getTeilMenge(12, 21).size());
		Assert.assertEquals(2, puffer.getTeilMenge(12, 22).size());

		werte = new long[]{20, 11, 10};
		i=0;
		for(HistPufferElement elem:puffer.getTeilMenge(10, 20)){
			Assert.assertEquals(werte[i++], elem.getZeitStempel());
		}

		werte = new long[]{20, 11};
		i=0;
		for(HistPufferElement elem:puffer.getTeilMenge(11, 20)){
			Assert.assertEquals(werte[i++], elem.getZeitStempel());
		}

		werte = new long[]{20};
		i=0;
		for(HistPufferElement elem:puffer.getTeilMenge(12, 20)){
			Assert.assertEquals(werte[i++], elem.getZeitStempel());
		}

		werte = new long[]{21, 20};
		i=0;
		for(HistPufferElement elem:puffer.getTeilMenge(12, 21)){
			Assert.assertEquals(werte[i++], elem.getZeitStempel());
		}

		werte = new long[]{21, 20};
		i=0;
		for(HistPufferElement elem:puffer.getTeilMenge(12, 22)){
			Assert.assertEquals(werte[i++], elem.getZeitStempel());
		}
	}

}
