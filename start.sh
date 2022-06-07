touch run.pid
java -jar $1 &
echo $! > run.pid
