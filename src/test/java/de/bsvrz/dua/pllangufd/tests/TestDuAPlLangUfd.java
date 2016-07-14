/*
 * Copyright 2016 by Kappich Systemberatung Aachen
 * 
 * This file is part of de.bsvrz.dua.pllangufd.tests.
 * 
 * de.bsvrz.dua.pllangufd.tests is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * de.bsvrz.dua.pllangufd.tests is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with de.bsvrz.dua.pllangufd.tests.  If not, see <http://www.gnu.org/licenses/>.

 * Contact Information:
 * Kappich Systemberatung
 * Martin-Luther-Straße 14
 * 52062 Aachen, Germany
 * phone: +49 241 4090 436 
 * mail: <info@kappich.de>
 */

package de.bsvrz.dua.pllangufd.tests;

import de.bsvrz.dav.daf.main.*;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.tests.DuATestBase;
import de.bsvrz.sys.funclib.operatingMessage.OperatingMessageInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TBD Dokumentation
 *
 * @author Kappich Systemberatung
 */
public class TestDuAPlLangUfd extends DuAPlLangUfdTestBase {

	private AttributeGroup _atg;
	private AttributeGroup _atgNs;
	private Aspect _aspSend;
	public static final String[] PIDS = new String[]{"ufd.lt.04", "ufd.lt.06", "ufd.lt.07"};
	public static final String[] PIDSNS = new String[]{"ufd.na.04", "ufd.na.06", "ufd.na.07"};

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		_atg = _dataModel.getAttributeGroup("atg.ufdsLuftTemperatur");
		_atgNs = _dataModel.getAttributeGroup("atg.ufdsNiederschlagsArt");
		_aspSend = _dataModel.getAspect("asp.messWertErsetzung");
		_connection.subscribeSender(new ClientSenderInterface() {
			@Override
			public void dataRequest(final SystemObject object, final DataDescription dataDescription, final byte state) {
			}

			@Override
			public boolean isRequestSupported(final SystemObject object, final DataDescription dataDescription) {
				return false;
			}
		}, Arrays.asList(PIDS).stream().map(_dataModel::getObject).collect(Collectors.toList()), new DataDescription(_atg, _aspSend), SenderRole.source());		_connection.subscribeSender(new ClientSenderInterface() {
			@Override
			public void dataRequest(final SystemObject object, final DataDescription dataDescription, final byte state) {
			}

			@Override
			public boolean isRequestSupported(final SystemObject object, final DataDescription dataDescription) {
				return false;
			}
		}, Arrays.asList(PIDSNS).stream().map(_dataModel::getObject).collect(Collectors.toList()), new DataDescription(_atgNs, _aspSend), SenderRole.source());
	}


	private long t(final String s) {
		return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("dd.MM.uu HH:mm")).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	@Test
	public void testDua78() throws Exception {
		fakeParamApp.publishParam(
				PIDS[1],
				"atg.ufdsLangzeitPLPrüfungLuftTemperatur",
				"{" +
						"VergleichsIntervall:'1 Stunde'," +
						"maxAusfallZeit:'10 Minuten'," +
						"maxAbweichungLT:'1'" +
						"}");
		DuATestBase.TestData testData = readTestData("DUA78.csv", true);
		List<List<List<String>>> dataSets = testData.getDataSets();
		final List<String> errors = new ArrayList<String>();
		for(List<List<String>> sets : dataSets.subList(1,dataSets.size())) {
			List<String> line = sets.get(0);
			sendData(makeDatas(PIDS, line, _atg));
			OperatingMessageInterface message = pollMessage();
			if(line.get(12).contains("Betriebsmeldung")){
				if(message == null) {
					errors.add("Betriebsmeldung erwartet in Zeile " + line.get(0));
				}
				else {
					System.out.println("Zeile: " + line.get(0) + ": " + message);
				}
			}
			else {
				 if(message != null){
					 errors.add("Unerwartete Betriebsmeldung in Zeile " + line.get(0) + ": " + message);
				 }
			}
		}
		for(String error : errors) {
			System.out.println("error = " + error);
		}
		Assert.assertEquals(0, errors.size());
	}
	
	@Test
	public void testDua78b() throws Exception {
		fakeParamApp.publishParam(
				PIDS[1],
				"atg.ufdsLangzeitPLPrüfungLuftTemperatur",
				"{" +
						"VergleichsIntervall:'1 Stunde'," +
						"maxAusfallZeit:'60 Minuten'," +
						"maxAbweichungLT:'1'" +
						"}");
		DuATestBase.TestData testData = readTestData("DUA78b.csv", true);
		List<List<List<String>>> dataSets = testData.getDataSets();
		final List<String> errors = new ArrayList<String>();
		for(List<List<String>> sets : dataSets.subList(1,dataSets.size())) {
			List<String> line = sets.get(0);
			sendData(makeDatas(PIDS, line, _atg));
			OperatingMessageInterface message = pollMessage();
			if(line.get(12).contains("Betriebsmeldung")){
				if(message == null) {
					errors.add("Betriebsmeldung erwartet in Zeile " + line.get(0));
				}
				else {
					System.out.println("Zeile: " + line.get(0) + ": " + message);
				}
			}
			else {
				 if(message != null){
					 errors.add("Unerwartete Betriebsmeldung in Zeile " + line.get(0) + ": " + message);
				 }
			}
		}
		for(String error : errors) {
			System.out.println("error = " + error);
		}
		Assert.assertEquals(0, errors.size());
	}


	@Test
	public void testDua78NoCheck() throws Exception {
		// Prüfung mit Grenzwert 0
		fakeParamApp.publishParam(
				PIDS[1],
				"atg.ufdsLangzeitPLPrüfungLuftTemperatur",
				"{" +
						"VergleichsIntervall:'1 Stunde'," +
						"maxAusfallZeit:'10 Minuten'," +
						"maxAbweichungLT:'0'" +
						"}");
		DuATestBase.TestData testData = readTestData("DUA78.csv", true);
		List<List<List<String>>> dataSets = testData.getDataSets();
		final List<String> errors = new ArrayList<String>();
		for(List<List<String>> sets : dataSets.subList(1,dataSets.size())) {
			List<String> line = sets.get(0);
			sendData(makeDatas(PIDS, line, _atg));
			OperatingMessageInterface message = pollMessage();
			if(message != null){
				errors.add("Unerwartete Betriebsmeldung in Zeile " + line.get(0) + ": " + message);
			}
		}
		for(String error : errors) {
			System.out.println("error = " + error);
		}
		Assert.assertEquals(0, errors.size());
	}


	@Test
	public void testDua78NS() throws Exception {
		fakeParamApp.publishParam(
				PIDSNS[1],
				"atg.ufdsLangzeitPLPrüfungNiederschlagsArt",
				"{" +
						"VergleichsIntervall:'1 Stunde'," +
						"maxAusfallZeit:'10 Minuten'," +
						"maxAbweichungNS:'10 Minuten'" +
						"}");
		DuATestBase.TestData testData = readTestData("DUA78NS.csv", true);
		List<List<List<String>>> dataSets = testData.getDataSets();
		final List<String> errors = new ArrayList<String>();
		for(List<List<String>> sets : dataSets.subList(1,dataSets.size())) {
			List<String> line = sets.get(0);
			sendData(makeDatas(PIDSNS, line, _atgNs));
			OperatingMessageInterface message = pollMessage();
			if(line.get(12).contains("Betriebsmeldung")){
				if(message == null) {
					errors.add("Betriebsmeldung erwartet in Zeile " + line.get(0));
				}
				else {
					System.out.println("Zeile: " + line.get(0) + ": " + message);
				}
			}
			else {
				if(message != null){
					errors.add("Unerwartete Betriebsmeldung in Zeile " + line.get(0) + ": " + message);
				}
			}
		}
		for(String error : errors) {
			System.out.println("error = " + error);
		}
		Assert.assertEquals(0, errors.size());
	}

	private ResultData[] makeDatas(final String[] pids, final List<String> line, final AttributeGroup atg) {
		ResultData[] result = new ResultData[3];
		for(int i = 0; i < 3; i++){
			result[i] = new ResultData(
					_dataModel.getObject(pids[i]),
					new DataDescription(atg, _aspSend),
			        t(line.get(1)),
			        makeData(line.get(3+i), atg)
					);
		}
		return result;
	}

	private Data makeData(final String value, final AttributeGroup atg) {
		if(value == null || value.trim().isEmpty()) return null;
		Data data = _connection.createData(atg);
		resetData(data);
		data.getTimeValue("T").setSeconds(60);
		if(atg.getPid().equals(_atg.getPid())) {
			if(value.startsWith("-100")) {
				data.getItem("LuftTemperatur").getUnscaledValue("Wert").set(Integer.parseInt(value));
			}
			else {
				data.getItem("LuftTemperatur").getTextValue("Wert").setText(value);
			}
		}
		else {
			data.getItem("NiederschlagsArt").getUnscaledValue("Wert").set(Integer.parseInt(value));
		}
		return data;
	}
}
