mvn compile exec:java -Dexec.mainClass="za.co.wethinkcode.robotworlds.$1" -Dexec.args="$2" > "Test Output/out - $1 $2.txt" 2> "Test Output/err - $1 $2.txt" &
echo $! > run.pid
