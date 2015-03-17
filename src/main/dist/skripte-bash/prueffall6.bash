#!/bin/bash
source ../../../skripte-bash/einstellungen.sh

echo =================================================
echo =
echo =       Pruefungen SE4 - DUA, SWE 4.13 
echo =
echo =================================================
echo 

index=0
declare -a tests
declare -a testTexts

#########################
# Name der Applikation #
#########################
appname=pllangufd

########################
#     Testroutinen     #
########################

testTexts[$index]="Testet die Langzeitprüfung des Fahrbahnzustandes"
tests[$index]="fbz.FbzTest"
index=$(($index+1))

testTexts[$index]="Testet die Langzeitprüfung der Niederschlagsart"
tests[$index]="na.NaTest"
index=$(($index+1))

testTexts[$index]="Testet die Langzeitprüfung der Lufttemperatur"
tests[$index]="rest.LtTest"
index=$(($index+1))

testTexts[$index]="Testet die Langzeitprüfung der Niederschlagsintensität"
tests[$index]="rest.NiTest"
index=$(($index+1))

testTexts[$index]="Testet die Langzeitprüfung der Sichtweite"
tests[$index]="rest.SwTest"
index=$(($index+1))

testTexts[$index]="Testet die Langzeitprüfung der Wasserfilmdicke"
tests[$index]="rest.WfdTest"
index=$(($index+1))


########################
#      ClassPath       #
########################
cp="../../de.bsvrz.sys.funclib.bitctrl/de.bsvrz.sys.funclib.bitctrl-runtime.jar"
cp=$cp":../de.bsvrz.dua."$appname"-runtime.jar"
cp=$cp":../de.bsvrz.dua."$appname"-test.jar"
cp=$cp":../../junit-4.1.jar"

########################
#     Ausfuehrung      #
########################

for ((i=0; i < ${#tests[@]}; i++));
do
	echo "================================================="
	echo "="
	echo "= Test Nr. "$(($i+1))":"
	echo "="
	echo "= "${testTexts[$i]}
	echo "="
	echo "================================================="
	echo 
	java -cp $cp $jvmArgs org.junit.runner.JUnitCore "de.bsvrz.dua."$appname"."${tests[$i]}
	sleep 5
done

exit
