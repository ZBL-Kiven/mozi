package com.cityfruit.mozi.comman.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
public class DateUtil {

    /**
     * @return 当日零点时间戳
     */
    public static long getTodayTimeMillis() {
        return System.currentTimeMillis() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
    }

    /**
     * 从禅道 BUG 中获取时间的时间戳
     *
     * @param timeString 禅道 BUG 中的时间字符串
     * @return 时间戳
     */
    public static long getTimeMillisFromBug(String timeString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        return date.getTime();
    }
}
