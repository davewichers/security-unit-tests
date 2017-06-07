call mvn clean
call mvn compile war:war 
call mvn cargo:start 
call mvn cargo:run < Nul
call mvn cargo:stop