@echo off 
echo Starting Plants vs Zombies... 
java -jar PlantsVsZombiesGame.jar 
if errorlevel 1 ( 
    echo ERROR: Java is required to run this game. 
    echo Download Java from: https://java.com/download 
    pause 
) 
