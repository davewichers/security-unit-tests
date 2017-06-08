@echo off
if exist .\contrast.jar (
	call mvn clean compile war:war cargo:run -PdeployWithContrast
) else (
	echo Contrast is a commercial product, so you need a licensed version of Contrast in order to run it on the Benchmark. If you have access to Contrast, download the Contrast Agent for Java (contrast.jar^) from the Team Server and put it into the base directory (xxetest^), and then rerun this script.
)