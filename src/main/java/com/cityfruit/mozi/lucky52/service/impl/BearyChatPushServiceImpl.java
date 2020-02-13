package com.cityfruit.mozi.lucky52.service.impl;

import com.cityfruit.mozi.comman.util.HttpUtil;
import com.cityfruit.mozi.lucky52.constant.Url;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.service.MemberService;
import com.cityfruit.mozi.lucky52.util.TreasureBoxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tianyuheng
 * @date 2020/02/11
 */
@Slf4j
@Component
@EnableScheduling
public class BearyChatPushServiceImpl {

    @Resource
    MemberService memberService;

    /**
     * 每日 12、15、18 点向倍洽机器人中推送 VP 得分情况
     */
    @Scheduled(cron = "0 0 12,15,18 * * ?")
    public void pushValuePoint() {
        log.info("[开始向倍洽群组推送 VP 得分情况……]");
        StringBuilder pushTextStringBuilder = new StringBuilder(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        // 获取用户信息
        List<Member> members = memberService.getMembers();
        for (Member member : members) {
            // 构造消息体
            pushTextStringBuilder.append("\n").append(member.getName()).append("的 VP 得分为 ").append(member.getValuePoint());
        }
        String pushText = pushTextStringBuilder.toString();
        // 构造请求体
        Map<String, Object> body = new HashMap<String, Object>(1) {{
            put("text", pushText);
        }};
        // 推送
        HttpUtil.post(Url.BC_VALUE_POINT_PUSH, body, "");
        log.info("[向倍洽群组推送 VP 得分情况完成]");
    }

    /**
     * 每日 18 点向倍洽群组推送不满足开宝箱条件的用户
     */
    @Scheduled(cron = "0 0 18 * * ?")
    public void pushCannotOpenTreasureBoxMembers() {
        log.info("[开始向倍洽群组推送不满足开宝箱条件的用户……]");
        List<Member> members = memberService.getMembers();
        // 遍历用户，查找分数低于 50 的用户
        for (Member member : members) {
            if (Float.parseFloat(member.getValuePoint()) < TreasureBoxUtil.VP_50) {
                // 构造消息体
                StringBuilder pushTextStringBuilder = new StringBuilder();
                pushTextStringBuilder.append("@").append(member.getBearyChatId()).append("，您暂未获得今日开宝箱资格，加油！今天还未结束！");
                Map<String, Object> body = new HashMap<String, Object>(1) {{
                    put("text", pushTextStringBuilder.toString());
                }};
                HttpUtil.post(Url.BC_VALUE_POINT_PUSH, body, "");
            }
        }
        log.info("[向倍洽群组推送不满足开宝箱条件的用户完成]");
    }

    /**
     * 校验是否推送开宝箱提醒
     *
     * @param beforeValuePoint 加分前分数
     * @param nowValuePoint    加分后分数
     * @param bearyChatId      倍洽 ID
     */
    public static void checkValuePointAndPushNotice(float beforeValuePoint, float nowValuePoint, String bearyChatId) {
        // 获取概率名称
        String probabilityName = TreasureBoxUtil.getProbabilityName(beforeValuePoint, nowValuePoint);
        if (probabilityName != null) {
            // 构造推送消息体
            StringBuilder pushTextStringBuilder = new StringBuilder("@");
            pushTextStringBuilder.append(bearyChatId).append("，您已获得今日开宝箱资格，");
            pushTextStringBuilder.append(probabilityName);
            // 推送消息体后缀
            pushTextStringBuilder.append("掉落`品质碎片`，每 20 个品质碎片可以兑换 1 个品质星\n" +
                    "您可以选择现在开奖，或继续工作一会再开，但请注意一天之内只能开一次\n" +
                    "---\n" +
                    "开奖方式及规则：\n" +
                    "- 输入 `@eva 开宝箱` 进行开奖\n" +
                    "- 必须在 #推送测试 群进行开奖，在其他群组或个人对话中进行开奖不算");
            // 构造推送请求体
            Map<String, Object> body = new HashMap<String, Object>(1) {{
                put("text", pushTextStringBuilder.toString());
            }};
            // 推送至 BC 群组
            HttpUtil.post(Url.BC_VALUE_POINT_PUSH, body, "");
            log.info("[推送完成]{}", pushTextStringBuilder.toString());
        }
    }

}
