package com.cityfruit.mozi.comman.component;

import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
import com.cityfruit.mozi.lucky52.constant.TaskConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.entity.TaskStatus;
import com.cityfruit.mozi.lucky52.service.MemberService;
import com.cityfruit.mozi.lucky52.util.BearyChatPushUtil;
import com.cityfruit.mozi.lucky52.util.ScoreUtil;
import com.cityfruit.mozi.lucky52.util.Utils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.cityfruit.mozi.lucky52.constant.TaskConst.TASK_NAME_11;

/**
 * 定时任务
 *
 * @author tianyuheng
 * @date 2020/02/13
 */
@Component
@EnableScheduling
public class ScheduledTasks {

    @Resource
    MemberService memberService;

    /**
     * 将时间戳更新为今日零点时间戳，清零 QP 统计，重置开宝箱次数
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearData() {
        ScoreUtil.getMembers(true, memberMap -> true);
    }


    /**
     * 10 点,推送特殊任务 10、11
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void pushSpecial() {
        ScoreUtil.getMembers(map -> {
            Utils.pushTask10or11(map);
            return true;
        });
    }

    /**
     * 每日 12、15、18 点向倍洽机器人中推送 QP 得分情况
     */
    @Scheduled(cron = "0 0 12,15,18 * * ?")
    public void pushQualityPointToBearyChat() {
        List<Member> members = memberService.getMembers();
        BearyChatPushUtil.pushQualityPoint(members);
    }

    /**
     * 每日 18 点向倍洽群组推送不满足开宝箱条件的用户
     */
    @Scheduled(cron = "0 0 18 * * ?")
    public void pushCannotOpenTreasureBoxMembersToBearyChat() {
        List<Member> members = memberService.getMembers();
        BearyChatPushUtil.pushCannotOpenTreasureBoxMembers(members);
    }

    /**
     * 每日 18 点向倍洽群组推送满足开宝箱条件的用户提醒
     */
    @Scheduled(cron = "0 0 18 * * ?")
    public void pushOpenTreasureBoxNoticeToBearyChat() {
        List<Member> members = memberService.getMembers();
        BearyChatPushUtil.pushOpenTreasureBoxNotice(members);
    }

}
