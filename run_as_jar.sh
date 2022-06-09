filename=$(echo "$1" | cut -c 7-)
java -jar $1 > "Test Output/out - $filename.txt" 2> "Test Output/err - $filename.txt" &
echo $! > run.pid