:: javac -cp WEB-INF\lib\*; -d WEB-INF\classes WEB-INF\src\*.java

:: jar -cvf urquery.war *

:: cd C:\Program Files\Apache Software Foundation\Tomcat 10.0\bin 
:: catalina.bat start


set CLASSPATH=.;classes;lib\annotations-api.jar;lib\ecj-4.6.3.jar;lib\tomcat-dbcp.jar;lib\tomcat-embed-core.jar;lib\tomcat-embed-el.jar;lib\tomcat-embed-jasper.jar;lib\tomcat-embed-websocket.jar;lib\gson-2.9.1.jar;lib\dom-2.3.0-jaxb-1.0.6

@echo off
javac -cp %CLASSPATH% -d classes src\*.java
set PORT = 8081

@echo on
java -cp %CLASSPATH% Main %PORT%