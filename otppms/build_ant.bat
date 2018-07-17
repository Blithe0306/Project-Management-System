@echo off
@rem build otppms

echo ====================
echo ===== OTPPMS   =====
echo ====================
echo build OTPPMS begin

@echo off
set mon=%DATE:~5,2%
set day=%DATE:~8,2%
set year=%DATE:~0,4%

pushd %~dp0\

set yymmdd=%year%%mon%%day%

copy c:\otp\packagehelper\web\jsp-api.jar %~dp0\lib\.
copy c:\otp\packagehelper\web\servlet-api.jar %~dp0\lib\.

call ant -buildfile build.xml
if not exist otppms-%yymmdd%.zip echo build OTPPMS FAILED!
if not exist otppms-%yymmdd%.zip goto ERROR

goto OK

:ERROR
echo A error occur...
pause
goto END

:OK
echo.
echo ==============================================
echo ===== build otppms with successful  =====
echo ==============================================

:END
if not "%1" == "package" pause