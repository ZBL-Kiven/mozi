package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSON;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.entity.RecordInfo;

import java.util.ArrayList;
import java.util.List;

public class RecordUtil {

    public static <R> R listRecord(boolean saved, Fun<List<RecordInfo>, R> callback) {
        List<RecordInfo> infos = listRecord();
        if(infos==null){
            infos = new ArrayList<>();
        }
        R r = callback.exec(infos);
        if (saved) {
            save(infos);
        }
        return r;
    }

    private static List<RecordInfo> listRecord() {
        String content = JsonUtil.getStringFromFile(FilePathConst.RECORDS_JSON_FILE);
        if (StringUtil.isEmpty(content)) {
            return new ArrayList<>();
        }
        return JSON.parseArray(content, RecordInfo.class);
    }

    private static void save(List<RecordInfo> infos) {
        JsonUtil.saveJsonFile(infos, FilePathConst.RECORDS_JSON_FILE);
    }

}
