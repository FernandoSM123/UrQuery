#!/bin/bash
set +v
export CLASSPATH=.:classes:lib/annotations-api.jar:lib/ecj-4.6.3.jar:lib/tomcat-dbcp.jar:lib/tomcat-embed-core.jar:lib/tomcat-embed-el.jar:lib/tomcat-embed-jasper.jar:lib/tomcat-embed-websocket.jar:lib/gson-2.9.1.jar

javac -cp $CLASSPATH -d classes src/*.java

export PORT=8081
 
java -cp $CLASSPATH Main $PORT