echo "开始同步代码"
git pull
echo "开始编译项目"
mvn clean package
echo "开始运行项目"
nohup java -jar ./target/mozi-0.0.1-SNAPSHOT.jar > log.txt &
