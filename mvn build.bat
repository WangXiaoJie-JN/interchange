@Echo "start build ---"
@Call mvn clean install -Dmaven.test.skip=true
@Echo "end"

pause 
