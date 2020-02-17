# 墨子业务中台
初步构想将墨子业务整合至一个项目中，目前只有 `斯巴达幸运 52`。  
## 斯巴达幸运 52  
### 运行依赖
项目目录下的 `score.json` 和 `records.json` 文件。  
### 环境变量
```
# json 文件目录  
# 例如 score.json 文件绝对路径：/Users/cityfruit/Documents/Workspace/mozi/score.json  
# 则 JSON_FILE_PATH 的值为：/Users/cityfruit/Documents/Workspace/mozi  
JSON_FILE_PATH  
```
### 打包
```shell script  
mvn clean package  
```
### 运行
```shell script  
nohup java -jar ./target/mozi-0.0.1-SNAPSHOT.jar > log.txt &  
```
### 监听端口
`9877`
### 其他配置
#### 禅道
禅道 -> 后台 -> 通知 -> Webhook -> 添加 Webhook  
- 类型：其他  
- Hook 地址：此服务的 `/mozi/v1/lucky-52/zentao-notice` 地址  
#### 机器人 EVA
- 机器人 EVA 的环境变量 `OPEN_TREASURE_BOX_URL`：此服务的 `/mozi/v1/lucky-52/open-treasure-box` 地址  