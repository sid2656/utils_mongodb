#!/bin/bash
sourcepath='/usr/local/mongodb2.6.3/bin'
targetpath='/usr/local/mongodbcp/databases'
jarpath='/usr/local/compress/'
ip='192.168.5.170'
port='27000'
nowtime=$(date +%Y%m%d)
deletetime=$(date -d"7 day ago" +"%Y%m%d")
 
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
    ${jarpath}linuxcompress.sh
    nohup java -server -Xms256m -Xmx2048m -Dfile.encoding=GBK -XX:+UseParallelGC -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -cp ${jarpath}classes:${jarpath}compress-0.0.1-SNAPSHOT.jar com.hdsx.taxi.upa.compress.App ${deletetime} >/dev/null 2>/dev/null &
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