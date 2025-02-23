@echo off

set /p port=Podaj numer portu: 

set /p number=Podaj liczbe: 

::if not exist DAS.class (
::    echo Kompilacja programu...
::    javac DAS.java
::)

echo Uruchamianie programu...
java DAS %port% %number%
pause
