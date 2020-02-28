package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.*;
import com.cityfruit.mozi.lucky52.entity.Bug;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.entity.TaskStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
@Slf4j
public class Utils {

    public static void updateQualityPointAndSave(Map<String, Member> memberMap, Bug bug, String actionType, boolean pushOpenTreasureBoxNotice) {

        //获取当前 BUG 创建人
        Member member = memberMap.get(bug.getOpenedBy());
        if (member == null) {
            log.info("本地数据 没有该创建人" + bug.getOpenedBy());
            return;
        }

        // 获取该创建人未确认的 BUG 列表
        List<String> openedBugs = member.getOpenedBugs();

        switch (actionType) {

            // 创建新 BUG，将 BUG ID 添加至 json 文件
            case BugConst.ACTION_TYPE_OPEN: {
                // 当天创建的 BUG、创建人属于 QA
                openedBugs.add(bug.getId());
                member.setOpenedBugs(openedBugs);
                break;
            }

            // 确认 BUG，将 BUG ID 从 json 文件中移除
            case BugConst.ACTION_TYPE_CONFIRM: {
                // 当天创建的 BUG、创建人属于 QA
                // 获取该创建人未确认的 BUG 列表
                // 逆向遍历列表，移除 bugId 相等的元素
                for (int i = openedBugs.size() - 1; i >= 0; i--) {
                    if (openedBugs.get(i).equals(bug.getId())) {
                        openedBugs.remove(i);
                    }
                }

                // 统计加分
                increaseQualityPointAndPushNotice(member, bug, actionType, pushOpenTreasureBoxNotice);

                // 增加创建相应级别 BUG 数量 1 个
                member.getOpen().put(bug.getSeverity(), member.getOpen().get(bug.getSeverity()) + 1);

                // 保存列表
                member.setOpenedBugs(openedBugs);
                break;
            }

            // 解决 BUG，若 json 文件中存在此未确认的 BUG，移除此 BUG ID，并进行统计加分
            case BugConst.ACTION_TYPE_RESOLVE: {
                // 当天创建的 BUG、创建人属于 QA
                // 逆向遍历列表，若存在 BUG ID 相等的元素，移除此元素，并统计加分
                for (int i = openedBugs.size() - 1; i >= 0; i--) {
                    if (openedBugs.get(i).equals(bug.getId())) {
                        openedBugs.remove(i);
                        // 统计加分
                        increaseQualityPointAndPushNotice(member, bug, actionType, pushOpenTreasureBoxNotice);
                        // 增加创建相应级别 BUG 数量 1 个
                        member.getOpen().put(bug.getSeverity(), member.getOpen().get(bug.getSeverity()) + 1);
                    }
                }
                member.setOpenedBugs(openedBugs);
                break;
            }

            // 关闭 BUG，校验有效，统计加分
            case BugConst.ACTION_TYPE_CLOSE: {
                // 当天关闭的 BUG，关闭人属于 QA
                // 解决方案有效
                if (BugConst.BUG_RESOLUTIONS.contains(bug.getResolution())) {
                    // 统计加分
                    increaseQualityPointAndPushNotice(member, bug, actionType, pushOpenTreasureBoxNotice);

                    // 增加创建相应级别 BUG 数量 1 个
                    member.getClose().put(bug.getSeverity(), member.getClose().get(bug.getSeverity()) + 1);
                }
                break;
            }

            default:
                break;
        }
        checkTaskPush(member);
    }

    /**
     * 更新 QP 值、保存、是否推送
     *
     * @param member                    需要加分的成员
     * @param bug                       要校验的 BUG 对象
     * @param actionType                BUG 操作类型
     * @param pushOpenTreasureBoxNotice 是否推送通知
     */
    private static void increaseQualityPointAndPushNotice(Member member, Bug bug, String actionType, boolean pushOpenTreasureBoxNotice) {
        float qualityPoint = member.getQualityPoint();

        // 增加相应 QP 值
        float qualityPointAdd = BugConst.QUALITY_POINTS.get(actionType).get(bug.getSeverity());

        // 分数超过 50 推送
        if (pushOpenTreasureBoxNotice) {
            BearyChatPushUtil.checkQualityPointAndPushNotice(qualityPoint, qualityPoint + qualityPointAdd, member.getBearyChatId());
        }

        log.info("{} {} 获得 Quality Point：{} 分，当前总共 {} 分。\nBug 详情：{}", member.getName(), actionType, qualityPointAdd, qualityPoint, bug);
    }

    /**
     * 判断任务是否完成 并且是否推送
     *
     * @param member QA
     */
    private static void checkTaskPush(Member member) {

        Map<String, Integer> open = member.getOpen();
        Map<String, Integer> closes = member.getClose();

        TaskStatus status = member.getStatus();

        // 1-5、获取今日有效创建 BUG 总数

        int open1_5 = 0;
        for (Integer value : open.values()) {
            open1_5 += value;
        }
        status.setTaskSuccess1(open1_5 >= 1);
        status.setTaskSuccess2(open1_5 >= 5);
        status.setTaskSuccess3(open1_5 >= 15);
        status.setTaskSuccess4(open1_5 >= 25);
        status.setTaskSuccess5(open1_5 >= 30);
        if (status.isTaskSuccess1() && !status.isTaskPush1()) {
            status.setTaskPush1(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_1, 1);
        }

        if (status.isTaskSuccess2() && !status.isTaskPush2()) {
            status.setTaskPush2(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_2, 1);
        }

        if (status.isTaskSuccess3() && !status.isTaskPush3()) {
            status.setTaskPush3(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_3, 3);
        }

        if (status.isTaskSuccess4() && !status.isTaskPush4()) {
            status.setTaskPush4(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_4, 5);
        }
        if (status.isTaskSuccess5() && !status.isTaskPush5()) {
            status.setTaskPush5(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_5, 5);
        }

        // 6、判断今日 S1 BUG 总数 > 0
        if (!status.isTaskSuccess6()) {
            status.setTaskSuccess6(open.get("1") > 0);
        }

        if (status.isTaskSuccess6() && !status.isTaskPush6()) {
            status.setTaskPush6(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_6, 2);
        }

        // 7、判断今日 S2 BUG 总数 > 3
        if (!status.isTaskSuccess7()) {
            status.setTaskSuccess7(open.get("1") >= 3);
        }

        if (status.isTaskSuccess7() && !status.isTaskPush7()) {
            status.setTaskPush7(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_7, 2);
        }

        // 8、判断今日 BUG 关闭总数 > 20
        if (!status.isTaskSuccess8()) {
            int count = 0;
            for (Integer value : closes.values()) {
                count += value;
            }
            status.setTaskSuccess8(count >= 20);
        }

        if (status.isTaskSuccess8() && !status.isTaskPush8()) {
            status.setTaskPush8(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_8, 5);
        }

        // 9、判断今日 BUG S3+S4 关闭总数 > 30
        if (!status.isTaskSuccess9()) {
            int count = closes.getOrDefault("3", 0)
                    + closes.getOrDefault("4", 0);
            status.setTaskSuccess9(count >= 30);
        }

        if (status.isTaskSuccess9() && !status.isTaskPush9()) {
            status.setTaskPush9(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_9, 5);
        }

        // 10、僵尸 BUG 两个月前的


        // 11、前一天第一名

    }

    /**
     * 推送信息
     *
     * @param member   用户
     * @param taskName 任务名称
     * @param qp       当前qp 点数
     */
    private static void pushTask(Member member, String taskName, int qp) {
        BearyChatPushUtil.pushBcByFinishedTask(member.getBearyChatId(), taskName, qp, member.getQualityPoint());
    }

}
