#!/bin/bash

# In das Verzeichnis des Skripts wechseln, damit relative Pfade funktionieren
cd `dirname $0`

source ../../../skripte-bash/einstellungen.sh

################################################################################
# SWE-Spezifische Parameter	(�berpr�fen und anpassen)                          #
################################################################################

kb="KonfigurationsBereichsPid="

################################################################################
# Folgende Parameter m�ssen �berpr�ft und evtl. angepasst werden               #
################################################################################

# Parameter f�r den Java-Interpreter, als Standard werden die Einstellungen aus # einstellungen.sh verwendet.
#jvmArgs="-Dfile.encoding=ISO-8859-1"

# Parameter f�r den Datenverteiler, als Standard werden die Einstellungen aus # einstellungen.sh verwendet.
#dav1="-datenverteiler=localhost:8083 -benutzer=Tester -authentifizierung=passwd -debugFilePath=.."

################################################################################
# Ab hier muss nichts mehr angepasst werden                                    #
################################################################################

# Applikation starten
java $jvmArgs -jar ../de.bsvrz.dua.pllangufd-runtime.jar \
	$dav1 \
	$kb \
	-debugLevelFileText=all \
	-debugLevelStdErrText=:error \
	-debugSetLoggerAndLevel=:none \
	-debugSetLoggerAndLevel=de.bsvrz.iav:config \
	&
