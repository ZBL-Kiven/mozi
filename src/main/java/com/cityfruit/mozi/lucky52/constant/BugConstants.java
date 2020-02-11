package com.cityfruit.mozi.lucky52.constant;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
public class BugConstants {

    public static final String ACTION_TYPE_CONFIRM = "确认了Bug";

    public static final String ACTION_TYPE_CLOSE = "关闭了Bug";

    public static final Set<String> BUG_RESOLUTIONS = Stream.of("fixed", "tostory", "postponed", "timebomb")
            .collect(Collectors.toCollection(HashSet::new));

    /**
     * 根据操作类型、BUG 严重级别获取相应的 VP 值
     * Map<\操作类型, Map<\严重级别, VP 值>>
     */
    public static final Map<String, Map<String, Float>> VALUE_POINTS;

    static {
        VALUE_POINTS = new HashMap<>();
        VALUE_POINTS.put(ACTION_TYPE_CONFIRM, new HashMap<String, Float>() {{
            put("1", 16f);
            put("2", 8f);
            put("3", 1f);
            put("4", 0.5f);
        }});
        VALUE_POINTS.put(ACTION_TYPE_CLOSE, new HashMap<String, Float>() {{
            put("1", 20f);
            put("2", 8f);
            put("3", 1.5f);
            put("4", 2f);
        }});
    }
}
