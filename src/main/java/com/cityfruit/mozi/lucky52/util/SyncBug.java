package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.HttpUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.constant.TaskConst;
import com.cityfruit.mozi.lucky52.constant.UrlConst;
import com.cityfruit.mozi.lucky52.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SyncBug {

    private static final String bugDetailsApi = "http://cf-issue.i-mocca.com/bug-view-%s.html";
    private static final String bugListApi = "http://cf-issue.i-mocca.com/bug-browse-%s-0-bySearch-myQueryID--0-2000-1.json";

    public static List<Bug> sysZombieBugs(String productId, String currentDay) {
        //currentDay -14;
        String zombieDay = DateUtil.getZombieDate(currentDay);

        Map<String, Object> body = new HashMap<String, Object>() {{
            put("fieldtitle", "");
            put("fieldkeywords", "");
            put("fieldsteps", "");
            put("fieldassignedTo", "");
            put("fieldresolvedBy", "");
            put("fieldstatus", "");
            put("fieldconfirmed", "ZERO");
            put("fieldproduct", "");
            put("fieldplan", "");
            put("fieldmodule", "ZERO");
            put("fieldproject", "");
            put("fieldseverity", "0");
            put("fieldpri", "0");
            put("fieldtype", "");
            put("fieldos", "");
            put("fieldbrowser", "");
            put("fieldresolution", "");
            put("fieldtoTask", "");
            put("fieldtoStory", "");
            put("fieldopenedBy", "");
            put("fieldclosedBy", "");
            put("fieldlastEditedBy", "");
            put("fieldmailto", "");
            put("fieldopenedBuild", "");
            put("fieldresolvedBuild", "");
            put("fieldopenedDate", "");
            put("fieldassignedDate", "");
            put("fieldresolvedDate", "");
            put("fieldclosedDate", "");
            put("fieldlastEditedDate", "");
            put("fielddeadline", "");
            put("fieldid", "");
            put("andOr1", "AND");
            put("field1", "status");
            put("operator1", "notinclude");
            put("value1", "closed");
            put("andOr2", "or");
            put("field2", "title");
            put("operator2", "=");
            put("value2", "");
            put("andOr3", "or");
            put("field3", "title");
            put("operator3", "=");
            put("value3", "");
            put("andOr4", "AND");
            put("field4", "openedDate");
            put("operator4", "<");
            put("value4", DateUtil.getZombieDate());
            put("andOr5", "or");
            put("field5", "title");
            put("operator5", "=");
            put("value5", "");
            put("andOr6", "or");
            put("field6", "title");
            put("operator6", "=");
            put("value6", "");
            put("module", "bug");
            put("actionURL", "");
            put("groupItems", "3");
            put("formType", "lite");
        }};
        body.replace("fieldproduct", productId);
        body.replace("actionURL", String.format("/bug-browse-%s-0-bySearch-myQueryID.html", productId));
        body.replace("value4", zombieDay);

        // 请求搜索条件
        HttpUtil.post(UrlConst.ZENTAO_BUILD_SEARCH_QUERY, body, ZentaoUtil.zentaoCookie, HttpUtil.CONTENT_TYPE_FORM_URLENCODED);
        String result = HttpUtil.get(String.format(bugListApi, productId), ZentaoUtil.zentaoCookie);
        String bugStr = JSON.parseObject(result).getJSONObject("data").getJSONArray("bugs").toJSONString();
        return JSON.parseArray(bugStr, Bug.class);
    }

    public static List<BugMini> sysProductBugs(String productId, String currentDay) {
        Map<String, Object> body = new HashMap<>();
        body.put("module", "bug");
        body.put("andOr1", "AND");
        body.put("field1", "lastEditedDate");
        body.put("operator1", "=");
        body.put("value1", currentDay);
        body.put("fieldproduct", productId);
        body.put("actionURL", String.format("/bug-browse-%s-0-bySearch-myQueryID.html", productId));
        // 请求搜索条件
        HttpUtil.post(UrlConst.ZENTAO_BUILD_SEARCH_QUERY, body, ZentaoUtil.zentaoCookie, HttpUtil.CONTENT_TYPE_FORM_URLENCODED);
        String result = HttpUtil.get(String.format(bugListApi, productId), ZentaoUtil.zentaoCookie);
        String bugStr = JSON.parseObject(result).getJSONObject("data").getJSONArray("bugs").toJSONString();
        return JSON.parseArray(bugStr, BugMini.class);
    }

    public static void sync(Map<String, Member> memberMap, String currentDay) {
        Set<String> products = ZentaoUtil.getProducts().keySet();
        products.remove("2");
        products.remove("3");
        products.remove("4");

        List<BugMini> bugMinis = new ArrayList<>();
        List<Bug> zombieBugs = new ArrayList<>();

        log.info("统计 BUG >>");
        for (String productId : products) {
            bugMinis.addAll(sysProductBugs(productId, currentDay));
        }
        log.info("统计 僵尸 BUG >>");

        if (!ZombieUtils.exists(currentDay)) {
            for (String productId : products) {
                zombieBugs.addAll(sysZombieBugs(productId, currentDay));
            }
        }

        log.info("统计 并计算 BUG >>");

        for (BugMini bug : bugMinis) {

            log.info("统计 并计算 BUG >> id: {} ", bug.getId());
            StatBug statBug = checkBug(bug.getId(), currentDay);
            log.info("统计 并计算 BUG << id: {} ", bug.getId());
            if (statBug.isOpened()) {
                Member member = memberMap.get(statBug.getOpenUser());
                if (member == null) {
                    continue;
                }
                int num = member.getOpen().getOrDefault(statBug.getSeverity(), 0);
                member.getOpen().put(statBug.getSeverity(), num + 1);
            }

            if (statBug.isClose()) {
                Member member = memberMap.get(statBug.getCloseUser());
                if (member == null) {
                    continue;
                }
                int num = member.getClose().getOrDefault(statBug.getSeverity(), 0);
                member.getClose().put(statBug.getSeverity(), num + 1);
            }
        }

        log.info("统计 并计算 僵尸BUG >>");
        //统计僵尸 BUG
        Map<String, ZombieInfo> zombieInfoMap = syncZombie(zombieBugs, currentDay);

        for (String name : memberMap.keySet()) {

            Member member = memberMap.get(name);
            ZombieInfo zombie = zombieInfoMap.get(name);
            if (member == null) {
                continue;
            }

            if (zombie == null) {
                member.setZombieCount(0);
            } else {
                member.setZombieCount(zombie.getZombieCount());
            }

        }

        log.info("同步完成{}", bugMinis.size());
    }

    public static StatBug checkBug(String bugId, String currentDay) {

        String text = String.format(bugDetailsApi, bugId);
        // 请求禅道 BUG 详情链接
        String bugViewString = HttpUtil.get(StringUtil.getZentaoBugUrl(text), ZentaoUtil.zentaoCookie);
        // 未登录禅道，登录、重新请求
        if (bugViewString.startsWith(ZentaoUtil.ZENTAO_NOT_LOGGED_IN_PREFIX)) {
            ZentaoUtil.login();
            HttpUtil.get(StringUtil.getZentaoBugUrl(text), ZentaoUtil.zentaoCookie);
        }
        // 获取 FastJson 对象的 BUG 详情

        BugInfo info = JSON.parseObject(JSONObject.parseObject(bugViewString).getString("data"), BugInfo.class);

        List<BugAction> list = info.getAction();

        BugAction bugAction = null;

        StatBug statBug = new StatBug();
        statBug.setSeverity(info.getBug().getSeverity());

        //判断 今日确定
        for (BugAction action : list) {
            if (action.getAction() == BugStatus.bugconfirmed && currentDay.equals(DateUtil.getDayFromStr(action.getDate()))) {
                bugAction = action;
                break;
            }
        }

        if (bugAction == null) {
            // 重复解决的问题
            int openId = 0;
            for (BugAction action : list) {
                if (action.getAction() == BugStatus.resolved && currentDay.equals(DateUtil.getDayFromStr(action.getDate())) && openId < action.getId()) {
                    openId = action.getId();
                    bugAction = action;
                }
            }
        }

        if (bugAction != null) {
            log.info("{} ,{}点击已确定的 BUG ； Id = {} bug级别 = {}", currentDay, info.getProductName(), info.getBug().getId(), info.getBug().getSeverity());
            // 创建加分
            String member = info.getBug().getOpenedBy();
            statBug.setOpenUser(member);
            statBug.setOpened(true);
        } else {
            return statBug;
        }

        int closeId = 0;
        // 关闭 重复关闭 找出最早日期
        for (BugAction action : list) {
            if (action.getAction() == BugStatus.closed && currentDay.equals(DateUtil.getDayFromStr(action.getDate())) && closeId < action.getId()) {
                // 关闭BUG = 关闭人
                closeId = action.getId();
                String member = action.getActor();
                statBug.setCloseUser(member);
                statBug.setClose(true);
            }
        }

        return statBug;
    }

    public static Map<String, ZombieInfo> syncZombie(List<Bug> zombieBugs, String currentDay) {
        return ZombieUtils.mapZombie(true, currentDay, zombieMap -> {
            if (zombieBugs == null || zombieBugs.size() == 0) {
                return zombieMap;
            }

            List<UserInfo> users = UserUtil.listUer(false, userInfos -> userInfos);

            Map<String, String> userMap = new HashMap<>();
            for (UserInfo userInfo : users) {
                userMap.put(userInfo.getZentaoId(), userInfo.getBearyChatId());
            }

            for (Bug bug : zombieBugs) {
                String name = bug.getAssignedTo();
                String bcName = userMap.get(name);
                if (bcName == null) {
                    continue;
                }

                ZombieInfo zombieInfo = zombieMap.get(bcName);

                if (zombieInfo == null) {
                    continue;
                }

                if (!zombieInfo.getZombieList().contains(bug.getId())) {
                    zombieInfo.getZombieList().add(bug.getId());
                    zombieInfo.setZombieCount(zombieInfo.getZombieCount() + 1);
                }
            }
            return zombieMap;
        });
    }

    /**
     * 特定任务
     *
     * @param memberMap 用户
     */
    public static void syncPush(Map<String, Member> memberMap) {
        for (Member member : memberMap.values()) {
            checkTaskPush(member);
        }
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
            pushTask(member, TaskConst.TASK_NAME_2, TaskConst.TASK_NAME_2_EXTRA_SCORE);
        }

        if (status.isTaskSuccess3() && !status.isTaskPush3()) {
            status.setTaskPush3(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_3, TaskConst.TASK_NAME_3_EXTRA_SCORE);
        }

        if (status.isTaskSuccess4() && !status.isTaskPush4()) {
            status.setTaskPush4(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_4, TaskConst.TASK_NAME_4_EXTRA_SCORE);
        }
        if (status.isTaskSuccess5() && !status.isTaskPush5()) {
            status.setTaskPush5(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_5, TaskConst.TASK_NAME_5_EXTRA_SCORE);
        }

        // 6、判断今日 S1 BUG 总数 > 0
        if (!status.isTaskSuccess6()) {
            status.setTaskSuccess6(open.getOrDefault("1", 0) > 0);
        }

        if (status.isTaskSuccess6() && !status.isTaskPush6()) {
            status.setTaskPush6(true);
            //需要推送
            pushTask(member, TaskConst.TASK_NAME_6, 2);
        }

        // 7、判断今日 S2 BUG 总数 > 3
        if (!status.isTaskSuccess7()) {
            status.setTaskSuccess7(open.getOrDefault("1", 0) >= 3);
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
