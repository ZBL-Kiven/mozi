package com.cityfruit.mozi.lucky52.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author tianyuheng
 * @date 2020/02/12
 */
public class TreasureBoxUtil {

    public static final Float QP_50 = 50f;

    public static final Float QP_60 = 60f;

    public static final Float QP_80 = 80f;

    public static final Float QP_100 = 100f;

    public static final Float QP_120 = 120f;

    /**
     * 不同概率推送内容
     */
    private static final Map<Float, String> PROBABILITY_NAME;

    static {
        PROBABILITY_NAME = new HashMap<>();
        PROBABILITY_NAME.put(QP_50, "有很小概率");
        PROBABILITY_NAME.put(QP_60, "有概率");
        PROBABILITY_NAME.put(QP_80, "有一定概率");
        PROBABILITY_NAME.put(QP_100, "有极大概率");
        PROBABILITY_NAME.put(QP_120, "一定");
    }

    /**
     * 开奖概率
     */
    private static final Map<Float, Map<Integer, Integer>> QUALITY_FRAGMENT_MAP;

    static {
        QUALITY_FRAGMENT_MAP = new HashMap<>();
        QUALITY_FRAGMENT_MAP.put(QP_50, new HashMap<Integer, Integer>() {{
            put(0, 0);
            put(1, 0);
            put(2, 0);
            put(3, 0);
            put(4, 0);
            put(5, 1);
            put(6, 1);
            put(7, 1);
            put(8, 1);
            put(9, 1);
        }});
        QUALITY_FRAGMENT_MAP.put(QP_60, new HashMap<Integer, Integer>() {{
            put(0, 1);
            put(1, 1);
            put(2, 1);
            put(3, 1);
            put(4, 1);
            put(5, 1);
            put(6, 1);
            put(7, 1);
            put(8, 1);
            put(9, 2);
        }});
        QUALITY_FRAGMENT_MAP.put(QP_80, new HashMap<Integer, Integer>() {{
            put(0, 1);
            put(1, 1);
            put(2, 1);
            put(3, 1);
            put(4, 1);
            put(5, 1);
            put(6, 2);
            put(7, 2);
            put(8, 2);
            put(9, 2);
        }});
        QUALITY_FRAGMENT_MAP.put(QP_100, new HashMap<Integer, Integer>() {{
            put(0, 1);
            put(1, 1);
            put(2, 1);
            put(3, 1);
            put(4, 1);
            put(5, 2);
            put(6, 2);
            put(7, 2);
            put(8, 2);
            put(9, 2);
        }});
        QUALITY_FRAGMENT_MAP.put(QP_120, new HashMap<Integer, Integer>() {{
            put(0, 2);
            put(1, 2);
            put(2, 2);
            put(3, 2);
            put(4, 2);
            put(5, 2);
            put(6, 2);
            put(7, 2);
            put(8, 2);
            put(9, 2);
        }});
    }

    /**
     * 根据得分前后的分数获取 Quality Point 对应的概率名称
     *
     * @param qualityPoint 分数
     * @return 获取成功：概率名称；获取失败：空
     */
    public static String getProbabilityName(double qualityPoint) {
        if (qualityPoint >= QP_50 && qualityPoint < QP_60) {
            return PROBABILITY_NAME.get(QP_50);
        } else if (qualityPoint >= QP_60 && qualityPoint < QP_80) {
            return PROBABILITY_NAME.get(QP_60);
        } else if (qualityPoint >= QP_80 && qualityPoint < QP_100) {
            return PROBABILITY_NAME.get(QP_80);
        } else if (qualityPoint >= QP_100 && qualityPoint < QP_120) {
            return PROBABILITY_NAME.get(QP_100);
        } else if (qualityPoint >= QP_120) {
            return PROBABILITY_NAME.get(QP_120);
        } else {
            return null;
        }
    }

    /**
     * 根据得分前后的分数获取 Quality Point 对应的概率名称
     *
     * @param beforeQualityPoint 得分前分数
     * @param nowQualityPoint    现在的分数
     * @return 获取成功：概率名称；获取失败：空
     */
    public static String getProbabilityName(double beforeQualityPoint, float nowQualityPoint) {
        if ((beforeQualityPoint < QP_50) && (nowQualityPoint >= QP_50)) {
            return PROBABILITY_NAME.get(QP_50);
        } else if ((beforeQualityPoint < QP_60) && (nowQualityPoint >= QP_60)) {
            return PROBABILITY_NAME.get(QP_60);
        } else if ((beforeQualityPoint < QP_80) && (nowQualityPoint >= QP_80)) {
            return PROBABILITY_NAME.get(QP_80);
        } else if ((beforeQualityPoint < QP_100) && (nowQualityPoint >= QP_100)) {
            return PROBABILITY_NAME.get(QP_100);
        } else if ((beforeQualityPoint < QP_120) && (nowQualityPoint >= QP_120)) {
            return PROBABILITY_NAME.get(QP_120);
        } else {
            return null;
        }
    }

    /**
     * 开宝箱
     *
     * @param qualityPoint QP 分数
     * @return 质量碎片数量
     */
    public static int getQualityFragmentsByQualityPoint(float qualityPoint) {
        int random = new Random().nextInt(10);
        if ((qualityPoint >= QP_50) && (qualityPoint < QP_60)) {
            return QUALITY_FRAGMENT_MAP.get(QP_50).get(random);
        } else if ((qualityPoint >= QP_60) && (qualityPoint < QP_80)) {
            return QUALITY_FRAGMENT_MAP.get(QP_60).get(random);
        } else if ((qualityPoint >= QP_80) && (qualityPoint < QP_100)) {
            return QUALITY_FRAGMENT_MAP.get(QP_80).get(random);
        } else if ((qualityPoint >= QP_100) && (qualityPoint < QP_120)) {
            return QUALITY_FRAGMENT_MAP.get(QP_100).get(random);
        } else if (qualityPoint >= QP_120) {
            return QUALITY_FRAGMENT_MAP.get(QP_120).get(random);
        }
        return 0;
    }

}
