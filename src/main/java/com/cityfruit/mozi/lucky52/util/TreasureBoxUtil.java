package com.cityfruit.mozi.lucky52.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tianyuheng
 * @date 2020/02/12
 */
public class TreasureBoxUtil {

    public static final Float VP_50 = 50f;

    public static final Float VP_60 = 60f;

    public static final Float VP_80 = 80f;

    public static final Float VP_100 = 100f;

    public static final Float VP_120 = 120f;

    /**
     * 不同概率推送内容
     */
    public static final Map<Float, String> PROBABILITY_NAME;

    static {
        PROBABILITY_NAME = new HashMap<>();
        PROBABILITY_NAME.put(VP_50, "有很小概率");
        PROBABILITY_NAME.put(VP_60, "有概率");
        PROBABILITY_NAME.put(VP_80, "有一定概率");
        PROBABILITY_NAME.put(VP_100, "有极大概率");
        PROBABILITY_NAME.put(VP_120, "一定");
    }

    /**
     * 根据得分前后的分数获取 Value Point 对应的概率名称
     *
     * @param beforeValuePoint 得分前分数
     * @param nowValuePoint    现在的分数
     * @return 获取成功：概率名称；获取失败：空
     */
    public static String getProbabilityName(float beforeValuePoint, float nowValuePoint) {
        Float valuePoint = getValuePoint(beforeValuePoint, nowValuePoint);
        return (valuePoint == 0f) ? null : PROBABILITY_NAME.get(valuePoint);
    }

    /**
     * 判断区间，用于调用 Map
     *
     * @param beforeValuePoint 得分前分数
     * @param nowValuePoint    现在的分数
     * @return 获取成功：概率值；获取失败：0f
     */
    private static Float getValuePoint(float beforeValuePoint, float nowValuePoint) {
        if ((beforeValuePoint < VP_50) && (nowValuePoint >= VP_50)) {
            return VP_50;
        } else if ((beforeValuePoint < VP_60) && (nowValuePoint >= VP_60)) {
            return VP_60;
        } else if ((beforeValuePoint < VP_80) && (nowValuePoint >= VP_80)) {
            return VP_80;
        } else if ((beforeValuePoint < VP_100) && (nowValuePoint >= VP_100)) {
            return VP_100;
        } else if ((beforeValuePoint < VP_120) && (nowValuePoint >= VP_120)) {
            return VP_120;
        } else {
            return 0f;
        }
    }

}
