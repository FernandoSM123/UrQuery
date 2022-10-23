@echo off
cls

javac -cp WEB-INF\lib\*; -d WEB-INF\classes WEB-INF\src\*.java

jar -cvf urquery.war *

:: cd C:\Program Files\Apache Software Foundation\Tomcat 10.0\bin 
:: catalina.bat start