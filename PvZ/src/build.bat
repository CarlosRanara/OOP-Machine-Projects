@echo off
echo ===============================================
echo   Plants vs Zombies - Build with Music
echo ===============================================
echo.

REM Check if music file exists
if not exist "music.wav" (
    echo WARNING: music.wav not found!
    echo The game will work but won't have background music.
    echo.
    choice /C YN /M "Continue building without music"
    if errorlevel 2 exit /b 1
)

REM Create build directory
if not exist "build" mkdir build
if not exist "dist" mkdir dist

echo [1/5] Compiling Java source code...
javac -d build PlantsVsZombiesGame.java
if errorlevel 1 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

REM Copy music to build directory if it exists
if exist "music.wav" (
    echo [2/5] Adding music to package...
    copy music.wav build\
    echo Music file included: music.wav
) else (
    echo [2/5] Skipping music (file not found)
)

echo [3/5] Creating JAR with resources...
cd build
echo Main-Class: PlantsVsZombiesGame > manifest.txt

REM Create JAR with or without music
if exist "music.wav" (
    jar cfm ..\dist\PlantsVsZombiesGame.jar manifest.txt *.class music.wav
    echo JAR created with embedded music
) else (
    jar cfm ..\dist\PlantsVsZombiesGame.jar manifest.txt *.class
    echo JAR created without music
)
cd ..

echo [4/5] Creating distribution package...
if not exist "PlantsVsZombies-Game" mkdir "PlantsVsZombies-Game"
copy dist\PlantsVsZombiesGame.jar "PlantsVsZombies-Game\"

REM Also include external music file as backup/option
if exist "music.wav" (
    copy music.wav "PlantsVsZombies-Game\"
    echo External music.wav also included for user customization
)

echo [5/5] Creating launcher...
echo @echo off > "PlantsVsZombies-Game\run.bat"
echo echo Starting Plants vs Zombies... >> "PlantsVsZombies-Game\run.bat"
echo java -jar PlantsVsZombiesGame.jar >> "PlantsVsZombies-Game\run.bat"
echo if errorlevel 1 ( >> "PlantsVsZombies-Game\run.bat"
echo     echo ERROR: Java is required to run this game. >> "PlantsVsZombies-Game\run.bat"
echo     echo Download Java from: https://java.com/download >> "PlantsVsZombies-Game\run.bat"
echo     pause >> "PlantsVsZombies-Game\run.bat"
echo ) >> "PlantsVsZombies-Game\run.bat"

echo.
echo BUILD COMPLETE!
echo.
if exist "music.wav" (
    echo JAR file created WITH embedded music
) else (
    echo JAR file created WITHOUT music
)
echo.
echo Distribution folder: PlantsVsZombies-Game\
echo.

choice /C YN /M "Test the game now"
if errorlevel 1 (
    echo Testing game...
    cd "PlantsVsZombies-Game"
    start run.bat
    cd ..
)

echo Ready for distribution!
pause