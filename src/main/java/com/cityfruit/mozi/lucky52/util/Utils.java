package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.BugConst;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import com.cityfruit.mozi.lucky52.entity.Bug;
import com.cityfruit.mozi.lucky52.entity.Member;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
@Slf4j
public class Utils {

    /**
     * 校验日期是否为今天
     * 若不是今天，将时间戳更新为今日零点时间戳，清零 QP 统计，重置开宝箱次数，清空未确认 BUG 列表
     * 若是今天，清零 QP 统计，清空未确认 BUG 列表
     */
    public static void checkDateAndClearData() {
        JSONObject jsonScore = JsonUtil.getJsonObjectFromFile(FilePathConst.SCORE_JSON_FILE);
        JSONObject jsonMembers = jsonScore.getJSONObject(JsonKeysConst.MEMBERS);
        // 判断得分统计是否为当天统计
        long todayTimeMillis = DateUtil.getTodayTimeMillis();
        // json 文件数据非当天数据，更新时间戳，数据清零，重置开宝箱次数
        if (jsonScore.getLongValue(JsonKeysConst.TS) < todayTimeMillis) {
            jsonScore.put(JsonKeysConst.TS, todayTimeMillis);
            for (String member : jsonMembers.keySet()) {
                JSONObject jsonMember = jsonMembers.getJSONObject(member);
                // 情况 QP 值
                jsonMember.put(JsonKeysConst.QUALITY_POINT, 0);
                // 清空未确认 BUG 列表
                jsonMember.put(JsonKeysConst.OPENED_BUGS, new JSONArray());
                // 清空当日开宝箱情况
                jsonMember.put(JsonKeysConst.OPENED, false);
            }
        } else {
            // 清零 QP 统计
            for (String member : jsonMembers.keySet()) {
                jsonMembers.getJSONObject(member).put(JsonKeysConst.QUALITY_POINT, 0);
                // 清空未确认 BUG 列表
                jsonMembers.getJSONObject(member).put(JsonKeysConst.OPENED_BUGS, new JSONArray());
            }
        }
        // 存储
        JsonUtil.saveJsonFile(jsonScore, FilePathConst.SCORE_JSON_FILE);
    }

