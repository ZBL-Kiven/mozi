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
}
