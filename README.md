[![Build Status](https://travis-ci.org/bitctrl/de.bsvrz.dua.pllangufd.svg?branch=develop)](https://travis-ci.org/bitctrl/de.bsvrz.dua.pllangufd)
[![Build Status](https://api.bintray.com/packages/bitctrl/maven/de.bsvrz.dua.pllangufd/images/download.svg)](https://bintray.com/bitctrl/maven/de.bsvrz.dua.pllangufd)

# Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.13 Pl-Prüfung langzeit UFD

Version: ${version}

## Übersicht

Die SWE Pl-Prüfung langzeit UFD vergleicht verschiedene messwertersetzte Umfelddaten
von jeweils drei benachbarten Umfeldmessstellen für einen je Sensortyp parametrierbaren
Zeitbereich miteinander. Die mittlere Messstelle wird als Prüfling mit den beiden
benachbarten Messstellen verglichen. Wird bei diesem Vergleich ein je Sensortyp
vorgegebener Schwellwert überschritten, so wird eine entsprechende Warnung für
den Operator als Betriebsmeldung ausgegeben.

## Versionsgeschichte

### 2.0.2

Release-Datum: 28.07.2016

de.bsvrz.dua.pllangufd.parameter.UfdsLangZeitPlPruefungsParameter

- die Klasse erweitert nicht mehr de.bsvrz.sys.funclib.bitctrl.dua.AllgemeinerDatenContainer
- equals und hashCode sind konform implementiert

de.bsvrz.dua.pllangufd.historie.HistPufferElement
- hashCode entsprechend equals-Funktion ergänzt

de.bsvrz.dua.pllangufd.tests.DuAPlLangUfdTestBase
- der Member "_pruefungLangzeitUfd" sollte nicht statisch sein, der er bei jedem Test neu initialisiert wird

- Javadoc für Java8-Kompatibilität korrigiert
- Obsolete inheritDoc-Kommentare entfernt

### 2.0.1

Release-Datum: 22.07.2016
- Umpacketierung gemäß NERZ-Konvention
  
### 2.0.0

Release-Datum: 31.05.2016

#### Neue Abhängigkeiten

Die SWE benötigt nun das Distributionspaket de.bsvrz.sys.funclib.bitctrl.dua
in Mindestversion 1.5.0 und de.bsvrz.sys.funclib.bitctrl in Mindestversion 1.4.0,
sowie die Kernsoftware in Mindestversion 3.8.0.

#### Änderungen

Folgende Änderungen gegenüber vorhergehenden Versionen wurden durchgeführt:

- Anpassung der Betriebsmeldungen an neue AFo.
- Wenn die maximale Abweichung null ist, wird keine Betriebsmeldung mehr abgesetzt.
- Es wird nur noch einmal pro Vergleichszeitbereich eine Prüfung durchgeführt, statt
  diese fließend (je Datensatz) durchzuführen.

  – Das heißt, bei einem Vergleichszeitbereich von einer Stunde wird die Prüfung
    jetzt einmal jede Stunde durchgeführt, früher wurde sie bei jedem eintreffenden
    Datensatz für die zurückliegende Stunde durchgeführt.
  – Die Prüfung wird trotzdem nur dann durchgeführt, wenn auch neue Daten
    eintreffen.

- Die Niederschlagsereignisse Hagel und Graupel werden nicht mehr berücksichtigt.

#### Fehlerkorrekturen

Folgende Fehler gegenüber vorhergehenden Versionen wurden korrigiert:

- Verschiedene Fehlerkorrekturen und Anpassungen bei der Ermittlung des Vergleichsintervalls
  und bei der Ausgabe von skalierten Werten.

### 1.5.0

- Umstellung auf Java 8 und UTF-8

### 1.4.0
- Umstellung auf Funclib-BitCtrl-Dua

### 1.3.0

- Umstellung auf Maven-Build
- Behandlung nicht unterstützter Sensorarten über die 'UmfeldDatenSensorUnbekannteDatenartException'
- benötigt SWE_de.bsvrz.sys.funclib.bitctrl_FREI_V1.2.3.zip oder höher 

### 1.2.2

- Senden von reinen Betriebsmeldungen in DUA um die Umsetzung von Objekt-PID/ID nach
  Betriebsmeldungs-ID erweitert.  

### 1.2.1

- Sämtliche Konstruktoren DataDescription(atg, asp, sim)
  ersetzt durch DataDescription(atg, asp)

### 1.2.0

- Bash-Startskript hinzu

### 1.1.0

- Hinzufuegen von Skripten zur Konsolenausfuehrung von JUnit-Tests
  
### 1.0.0

- Erste Auslieferung
  
## Bemerkungen

Diese SWE ist eine eigenständige Datenverteiler-Applikation, welche über die Klasse
de.bsvrz.dua.pllangufd.vew.VerwaltungPlLangzeitUFD mit folgenden Parametern gestartet werden kann
(zusaetzlich zu den normalen Parametern jeder Datenverteiler-Applikation):
	-KonfigurationsBereichsPid=pid(,pid)
	
## Kontakt

BitCtrl Systems GmbH
Weißenfelser Straße 67
04229 Leipzig
Phone: +49 34A-490670
mailto: info@bitctrl.de
