@echo off


REM ############################################################################
REM Folgende Parameter müssen überprüft und evtl. angepasst werden

REM Java-Klassenpfad
SET jar=de.bsvrz.dua.pllangufd-runtime.jar

REM Argumente für die Java Virtual Machine
SET jvmArgs=-showversion -Dfile.encoding=ISO-8859-1 -Xms32m -Xmx256m -cp ..\%jar%
REM Parameter für den Datenverteiler
SET benutzer=Tester
SET passwortDatei=..\..\..\skripte-dosshell\passwd
SET dav1Host=localhost
SET dav1AppPort=8083



REM Applikation starten
CHCP 1252
TITLE PL-Prüfung langzeit UFD
java %jvmArgs% ^
de.bsvrz.dua.pllangufd.vew.VerwaltungPlLangzeitUFD ^
-debugLevelStdErrText=info ^
-debugSetLoggerAndLevel=:config ^
-datenverteiler=%dav1Host%:%dav1AppPort% ^
-benutzer=%benutzer% ^
-authentifizierung=%passwortDatei% ^
-KonfigurationsBereichsPid=kb.plLangUfdTestModell

REM Nach dem Beenden warten, damit Meldungen gelesen werden können
PAUSE

