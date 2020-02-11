package com.cityfruit.mozi.comman.util;

import com.cityfruit.mozi.lucky52.constant.BugConstants;
import com.cityfruit.mozi.lucky52.constant.RegexString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
public class StringUtil {

    /**
     * 从禅道通知中获取 BUG 状态
     *
     * @param text 禅道通知
     * @return BUG 状态（确认了Bug、关闭了Bug、null）
     */
    public static String getZentaoNoticeType(String text) {
        String[] textArray = text.split(RegexString.ZENTAO_NOTICE_TYPE_SPLIT);
        if (textArray[0].contains(BugConstants.ACTION_TYPE_CONFIRM)) {
            return BugConstants.ACTION_TYPE_CONFIRM;
        } else if (textArray[0].contains(BugConstants.ACTION_TYPE_CLOSE)) {
            return BugConstants.ACTION_TYPE_CLOSE;
        }
        return null;
    }

    /**
     * 从禅道通知中获取 BUG 链接
     *
     * @param text 禅道通知
     * @return BUG 链接字符串（匹配不到返回 null）
     */
    public static String getZentaoBugUrl(String text) {
        Pattern pattern = Pattern.compile(RegexString.ZENTAO_BUG_VIEW_URL, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group().replace(".html", ".json");
        }
        return null;
    }

}
