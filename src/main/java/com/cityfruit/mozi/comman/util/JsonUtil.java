package com.cityfruit.mozi.comman.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
     * @param fileName JSON 文件名
     * @return JSONObject
     */
    public static JSONObject getJsonObjectFromFile(String fileName) {
        return JSON.parseObject(getStringFromFile(fileName), Feature.OrderedField);
    }

    /**
     * JSON 文件读取
     *
     * @param fileName JSON 文件名
     * @return JSONObject
     */
    public static JSONArray getJsonArrayFromFile(String fileName) {
        return JSON.parseArray(getStringFromFile(fileName));
    }

    private static String getStringFromFile(String fileName) {
        // 读取文件
        File jsonFile = new File(fileName);
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
        return stringBuilder.toString();
    }

    /**
     * JSON 文件写入、保存
     *
     * @param jsonObject   JSONObject
     * @param fileName JSON 文件名
     */
    public static void saveJsonFile(JSONObject jsonObject, String fileName) {
        BufferedWriter bufferedWriter = openFile(fileName);
        // 以 JSON 字符串形式写入
        try {
            assert bufferedWriter != null;
            bufferedWriter.write(jsonObject.toJSONString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * JSON 文件写入、保存
     *
     * @param jsonArray   JSONArray
     * @param fileName JSON 文件名
     */
    public static void saveJsonFile(JSONArray jsonArray, String fileName) {
        BufferedWriter bufferedWriter = openFile(fileName);
        // 以 JSON 字符串形式写入
        try {
            assert bufferedWriter != null;
            bufferedWriter.write(jsonArray.toJSONString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedWriter openFile(String fileName) {
        File file = new File(fileName);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bufferedWriter;
    }


}
