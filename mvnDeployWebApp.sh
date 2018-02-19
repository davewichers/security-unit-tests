#!/bin/sh 

mvn initialize
./setExternalSpringConfig.sh # this sets the config for the Spring app

# build and run spring app
mvn -f spring-xml-parsers/pom.xml clean package
java -jar spring-xml-parsers/target/spring-xml-parsers-0.1.0.war &
spring_pid=$!
trap ctrl_c 2 # kill backgrounded process if ctrl-c is pressed
ctrl_c() {
    echo "\n*** Killing Spring PID $spring_pid ***\n"
    kill $spring_pid
}

mvn clean compile war:war cargo:run

