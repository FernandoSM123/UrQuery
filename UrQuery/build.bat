@echo off
cls

javac -cp WEB-INF\lib\*; -d WEB-INF\classes WEB-INF\src\*.java

jar -cvf urquery.war *

:: cd C:\Program Files\Apache Software Foundation\Tomcat 10.0\bin 
:: catalina.bat start

:: set %CLASSPATH%=.;classes;lib\annotations-api.jar;lib\ecj-4.6.3.jar;lib\tomcat-dbcp.jar;lib\tomcat-embed-core.jar;lib\tomcat-embed-el.jar;lib\tomcat-embed-jasper.jar;lib\tomcat-embed-websocket.jar;lib\gson-2.9.1.jar;lib\jakarta-servlet-api-4.jar;lib\jakarta-servlet.jar;lib\servlet-api.jar

:: javac -cp %CLASSPATH% -d classes src\*.java
IF %ERRORLEVEL% NEQ 0 ( 
    EXIT /B %ERRORLEVEL%
)

set PORT=
IF "%~1"=="" set PORT=8080

@echo on
@echo.*** Server will be started up ****
@echo.
@echo.*** type CTRL+C to stop server ***
@timeout 3
 
java -cp %CLASSPATH% com.UrQuery.web.EmbeddedServer %PORT%