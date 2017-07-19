@echo off
if exist .\contrast.jar (
	call mvn install:install-file -Dfile=lib\Custom-Saxon-HE-9.8.0-3.jar -DgroupId=customsaxon -DartifactId=Custom-Saxon-HE -Dversion=9.8.0-3 -Dpackaging=jar
	call mvn clean compile war:war cargo:run -PdeployWithContrast
) else (
	echo Contrast is a commercial product, so you need a licensed version of Contrast in order to run it on this application. If you have access to Contrast, download the Contrast Agent for Java (contrast.jar^) from the Team Server and put it into the base directory (xxetest^), and then rerun this script.
)
