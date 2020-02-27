package com.cityfruit.mozi.lucky52.util;

import com.cityfruit.mozi.comman.util.HttpUtil;
import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
import com.cityfruit.mozi.lucky52.constant.UrlConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tianyuheng
 * @date 2020/02/11
 */
@Slf4j
public class BearyChatPushUtil {

    /**
     * 向倍洽机器人中推送 QP 得分情况
     */
    public static void pushQualityPoint(List<Member> members) {
        log.info("[开始向倍洽群组推送 QP 得分情况……]");
        StringBuilder pushTextStringBuilder = new StringBuilder(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        // 获取用户信息
        for (Member member : members) {
            // 构造消息体
            pushTextStringBuilder.append("\n").append(member.getName()).append(BearyChatConst.scheduleQualityPoint(member));
        }
        String pushText = pushTextStringBuilder.toString();
        // 构造请求体
        Map<String, Object> body = new HashMap<String, Object>(1) {{
            put("text", pushText);
        }};
        // 推送
        HttpUtil.post(UrlConst.BC_QUALITY_POINT_PUSH, body, "", HttpUtil.CONTENT_TYPE_JSON);
        log.info("[向倍洽群组推送 QP 得分情况完成]");
    }

    /**
     * 18 点向倍洽群组推送不满足开宝箱条件的用户
     */
    public static void pushCannotOpenTreasureBoxMembers(List<Member> members) {
        log.info("[开始向倍洽群组推送不满足开宝箱条件的用户……]");
        // 遍历用户，查找分数低于 50 的用户
        for (Member member : members) {
            if (member.getQualityPoint() < TreasureBoxUtil.QP_50) {
                // 构造消息体
                String text = BearyChatConst.cannotOpenBoxNotice(member.getBearyChatId());
                Map<String, Object> body = new HashMap<String, Object>(1) {{
                    put("text", text);
                }};
                HttpUtil.post(UrlConst.BC_QUALITY_POINT_PUSH, body, "", HttpUtil.CONTENT_TYPE_JSON);
                log.info("{}", text);
            }
        }
        log.info("[向倍洽群组推送不满足开宝箱条件的用户完成]");
    }

    /**
     * 每日 18 点向倍洽群组推送满足开宝箱条件的用户提醒
     *
     * @param members 成员列表
     */
    public static void pushOpenTreasureBoxNotice(List<Member> members) {
        log.info("[开始向倍洽群组推送满足开宝箱条件的用户提醒……]");
        // 遍历用户，查找分数满足开宝箱要求的用户
        for (Member member :
                members) {
            // 已开过宝箱，不提醒
            if (member.isOpened()) {
                continue;
            }
            // 获取概率名称
            String probabilityName = TreasureBoxUtil.getProbabilityName(member.getQualityPoint());
            if (probabilityName != null) {
                String text = BearyChatConst.openBoxNotice(member.getBearyChatId(), probabilityName);
                Map<String, Object> body = new HashMap<String, Object>(1) {{
                    put("text", text);
                }};
                // 推送至 BC 群组
                HttpUtil.post(UrlConst.BC_QUALITY_POINT_PUSH, body, "", HttpUtil.CONTENT_TYPE_JSON);
                log.info("[推送满足开宝箱条件的用户提醒……完成]");
            }
        }
    }

    /**
     * 校验是否推送开宝箱提醒
     *
     * @param beforeQualityPoint 加分前分数
     * @param nowQualityPoint    加分后分数
     * @param bearyChatId        倍洽 ID
     */
    public static void checkQualityPointAndPushNotice(float beforeQualityPoint, float nowQualityPoint, String bearyChatId) {
        // 获取概率名称
        String probabilityName = TreasureBoxUtil.getProbabilityName(beforeQualityPoint, nowQualityPoint);
        if (probabilityName != null) {
            String text = BearyChatConst.openBoxNotice(bearyChatId, probabilityName);
            // 构造推送请求体
            Map<String, Object> body = new HashMap<String, Object>(1) {{
                put("text", text);
            }};
            // 推送至 BC 群组
            HttpUtil.post(UrlConst.BC_QUALITY_POINT_PUSH, body, "", HttpUtil.CONTENT_TYPE_JSON);
            log.info("[推送完成]{}", text);
        }
    }

}
