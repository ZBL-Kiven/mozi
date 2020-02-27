package com.cityfruit.mozi.lucky52.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

    private TaskStatus status = new TaskStatus();

    public static Member toMember(String json) {
        return JSON.parseObject(json, Member.class);
    }

    public static Member toMember(JSONObject json) {
        return toMember(json.toJSONString());
    }

}
