touch run.pid
touch out.txt
mvn compile exec:java -Dexec.mainClass="za.co.wethinkcode.robotworlds.$1" -Dexec.args="$2"> out.txt 2> err.txt &
echo $! > run.pid
