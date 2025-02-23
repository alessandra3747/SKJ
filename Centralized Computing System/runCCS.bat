@echo off
set /p port=Podaj numer portu: 

echo Uruchamianie programu...
java -jar CCS.jar %port%
pause
