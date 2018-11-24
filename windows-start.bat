@echo off
@title Bash Launcher

set Looping=True

:START
if %Looping% == True (
    goto START_MENU
)

:START_MENU
echo.
echo Bash Launcher
echo Select an option below to continue:
echo.
echo    1. Start Bash.
echo    2. Start Bash with auto-purge.
echo    3. Re-download latest version from GitHub.
echo    9. Exit program.
echo.

set /p in="Enter your option: "

if %in% == 1 (
    goto START_BOT
)
if %in% == 2 (
    goto START_WITH_PURGE
)
if %in% == 3 (
    goto UPDATE_BOT
)
if %in% == 9 (
    goto EXIT_PROGRAM
) else (
    echo.
    echo     Invalid option given
    goto START
)


:START_BOT
java -jar Bash.jar
Looping=False
goto EXIT_PROGRAM

:START_WITH_PURGE
java -jar Bash.jar -purge
Looping=False
goto EXIT_PROGRAM

:UPDATE_BOT
git pull
echo.
echo Updated Bash from latest GitHub commit on branch: master.
echo.
if %Looping% == True (
    goto START_MENU
)

:EXIT_PROGRAM
exit