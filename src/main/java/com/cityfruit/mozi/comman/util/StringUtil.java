package com.cityfruit.mozi.comman.util;

import com.cityfruit.mozi.lucky52.constant.BugConst;
import com.cityfruit.mozi.lucky52.constant.RegexStringConst;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
public class StringUtil {

    /**
     * 字符串是否为空
     *
     * @param str 字符串
     * @return 为空：true；不为空：false
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim()) || "null".equals(str);
    }

    /**
     * 从禅道通知中获取 BUG 链接
     *
     * @param text 禅道通知
     * @return BUG 链接字符串（匹配不到返回 null）
     */
    public static String getZentaoBugUrl(String text) {
        Pattern pattern = Pattern.compile(RegexStringConst.ZENTAO_BUG_VIEW_URL, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group().replace(".html", ".json");
        }
        return null;
    }

}
