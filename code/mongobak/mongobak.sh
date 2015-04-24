#!/bin/bash
sourcepath='/usr/local/mongodb2.6.3/bin'
targetpath='/usr/local/mongodbcp/databases'
jarpath='/usr/local/compress/'
ip='192.168.5.130'
port='27000'
nowtime=$(date +%Y%m%d)
 
start()
{
  ${sourcepath}/mongodump --host ${ip} --port ${port} --out ${targetpath}
}
execute()
{
  start
  if [ $? -eq 0 ]
  then
    echo "back successfully!"
    nohup java -server -Xms256m -Xmx2048m -Dfile.encoding=GBK -XX:+UseParallelGC -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -cp ${jarpath}classes:compress-0.0.1-SNAPSHOT.jar com.hdsx.taxi.upa.compress.App ${nowtime} >/dev/null 2>/dev/null &
  else
    echo "back failure!"
  fi
}
 
if [ ! -d "${targetpath}/" ]
then
 mkdir ${targetpath}
fi
execute
echo "============== back end ${nowtime} =============="