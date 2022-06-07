touch run.pid
touch out.txt
mvn compile exec:java > out.txt 2> err.txt &
echo $! > run.pid
