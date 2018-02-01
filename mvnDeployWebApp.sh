#!/bin/sh 

mvn initialize
./setExternalSpringConfig.sh # this sets the config for the Spring app
mvn clean compile war:war cargo:run
