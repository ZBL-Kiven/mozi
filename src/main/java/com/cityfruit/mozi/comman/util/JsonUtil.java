package com.cityfruit.mozi.comman.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author tianyuheng
 * @date 2020/02/06
 */
public class JsonUtil {

    /**
     * JSON 文件读取
     *
     * @param jsonFileName JSON 文件名
     * @return JSON 对象
     * @throws IOException 文件读取异常
     */
    public static JSONObject getJsonFromFile(String jsonFileName) throws IOException {
        // 读取文件
        File jsonFile = new File(jsonFileName);
        FileReader fileReader = new FileReader(jsonFile);
        // 读取文件内容
        Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
        int ch;
        StringBuilder stringBuilder = new StringBuilder();
        while ((ch = reader.read()) != -1) {
            stringBuilder.append((char) ch);
        }
        fileReader.close();
        reader.close();
        // JSON 编码
        return JSON.parseObject(stringBuilder.toString(), Feature.OrderedField);
    }

    /**
     * JSON 文件写入、保存
     *
     * @param jsonObject   JSONObject
     * @param jsonFileName JSON 文件名
     */
    public static void saveJsonFile(JSONObject jsonObject, String jsonFileName) throws IOException {
        File file = new File(jsonFileName);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
        // 以 JSON 字符串形式写入
        bufferedWriter.write(jsonObject.toJSONString());
        bufferedWriter.close();
    }

    /**
     * 从 HTTP 请求结果中获取 BUG 的 JSON 对象
     *
     * @param result "/bug-view-{}.json" 的 HTTP 请求结果
     * @return BUG 的 JSONObject
     */
    public static JSONObject getBugFromResultString(String result) {
        String data = (String) JSONObject.parseObject(result, Feature.OrderedField).get("data");
        return JSONObject.parseObject(data, Feature.OrderedField);
    }

}
