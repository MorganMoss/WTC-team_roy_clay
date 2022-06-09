touch run.pid
touch out.txt
java -jar $1 > out.txt 2> err.txt &
echo $! > run.pid
