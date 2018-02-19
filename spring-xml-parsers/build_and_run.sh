# get running dir so we can run from anywhere
this_script=`readlink -f $0`
this_dir=`dirname $0`

mvn -f $this_dir/pom.xml clean package
java -jar $this_dir/target/spring-xml-parsers-0.1.0.war

