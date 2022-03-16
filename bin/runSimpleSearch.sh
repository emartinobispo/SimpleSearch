#!/bin/bash
JAR_FILE=${1:-../target/SimpleSearch-1.0.0-jar-with-dependencies.jar}
INDEXABLE_DIRECTORY=${2:-../input/}

echo ${JAR_FILE}
echo ${INDEXABLE_DIRECTORY}

exec java -cp ../target/SimpleSearch-1.0.0-jar-with-dependencies.jar org.simplesearch.SimpleSearch ../input/