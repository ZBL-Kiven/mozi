package com.cityfruit.mozi.lucky52.entity;

import lombok.Data;

/**
 * 每个用户使用 @eva 三个命令的次数
 */
@Data
public class MemberOperation {
    /**
     * 姓名
     */
    private String name;
    /**
     * 当天“@eva 我要中奖”次数
     */
    private int dailyOpenTreasureBox = 0;
    /**
     * 总共“@eva 我要中奖”次数
     */
    private int totalOpenTreasureBox = 0;
    /**
     * “@eva 我的 QP 得分”
     */
    private int dailyGetQualityPoint = 0;
    private int totalGetQualityPoint = 0;
    /**
     * “@eva 我的碎片数”
     */
    private int dailyGetQualityFragment = 0;
    private int totalGetQualityFragment = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDailyOpenTreasureBox() {
        return dailyOpenTreasureBox;
    }

    public void setDailyOpenTreasureBox(int dailyOpenTreasureBox) {
        this.dailyOpenTreasureBox = dailyOpenTreasureBox;
    }

    public int getTotalOpenTreasureBox() {
        return totalOpenTreasureBox;
    }

    public void setTotalOpenTreasureBox(int totalOpenTreasureBox) {
        this.totalOpenTreasureBox = totalOpenTreasureBox;
    }

    public int getDailyGetQualityPoint() {
        return dailyGetQualityPoint;
    }

    public void setDailyGetQualityPoint(int dailyGetQualityPoint) {
        this.dailyGetQualityPoint = dailyGetQualityPoint;
    }

    public int getTotalGetQualityPoint() {
        return totalGetQualityPoint;
    }

    public void setTotalGetQualityPoint(int totalGetQualityPoint) {
        this.totalGetQualityPoint = totalGetQualityPoint;
    }

    public int getDailyGetQualityFragment() {
        return dailyGetQualityFragment;
    }

    public void setDailyGetQualityFragment(int dailyGetQualityFragment) {
        this.dailyGetQualityFragment = dailyGetQualityFragment;
    }

    public int getTotalGetQualityFragment() {
        return totalGetQualityFragment;
    }

    public void setTotalGetQualityFragment(int totalGetQualityFragment) {
        this.totalGetQualityFragment = totalGetQualityFragment;
    }
}
