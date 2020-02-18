#!/bin/bash

DOCS=/data/clinical_trials/extracted
#DOCS=/home/info/trec/data/clinicaltrials

nohup mvn -DskipTests=true install exec:java -Dexec.mainClass="at.medunigraz.imi.bst.clinicaltrial.Indexing" -Dexec.args="$DOCS" | tee clinicaltrials.log
