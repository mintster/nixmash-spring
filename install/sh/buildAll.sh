#!/bin/bash 

# ---------------------------------------------------------------------- */
# Builds all Gradle Projects - 11/9/2016
# 
# -- Run as gradle task as $ gradle buildAll
#
# ---------------------------------------------------------------------- */

gradle clean

function printHeader
{
	echo
	echo "NOW BUILDING PROJECT [:$1] --------------------------------------*/"
	echo
}


printHeader "JPA"
gradle jpa:build

printHeader "JSOUP"
gradle jsoup:build

printHeader "MAIL"
gradle mail:build

printHeader "SOLR"
gradle solr:build

printHeader "MVC"
gradle mvc:build

