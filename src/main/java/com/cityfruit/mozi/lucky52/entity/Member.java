package com.cityfruit.mozi.lucky52.entity;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
@Data
public class Member {

    /**
     * 姓名
     */
    private String name;

    /**
     * 禅道 ID
     */
    private String zentaoId;

    /**
     * 倍洽 ID
     */
    private String bearyChatId;

    /**
     * Quality Point
     */
    private float qualityPoint;

    /**
     * 有效创建不同级别的 BUG 数量
     */
    private Map<String, Integer> open = new HashMap<>(4);

    /**
     * 有效关闭不同级别的 BUG 数量
     */
    private Map<String, Integer> close = new HashMap<>(4);

    /**
     * 今日创建的未确认的 BUG
     */
    private List<String> openedBugs = new ArrayList<>(4);

    /**
     * 当日是否开过宝箱
     * 1：开过；0：没开过
     */
    private boolean opened;

    private int qualityFragment;

    public Member(JSONObject jsonMember) {
        name = jsonMember.getString(JsonKeysConst.NAME);
        zentaoId = jsonMember.getString(JsonKeysConst.ZENTAO_ID);
        bearyChatId = jsonMember.getString(JsonKeysConst.BEARY_CHAT_ID);
        qualityPoint = jsonMember.getFloatValue(JsonKeysConst.QUALITY_POINT);
        open.put("1", jsonMember.getJSONObject(JsonKeysConst.OPEN).getInteger(JsonKeysConst.S1));
        open.put("2", jsonMember.getJSONObject(JsonKeysConst.OPEN).getInteger(JsonKeysConst.S2));
        open.put("3", jsonMember.getJSONObject(JsonKeysConst.OPEN).getInteger(JsonKeysConst.S3));
        open.put("4", jsonMember.getJSONObject(JsonKeysConst.OPEN).getInteger(JsonKeysConst.S4));
        close.put("1", jsonMember.getJSONObject(JsonKeysConst.CLOSE).getInteger(JsonKeysConst.S1));
        close.put("2", jsonMember.getJSONObject(JsonKeysConst.CLOSE).getInteger(JsonKeysConst.S2));
        close.put("3", jsonMember.getJSONObject(JsonKeysConst.CLOSE).getInteger(JsonKeysConst.S3));
        close.put("4", jsonMember.getJSONObject(JsonKeysConst.CLOSE).getInteger(JsonKeysConst.S4));
        for (Object bugId : jsonMember.getJSONArray(JsonKeysConst.OPENED_BUGS)) {
            openedBugs.add(bugId.toString());
        }
        opened = jsonMember.getBoolean(JsonKeysConst.OPENED);
        qualityFragment = jsonMember.getIntValue(JsonKeysConst.QUALITY_FRAGMENT);
    }

}
