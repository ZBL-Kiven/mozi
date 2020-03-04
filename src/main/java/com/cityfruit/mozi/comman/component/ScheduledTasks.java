package com.cityfruit.mozi.comman.component;

import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.service.MemberService;
import com.cityfruit.mozi.lucky52.util.BearyChatPushUtil;
import com.cityfruit.mozi.lucky52.util.ScoreUtil;
import com.cityfruit.mozi.lucky52.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务
 *
 * @author tianyuheng
 * @date 2020/02/13
 */
@Component
@Slf4j
@EnableScheduling
public class ScheduledTasks {

    @Resource
    MemberService memberService;

    /**
     * 将时间戳更新为今日零点时间戳，清零 QP 统计，重置开宝箱次数
     * 晚上1点 同步一下数据
     */
    @Scheduled(cron = "0 0 0,1 * * ?", zone = "Asia/Shanghai")
    public void clearData() {
        ScoreUtil.getMembers(true, memberMap -> true);
    }

    /**
     * 10 点,推送特殊任务 10、11
     */
    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Shanghai")
    public void pushSpecial() {
        ScoreUtil.getMembers(map -> {
            Utils.pushTask10or11(map);
            return true;
        });
    }

    /**
     * 每日 12、15、18 点向倍洽机器人中推送 QP 得分情况
     */
    @Scheduled(cron = "0 0 12,15,18 * * ?", zone = "Asia/Shanghai")
    public void pushQualityPointToBearyChat() {
        List<Member> members = memberService.getMembers();
        BearyChatPushUtil.pushQualityPoint(members);
    }

    /**
     * 每日 18 点向倍洽群组推送不满足开宝箱条件的用户
     */
    @Scheduled(cron = "0 0 18 * * ?", zone = "Asia/Shanghai")
    public void pushCannotOpenTreasureBoxMembersToBearyChat() {
        List<Member> members = memberService.getMembers();
        BearyChatPushUtil.pushCannotOpenTreasureBoxMembers(members);
    }

    /**
     * 每日 18 点向倍洽群组推送满足开宝箱条件的用户提醒
     */
    @Scheduled(cron = "0 0 18 * * ?", zone = "Asia/Shanghai")
    public void pushOpenTreasureBoxNoticeToBearyChat() {
        List<Member> members = memberService.getMembers();
        BearyChatPushUtil.pushOpenTreasureBoxNotice(members);
    }

}
