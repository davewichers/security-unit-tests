SET address=localhost
SET port=9000
SET proto=http

SET external_config=.\src\main\resources\spring_external_config.properties
echo server.address=%address%> %external_config%
echo server.port=%port%>> %external_config%
echo server.proto=%proto%>> %external_config%

SET spring_config=.\spring-xml-parsers\src\main\resources\application.properties
echo server.address=%address%> %spring_config%
echo server.port=%port%>> %spring_config%
REM no proto yet

