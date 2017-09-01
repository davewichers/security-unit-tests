#!/bin/sh

mvn initialize
mvn clean compile war:war cargo:run
