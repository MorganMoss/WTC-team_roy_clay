filename=$(echo "$1" | cut -c 7-)
java -jar $1 >"$2/out - $filename.txt" 2> "$2/err - $filename.txt" &
echo $! > run.pid