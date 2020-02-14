package com.cityfruit.mozi.lucky52.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import com.cityfruit.mozi.lucky52.parameter.BearyChatRequestParam;
import com.cityfruit.mozi.lucky52.service.TreasureBoxService;
import com.cityfruit.mozi.lucky52.util.TreasureBoxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author tianyuheng
 * @date 2020/02/13
 */
@Slf4j
@Service
public class TreasureBoxServiceImpl implements TreasureBoxService {

    /**
     * 开宝箱
     *
     * @param bearyChatRequestParam BC 机器人请求参数
     * @return BC 机器人响应参数
     */
    @Override
    public String openTreasureBox(BearyChatRequestParam bearyChatRequestParam) {
        // 群组判断
        if (!bearyChatRequestParam.checkChannel(BearyChatConst.OPEN_TREASURE_BOX_GROUP)) {
            return BearyChatConst.INVALID_GROUP;
        }
        String bearyChatName = bearyChatRequestParam.getUser_name();
        // 条件：BC 姓名相等、分数 >= 50 todo Repository
        JSONObject jsonScore = JsonUtil.getJsonObjectFromFile(FilePathConst.SCORE_JSON_FILE);
        JSONObject jsonMembers = jsonScore.getJSONObject(JsonKeysConst.MEMBERS);
        JSONObject jsonMember = null;
        for (String memberName : jsonMembers.keySet()) {
            // 姓名相同且 QP 值满足最低开宝箱要求
            if (jsonMembers.getJSONObject(memberName).getFloatValue(JsonKeysConst.QUALITY_POINT) >= TreasureBoxUtil.QP_50
                    && jsonMembers.getJSONObject(memberName).getString(JsonKeysConst.BEARY_CHAT_ID).equals(bearyChatName)) {
                jsonMember = jsonMembers.getJSONObject(memberName);
            }
        }
        // 此姓名成员的 QP 值满足要求，开宝箱
        if (jsonMember != null) {
            // 判断是否已经开过宝箱
            if (jsonMember.getBoolean(JsonKeysConst.OPENED)) {
                return BearyChatConst.opened(bearyChatName);
            } else {
                // 获取开宝箱结果
                int qualityFragment = TreasureBoxUtil.getQualityFragmentsByQualityPoint(jsonMember.getFloatValue(JsonKeysConst.QUALITY_POINT));
                // 标记已开宝箱
                jsonMember.put(JsonKeysConst.OPENED, true);
                // 增加品质碎片
                jsonMember.put(JsonKeysConst.QUALITY_FRAGMENT, jsonMember.getIntValue(JsonKeysConst.QUALITY_FRAGMENT) + qualityFragment);
                // 保存数据
                JsonUtil.saveJsonFile(jsonScore, FilePathConst.SCORE_JSON_FILE);
                // 开宝箱记录
                JSONArray jsonRecords = JsonUtil.getJsonArrayFromFile(FilePathConst.RECORDS_JSON_FILE);
                JSONObject jsonRecord = new JSONObject(true);
                jsonRecord.put(JsonKeysConst.TS, System.currentTimeMillis());
                jsonRecord.put(JsonKeysConst.NAME, jsonMember.getString(JsonKeysConst.NAME));
                jsonRecord.put(JsonKeysConst.QUALITY_POINT, jsonMember.getString(JsonKeysConst.QUALITY_POINT));
                jsonRecord.put(JsonKeysConst.OPENED, true);
                jsonRecord.put(JsonKeysConst.QUALITY_FRAGMENT, qualityFragment);
                jsonRecord.put(JsonKeysConst.EXCHANGED, false);
                jsonRecords.add(jsonRecord);
                JsonUtil.saveJsonFile(jsonRecords, FilePathConst.RECORDS_JSON_FILE);
                log.info("{}，打开了宝箱，获得了 {} 个品质碎片", jsonMember.getString(JsonKeysConst.NAME), qualityFragment);
                // 返回推送内容
                return BearyChatConst.openTreasureBoxResult(bearyChatName, qualityFragment);
            }
        }
        // 暂时没有开宝箱资格
        return BearyChatConst.noOpeningRights(bearyChatName);
    }

}
