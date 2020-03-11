package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;

public class OperationsUtil {

    public static final String OPERATION_OPEN_TREASURE_BOX = "openTreasureBox";
    public static final String OPERATION_GET_QUALITY_POINT = "getQualityPoint";
    public static final String OPERATION_GET_QUALITY_FRAGMENT = "getQualityFragment";

    /**
     * 添加操作记录
     *
     * @param name      姓名
     * @param operation 操作
     */
    public static void addOperation(String name, String operation) {
        JSONArray jsonOperations = JsonUtil.getJsonArrayFromFile(FilePathConst.OPERATIONS_JSON_FILE);
        JSONObject jsonOperation = new JSONObject(true);
        jsonOperation.put(JsonKeysConst.TS, System.currentTimeMillis());
        jsonOperation.put(JsonKeysConst.NAME, name);
        jsonOperation.put(JsonKeysConst.OPERATION, operation);
        jsonOperations.add(jsonOperation);
        JsonUtil.saveJsonFile(jsonOperations, FilePathConst.OPERATIONS_JSON_FILE);
    }

}
