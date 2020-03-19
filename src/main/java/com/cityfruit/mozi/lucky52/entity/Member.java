package com.cityfruit.mozi.lucky52.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
import com.cityfruit.mozi.lucky52.enums.Role;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZentaoId() {
        return zentaoId;
    }

    public void setZentaoId(String zentaoId) {
        this.zentaoId = zentaoId;
    }

    public String getBearyChatId() {
        return bearyChatId;
    }

    public void setBearyChatId(String bearyChatId) {
        this.bearyChatId = bearyChatId;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public Map<String, Integer> getOpen() {
        return open;
    }

    public void setOpen(Map<String, Integer> open) {
        this.open = open;
    }

    public Map<String, Integer> getClose() {
        return close;
    }

    public void setClose(Map<String, Integer> close) {
        this.close = close;
    }

    public List<String> getOpenedBugs() {
        return openedBugs;
    }

    public void setOpenedBugs(List<String> openedBugs) {
        this.openedBugs = openedBugs;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public int getZombieCount() {
        return zombieCount;
    }

    public void setZombieCount(int zombieCount) {
        this.zombieCount = zombieCount;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

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
     * 用户类型
     */
    private Role userRole;

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

    private int zombieCount = 0;

    private TaskStatus status = new TaskStatus();

    public static Member create(UserInfo memberBean) {
        //非当天数据，更新时间戳，数据清零，重置开宝箱次数
        Member member = new Member();
        member.setBearyChatId(memberBean.getBearyChatId());
        member.setZentaoId(memberBean.getZentaoId());
        member.setName(memberBean.getName());
        member.setUserRole(memberBean.getUserRole());
        return member;
    }

    @JSONField(serialize = false)
    public float getQualityPoint() {
        return BearyChatConst.calculateQualityPoint(this);
    }

}
