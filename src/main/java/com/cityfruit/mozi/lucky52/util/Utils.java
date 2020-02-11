package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.BugConstants;
import com.cityfruit.mozi.lucky52.constant.FilePath;
import com.cityfruit.mozi.lucky52.constant.JsonField;
import com.cityfruit.mozi.lucky52.entity.Bug;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
@Slf4j
public class Utils {

    /**
     * 校验日期是否为今天
     * 若不是今天，将时间戳更新为今日零点时间戳，清零 VP 统计
     */
    public static void checkDate(JSONObject jsonScore) {
        JSONObject jsonMembers = jsonScore.getJSONObject(JsonField.MEMBERS);
        // 判断得分统计是否为当天统计
        long todayTimeMillis = DateUtil.getTodayTimeMillis();
        // json 文件数据非当天数据，更新时间戳，数据清零
        if (jsonScore.getLongValue(JsonField.TS) < todayTimeMillis) {
            jsonScore.put(JsonField.TS, todayTimeMillis);
            for (String member : jsonMembers.keySet()) {
                jsonMembers.getJSONObject(member).put(JsonField.VALUE_POINT, 0);
            }
            // 存储
            JsonUtil.saveJsonFile(jsonScore, FilePath.SCORE_JSON_FILE);
        }
    }

    /**
     * 更新 VP 值并保存
     *
     * @param jsonScore  score.json JSONObject
     * @param bug        要校验的 BUG 对象
     * @param actionType BUG 操作类型
     */
    public static void updateValuePointAndSave(JSONObject jsonScore, Bug bug, String actionType) {
        JSONObject jsonMembers = jsonScore.getJSONObject(JsonField.MEMBERS);
        // 校验是否为有效创建/关闭
        if (bug.checkValid(actionType, jsonMembers)) {
            // 判断创建/关闭，获取加分成员
            JSONObject jsonMember = (actionType.equals(BugConstants.ACTION_TYPE_CONFIRM)) ?
                    jsonMembers.getJSONObject(bug.getOpenedBy()) : jsonMembers.getJSONObject(bug.getClosedBy());
            float valuePoint = jsonMember.getFloatValue(JsonField.VALUE_POINT);
            // 增加相应 VP 值
            float valuePointAdd = BugConstants.VALUE_POINTS.get(actionType).get(bug.getSeverity());
            valuePoint += valuePointAdd;
            // todo 分数超过 50 推送
            jsonMember.put(JsonField.VALUE_POINT, valuePoint);
            log.info("{} {} 获得 Value Point：{} 分，当前总共 {} 分。\nBug 详情：{}", jsonMember.getString(JsonField.NAME), actionType, valuePointAdd, valuePoint, bug);
            // 保存 json 文件
            JsonUtil.saveJsonFile(jsonScore, FilePath.SCORE_JSON_FILE);
        }
    }

}
