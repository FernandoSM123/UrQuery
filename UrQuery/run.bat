::iniciar prolog
cd Prolog
start cmd /k swipl prologServer.pl

::iniciar java
cd..
cd Java
set CLASSPATH=.;classes;lib\annotations-api.jar;lib\ecj-4.6.3.jar;lib\tomcat-dbcp.jar;lib\tomcat-embed-core.jar;lib\tomcat-embed-el.jar;lib\tomcat-embed-jasper.jar;lib\tomcat-embed-websocket.jar;lib\gson-2.9.1.jar;lib\dom-2.3.0-jaxb-1.0.6

@echo off
javac -cp %CLASSPATH% -d classes src\*.java
set PORT = 8081

@echo on
java -cp %CLASSPATH% Main %PORT%