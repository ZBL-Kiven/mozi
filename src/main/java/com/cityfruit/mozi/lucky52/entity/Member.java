package com.cityfruit.mozi.lucky52.entity;

import com.alibaba.fastjson.JSONObject;
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
     * Value Point
     */
    private String valuePoint;

    public Member(JSONObject jsonMember) {
        name = jsonMember.getString("name");
        valuePoint = jsonMember.getString("valuePoint");
    }

}
