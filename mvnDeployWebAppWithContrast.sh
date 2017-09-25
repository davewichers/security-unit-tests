#!/bin/sh 

if [ -f contrast.jar ]; then
	mvn initialize
	mvn clean compile war:war cargo:run -PdeployWithContrast
  else 
	echo "Contrast is a commercial product, so you need a licensed version of Contrast in order to run it on this application. If you have access to Contrast, download the Contrast Agent for Java (contrast.jar) from the Team Server and put it into the base directory (xxetest), and then rerun this script."

fi
