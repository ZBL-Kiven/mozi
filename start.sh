#!/bin/sh

# shellcheck disable=SC2006
IDD=`lsof -i:9877 | awk '{print $2}'`
echo $IDD
for id in $IDD
do
kill $id
done
echo "关闭服务完成"
IDD=`lsof -i:9877 | awk '{print $2}'`
echo $IDD

echo "开始同步代码"
git pull

echo "开始打包项目"
mvn clean package

echo "开始运行项目"
nohup java -jar ./target/mozi-0.0.1-SNAPSHOT.jar > log.txt &

tail -f log.txt