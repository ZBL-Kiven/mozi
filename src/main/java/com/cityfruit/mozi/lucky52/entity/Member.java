package com.cityfruit.mozi.lucky52.entity;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
     * 倍洽 ID
     */
    private String bearyChatId;

    /**
     * Quality Point
     */
    private float qualityPoint;

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
        bearyChatId = jsonMember.getString(JsonKeysConst.BEARY_CHAT_ID);
        qualityPoint = jsonMember.getFloatValue(JsonKeysConst.QUALITY_POINT);
        for (Object bugId :
                jsonMember.getJSONArray(JsonKeysConst.OPENED_BUGS)) {
            openedBugs.add(bugId.toString());
        }
        opened = jsonMember.getBoolean(JsonKeysConst.OPENED);
        qualityFragment = jsonMember.getIntValue(JsonKeysConst.QUALITY_FRAGMENT);
    }

}
