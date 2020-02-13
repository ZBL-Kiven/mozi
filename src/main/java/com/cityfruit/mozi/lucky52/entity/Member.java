package com.cityfruit.mozi.lucky52.entity;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.lucky52.constant.JsonField;
import lombok.Data;

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
     * Value Point
     */
    private String valuePoint;

    public Member(JSONObject jsonMember) {
        name = jsonMember.getString(JsonField.NAME);
        bearyChatId = jsonMember.getString(JsonField.BEARY_CHAT_ID);
        valuePoint = jsonMember.getString(JsonField.VALUE_POINT);
    }

}
