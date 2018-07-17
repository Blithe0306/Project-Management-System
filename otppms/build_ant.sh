echo =========================
echo ===== OTPCENTER   =====
echo =========================
echo build OTPCENTER begin

cp -f /y/web/*.jar ./lib/.

ant -buildfile build.xml
if [ $? -ne 0 ]
then
    echo "build otpcenter error!" 
    exit 1
fi

echo 
echo ==============================================
echo ===== build OTPCENTER with successful  =====
echo ==============================================
