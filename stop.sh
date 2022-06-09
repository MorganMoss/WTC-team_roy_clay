kill "$(cat run.pid)"
if [ $? -eq 1 ]
then
  echo "[1;31mJava Process crashed before closing[m"
else
  echo "[1;37mClosed Running Java Process[m"
fi
rm run.pid
