package com.cityfruit.mozi.lucky52.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.cityfruit.mozi.lucky52.bean.MemberBean;
import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
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
    private boolean opened = false;

    private int qualityFragment;

    private int zombieCount = 0;

    private TaskStatus status = new TaskStatus();

    public static Member create(MemberBean memberBean) {
        //非当天数据，更新时间戳，数据清零，重置开宝箱次数
        Member member = new Member();
        member.setBearyChatId(memberBean.getBearyChatId());
        member.setZentaoId(memberBean.getZentaoId());
        member.setName(memberBean.getName());
        return member;
    }

    @JSONField(serialize = false)
    public float getQualityPoint() {
        return BearyChatConst.calculateQualityPoint(this);
    }

}
