@echo off

call ..\..\..\skripte-dosshell\einstellungen.bat

set cp=..\..\de.bsvrz.sys.funclib.bitctrl\de.bsvrz.sys.funclib.bitctrl-runtime.jar
set cp=%cp%;..\de.bsvrz.dua.pllangufd-runtime.jar
set cp=%cp%;..\de.bsvrz.dua.pllangufd-test.jar
set cp=%cp%;..\..\junit-4.1.jar

title Pruefungen SE4 - DUA, SWE 4.13

echo =============================================================
echo #  Pruefungen SE4 - DUA, SWE 4.13
echo #
echo #  Testet allgemein den historischen Datenpuffer
echo =============================================================
echo.
%java% -cp %cp% org.junit.runner.JUnitCore de.bsvrz.dua.pllangufd.historie.HistorischerDatenpufferTest
pause

echo =============================================================
echo #  Pruefungen SE4 - DUA, SWE 4.13
echo #
echo #  Testet die Langzeitprüfung des Fahrbahnzustandes
echo =============================================================
echo.

%java% -cp %cp% org.junit.runner.JUnitCore de.bsvrz.dua.pllangufd.fbz.FbzTest
pause

echo =============================================================
echo #  Pruefungen SE4 - DUA, SWE 4.13
echo #
echo #  Testet die Langzeitprüfung der Niederschlagsart
echo =============================================================
echo.

%java% -cp %cp% org.junit.runner.JUnitCore de.bsvrz.dua.pllangufd.na.NaTest
pause


echo =============================================================
echo #  Pruefungen SE4 - DUA, SWE 4.13
echo #
echo #  Testet die Langzeitprüfung der Lufttemperatur
echo =============================================================
echo.

%java% -cp %cp% org.junit.runner.JUnitCore de.bsvrz.dua.pllangufd.rest.LtTest
pause

echo =============================================================
echo #  Pruefungen SE4 - DUA, SWE 4.13
echo #
echo #  Testet die Langzeitprüfung der Niederschlagsintensität
echo =============================================================
echo.

%java% -cp %cp% org.junit.runner.JUnitCore de.bsvrz.dua.pllangufd.rest.NiTest
pause

echo =============================================================
echo #  Pruefungen SE4 - DUA, SWE 4.13
echo #
echo #  Testet die Langzeitprüfung der Sichtweite
echo =============================================================
echo.

%java% -cp %cp% org.junit.runner.JUnitCore de.bsvrz.dua.pllangufd.rest.SwTest
pause

echo =============================================================
echo #  Pruefungen SE4 - DUA, SWE 4.13
echo #
echo #  Testet die Langzeitprüfung der Wasserfilmdicke
echo =============================================================
echo.

%java% -cp %cp% org.junit.runner.JUnitCore de.bsvrz.dua.pllangufd.rest.WfdTest
pause