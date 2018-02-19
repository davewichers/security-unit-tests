call mvn initialize

REM this sets the config for the Spring app
call setExternalSpringConfig.bat

REM build and run spring app
call mvn -f spring-xml-parsers/pom.xml clean package
start call java -jar spring-xml-parsers/target/spring-xml-parsers-0.1.0.war

call mvn clean compile war:war cargo:run
