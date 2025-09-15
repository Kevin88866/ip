@ECHO OFF

REM create bin directory if it doesn't exist
if exist ..\bin rmdir /s /q ..\bin
mkdir ..\bin

REM delete output from previous run
del ACTUAL.TXT
REM compile all Java files under src/main/java into bin folder
for /R ..\src\main\java %%f in (*.java) do (
    javac -cp ..\src\main\java -Xlint:none -d ..\bin "%%f"
)
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)
REM no error here, errorlevel == 0

REM run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -cp ..\bin kiki.ui.kiki < input.txt > ACTUAL.TXT

REM compare the output to the expected output
FC ACTUAL.TXT EXPECTED.TXT