    /**
     * 更新 QP 值并保存
     *
     * @param jsonScore                 score.json JSONObject
     * @param bug                       要校验的 BUG 对象
     * @param actionType                BUG 操作类型
     * @param pushOpenTreasureBoxNotice 是否推送通知
     */
    public static void updateQualityPointAndSave(JSONObject jsonScore, Bug bug, String actionType, boolean pushOpenTreasureBoxNotice) {
        // 当日零点时间戳
        long today = jsonScore.getLongValue(JsonKeysConst.TS);
        JSONObject jsonMembers = jsonScore.getJSONObject(JsonKeysConst.MEMBERS);
        switch (actionType) {
            // 创建新 BUG，将 BUG ID 添加至 json 文件
            case BugConst.ACTION_TYPE_OPEN: {
                // 当天创建的 BUG、创建人属于 QA
                if (jsonMembers.containsKey(bug.getOpenedBy()) && DateUtil.getTimeMillisFromBug(bug.getOpenedDate()) > today) {
                    // 获取该创建人未确认的 BUG 列表
                    JSONObject jsonMember = jsonMembers.getJSONObject(bug.getOpenedBy());
                    Member member = new Member(jsonMember);
                    List<String> openedBugs = member.getOpenedBugs();
                    openedBugs.add(bug.getId());
                    jsonMember.put(JsonKeysConst.OPENED_BUGS, openedBugs);
                }
                break;
            }
            // 确认 BUG，将 BUG ID 从 json 文件中移除
            case BugConst.ACTION_TYPE_CONFIRM: {
                // 当天创建的 BUG、创建人属于 QA
                if (jsonMembers.containsKey(bug.getOpenedBy()) && DateUtil.getTimeMillisFromBug(bug.getOpenedDate()) > today) {
                    // 获取该创建人未确认的 BUG 列表
                    JSONObject jsonMember = jsonMembers.getJSONObject(bug.getOpenedBy());
                    Member member = new Member(jsonMember);
                    List<String> openedBugs = member.getOpenedBugs();
                    // 逆向遍历列表，移除 bugId 相等的元素
                    for (int i = openedBugs.size() - 1; i >= 0; i--) {
                        if (openedBugs.get(i).equals(bug.getId())) {
                            openedBugs.remove(i);
                        }
                    }
                    // 统计加分
                    increaseQualityPointAndPushNotice(jsonMember, bug, actionType, pushOpenTreasureBoxNotice);
                    // 保存列表
                    jsonMember.put(JsonKeysConst.OPENED_BUGS, openedBugs);
                }
                break;
            }
            // 解决 BUG，若 json 文件中存在此未确认的 BUG，移除此 BUG ID，并进行统计加分
            case BugConst.ACTION_TYPE_RESOLVE: {
                // 当天创建的 BUG、创建人属于 QA
                if (jsonMembers.containsKey(bug.getOpenedBy()) && DateUtil.getTimeMillisFromBug(bug.getOpenedDate()) > today) {
                    JSONObject jsonMember = jsonMembers.getJSONObject(bug.getOpenedBy());
                    Member member = new Member(jsonMember);
                    List<String> openedBugs = member.getOpenedBugs();
                    // 逆向遍历列表，若存在 BUG ID 相等的元素，移除此元素，并统计加分
                    for (int i = openedBugs.size() - 1; i >= 0; i--) {
                        if (openedBugs.get(i).equals(bug.getId())) {
                            openedBugs.remove(i);
                            // 统计加分
                            increaseQualityPointAndPushNotice(jsonMember, bug, actionType, pushOpenTreasureBoxNotice);
                        }
                    }
                    jsonMember.put(JsonKeysConst.OPENED_BUGS, openedBugs);
                }
                break;
            }
            // 关闭 BUG，校验有效，统计加分
            case BugConst.ACTION_TYPE_CLOSE: {
                // 当天关闭的 BUG，关闭人属于 QA
                if (jsonMembers.containsKey(bug.getClosedBy()) && DateUtil.getTimeMillisFromBug(bug.getClosedDate()) > today) {
                    // 解决方案有效
                    if (BugConst.BUG_RESOLUTIONS.contains(bug.getResolution())) {
                        JSONObject jsonMember = jsonMembers.getJSONObject(bug.getClosedBy());
                        // 统计加分
                        increaseQualityPointAndPushNotice(jsonMember, bug, actionType, pushOpenTreasureBoxNotice);
                    }
                }
                break;
            }
            default:
                break;
        }
        JsonUtil.saveJsonFile(jsonScore, FilePathConst.SCORE_JSON_FILE);
    }

    /**
     * 更新 QP 值、保存、是否推送
     *
     * @param jsonMember                需要加分的成员
     * @param bug                       要校验的 BUG 对象
     * @param actionType                BUG 操作类型
     * @param pushOpenTreasureBoxNotice 是否推送通知
     */
    private static void increaseQualityPointAndPushNotice(JSONObject jsonMember, Bug bug, String actionType, boolean pushOpenTreasureBoxNotice) {
        float qualityPoint = jsonMember.getFloatValue(JsonKeysConst.QUALITY_POINT);
        // 增加相应 QP 值
        float qualityPointAdd = BugConst.QUALITY_POINTS.get(actionType).get(bug.getSeverity());
        // 分数超过 50 推送
        if (pushOpenTreasureBoxNotice) {
            BearyChatPushUtil.checkQualityPointAndPushNotice(qualityPoint, qualityPoint + qualityPointAdd, jsonMember.getString(JsonKeysConst.BEARY_CHAT_ID));
        }
        qualityPoint += qualityPointAdd;
        jsonMember.put(JsonKeysConst.QUALITY_POINT, qualityPoint);
        log.info("{} {} 获得 Quality Point：{} 分，当前总共 {} 分。\nBug 详情：{}", jsonMember.getString(JsonKeysConst.NAME), actionType, qualityPointAdd, qualityPoint, bug);
    }

}
