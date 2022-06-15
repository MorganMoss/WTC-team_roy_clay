mvn compile exec:java -Dexec.mainClass="za.co.wethinkcode.robotworlds.$1" -Dexec.args="$2" > "$3/out - $1 $2.txt" 2> "$3/err - $1 $2.txt" &
echo $! > run.pid
