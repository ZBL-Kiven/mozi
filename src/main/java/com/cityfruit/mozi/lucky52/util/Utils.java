package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.FilePath;

import java.io.IOException;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
public class Utils {

    /**
     * 校验日期是否为今天
     * 若不是今天，将时间戳更新为今日零点时间戳，清零 VP 统计
     */
    public static void checkDate(JSONObject jsonScore) {
        JSONObject jsonMembers = jsonScore.getJSONObject("members");
        // 判断得分统计是否为当天统计
        long todayTimeMillis = DateUtil.getTodayTimeMillis();
        // json 文件数据非当天数据，更新时间戳，数据清零
        if (jsonScore.getLongValue("ts") < todayTimeMillis) {
            jsonScore.put("ts", todayTimeMillis);
            for (String member : jsonMembers.keySet()) {
                jsonMembers.getJSONObject(member).put("valuePoint", 0);
            }
            // 存储
            try {
                JsonUtil.saveJsonFile(jsonScore, FilePath.SCORE_JSON_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
