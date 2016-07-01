***************************************************************************************
*  Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.13 Pl-Prüfung langzeit UFD  *
***************************************************************************************

Version: ${version}

Übersicht
=========

Die SWE Pl-Prüfung langzeit UFD vergleicht verschiedene messwertersetzte Umfelddaten
von jeweils drei benachbarten Umfeldmessstellen für einen je Sensortyp parametrierbaren
Zeitbereich miteinander. Die mittlere Messstelle wird als Prüfling mit den beiden
benachbarten Messstellen verglichen. Wird bei diesem Vergleich ein je Sensortyp
vorgegebener Schwellwert überschritten, so wird eine entsprechende Warnung für
den Operator als Betriebsmeldung ausgegeben.



Versionsgeschichte
==================

1.5.0
=====
- Umstellung auf Java 8 und UTF-8

1.4.0
- Umstellung auf Funclib-BitCtrl-Dua

1.3.0
- Umstellung auf Maven-Build
- Behandlung nicht unterstützter Sensorarten über die 'UmfeldDatenSensorUnbekannteDatenartException'
- benötigt SWE_de.bsvrz.sys.funclib.bitctrl_FREI_V1.2.3.zip oder höher 

1.2.2

  - Senden von reinen Betriebsmeldungen in DUA um die Umsetzung von Objekt-PID/ID nach
    Betriebsmeldungs-ID erweitert.  

1.2.1

  - Sämtliche Konstruktoren DataDescription(atg, asp, sim)
    ersetzt durch DataDescription(atg, asp)

1.2.0

  - Bash-Startskript hinzu

1.1.0

  - Hinzufuegen von Skripten zur Konsolenausfuehrung von JUnit-Tests
  
1.0.0

  - Erste Auslieferung
  
Bemerkungen
===========

Diese SWE ist eine eigenständige Datenverteiler-Applikation, welche über die Klasse
de.bsvrz.dua.pllangufd.vew.VerwaltungPlLangzeitUFD mit folgenden Parametern gestartet werden kann
(zusaetzlich zu den normalen Parametern jeder Datenverteiler-Applikation):
	-KonfigurationsBereichsPid=pid(,pid)
	
- Tests:

Alle Tests befinden sich unterhalb des Verzeichnisses junit und sind als JUnit-Tests ausführbar.
Die Tests untergliedern sich wie folgt:
	- Allgemeine Tests: z.B. Test des historischen Datenpuffers
	- DAV-Tests: die Tests, die die konkrete in Afo beschriebene Funktionalität der SWE testen (bei der
	  Durchführung dieser Tests wird jeweils implizit eine Instanz der SWE Pl.Prüfung langzeit UFD
	  gestartet)

Voraussetzungen für die DAV-Tests:
- Start der Test-Konfiguration (extra/testKonfig.zip)
- Anpassung der DAV-Start-Parameter (Variable CON_DATA) innerhalb von 
	junit/de.bsvrz.dua.pllangufd.DAVTest.java
- Die Parametrierung muss so eingestellt sein, dass die Parameter der Pl-Prüfung langzeit UFD
  für alle Umfelddatensensoren gespeichert werden

Inhalt/Verlauf DAV-Tests:
1. Datenarten LT, NI, SW und WFD:
Die Parameter für den Prüfling werden vorher wie folgt gesetzt:
	Vergleichsintervall: 1 Stunde
	maxAusfallZeit:      30 Minuten
	maxAbweichungXY:     1

Es werden für je drei (innerhalb der Testkonfiguration) assoziierte Umfelddatensensoren
folgende Messdaten (Aspekt MWE) gesendet:

            | provoziere Nachfolger         | provoziere Vorgänger          | provoziere Prüfling           | provoziere auf Meldung 
                                                                                                              "Pl-Prüfung konnte nicht
				                                                                                               durchgeführt werden"
Prüfling  	|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-1-|-6-|-6-|-1-|-1-|-1-|-1-|
                                                                                                            | Sende nichts
Vorgänger   |---1---|-1-|-1-|---1---|-1-|-1-|---1---|-1-|-1-|---1---|-6-|-6-|---1---|-1-|-1-|---1---|-1-|-1-|-------|---|---|

Nachfolger  |---1---|---1---|---4---|---4---|---1---|---1---|---1---|---1---|---1---|---1---|---1---|---1---|---1---|---1---|
                1. Stunde   |   2. Stunde   |   3. Stunde   |   4. Stunde   |   5. Stunde   |   6. Stunde   |   7. Stunde   |

Die dabei entstandenen Betriebsmeldungen werden überprüft und mit den erwarteten verglichen.


2. Datenarten FBZ und NA:
Die Parameter für den Prüfling werden vorher wie folgt gesetzt:
	Vergleichsintervall: 1 Stunde
	maxAusfallZeit:      30 Minuten
	maxAbweichungXY:     15 Minuten

Es werden für je drei (innerhalb der Testkonfiguration) assoziierte Umfelddatensensoren
jeweils die Zustände A und B (Aspekt MWE) gesendet:

            | provoziere Nachfolger         | provoziere Vorgänger          | provoziere Prüfling           | provoziere auf Meldung 
                                                                                                              "Pl-Prüfung konnte nicht
				                                                                                               durchgeführt werden"
Prüfling  	|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-B-|-B-|-A-|-A-|-A-|-A-|-A-|-A-|-/-|-/-|-/-|-A-|
                                                                                                                            | sende "fehlerhaft"
Vorgänger   |-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-B-|-B-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|

Nachfolger  |-A-|-A-|-A-|-A-|-A-|-A-|-B-|-B-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|-A-|
                A. Stunde   |   2. Stunde   |   3. Stunde   |   4. Stunde   |   5. Stunde   |   6. Stunde   |   7. Stunde   |   8. Stunde

Die dabei entstandenen Betriebsmeldungen werden überprüft und mit den erwarteten verglichen.
	
Die Tests wurden so bereits erfolgreich ausgeführt.


Disclaimer
==========

Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.13 Pl-Prüfung langzeit UFD
Copyright (C) 2007 BitCtrl Systems GmbH 

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the Free Software Foundation, Inc., 5A
Franklin Street, Fifth Floor, Boston, MA 02AA0-A30A, USA.


Kontakt
=======

BitCtrl Systems GmbH
Weißenfelser Straße 67
04229 Leipzig
Phone: +49 34A-490670
mailto: info@bitctrl.de
