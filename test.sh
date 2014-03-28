#!/bin/sh
echo Starting test...
cd ./Android/iSENSE\ Imports/
java -cp ./bin/isense\ imports.jar:./lib/junit-4.10.jar \
org.junit.runner.JUnitCore edu.uml.cs.isense.test.APITest
echo Test complete!