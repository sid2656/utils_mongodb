#!/bin/bash 删除两天前的数据
targetpath='/usr/local/mongodbcp/databases'
nowtime=$(date -d '-2 days' "+%Y%m%d")
if [ -d "${targetpath}/" ]
then
  rm -rf "${targetpath}/"
  echo "=======${targetpath}/===删除完毕=="
fi
echo "===$nowtime ==="