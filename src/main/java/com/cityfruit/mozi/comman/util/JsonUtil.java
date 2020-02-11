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
     */
    public static JSONObject getJsonFromFile(String jsonFileName) {
        // 读取文件
        File jsonFile = new File(jsonFileName);
        FileReader fileReader = null;
        Reader reader = null;
        try {
            fileReader = new FileReader(jsonFile);
            reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 读取文件内容
        int ch;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            assert reader != null;
            while ((ch = reader.read()) != -1) {
                stringBuilder.append((char) ch);
            }
            fileReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // JSON 编码
        return JSON.parseObject(stringBuilder.toString(), Feature.OrderedField);
    }

    /**
     * JSON 文件写入、保存
     *
     * @param jsonObject   JSONObject
     * @param jsonFileName JSON 文件名
     */
    public static void saveJsonFile(JSONObject jsonObject, String jsonFileName) {
        File file = new File(jsonFileName);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 以 JSON 字符串形式写入
        try {
            assert bufferedWriter != null;
            bufferedWriter.write(jsonObject.toJSONString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
