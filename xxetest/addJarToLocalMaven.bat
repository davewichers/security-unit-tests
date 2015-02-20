set JAVA_HOME=C:\Program Files\Java\jre1.8.0_25
set Path=C:\Program Files\Java\jre1.8.0_25\bin;%PATH%
C:\maven-3.2.3\bin\mvn.bat install:install-file -DgroupId=org.apache.maven.plugins -DartifactId=maven.resources.plugin -Dversion=2.6 -Dpackaging=jar -Dfile=missingjars/maven-resources-plugin-2.6.jar