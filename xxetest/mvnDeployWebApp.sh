#!/bin/sh

mvn install:install-file -Dfile=lib/Custom-Saxon-HE-9.8.0-3.jar -DgroupId=customsaxon -DartifactId=Custom-Saxon-HE -Dversion=9.8.0-3 -Dpackaging=jar
mvn install:install-file -Dfile=lib/Custom-xercesImpl-2.11.0.jar -DgroupId=customxerces -DartifactId=Custom-xercesImpl -Dversion=2.11.0 -Dpackaging=jar
mvn clean compile war:war cargo:run

