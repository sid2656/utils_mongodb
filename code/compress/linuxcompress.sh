#!/bin/sh
targetpath='/usr/local/mongodbbak/'
sourcepath='/usr/local/mongodbcp/databases'
list_alldir(){
    for file2 in `ls -a $1`
    do
        if [ x"$file2" != x"." -a x"$file2" != x".." ];then
            if [ -d "$1/$file2" ];then
                tar czvf ${targetpath}$file2.tar.gz $1/$file2
                zip -rqu ${targetpath}$file2.zip $1/$file2
                rm -rf $1/$file2
                list_alldir "$1/$file2"
            fi
        fi
    done
}

list_alldir ${sourcepath}
