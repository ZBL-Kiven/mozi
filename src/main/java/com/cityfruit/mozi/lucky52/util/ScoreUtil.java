package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.entity.UserInfo;
import com.cityfruit.mozi.lucky52.constant.BugConst;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import com.cityfruit.mozi.lucky52.entity.Bug;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.tools.MapType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ScoreUtil {
    private static ScoreListener listener;

    public interface ScoreListener {
        Map<String, Member> onCreate(String fileName);
    }

    public static void setListener(ScoreListener listener) {
        ScoreUtil.listener = listener;
    }

    public interface Fun<T, R> {
        R exec(T t);
    }

    public static <R> R getMembers(boolean sync, Fun<Map<String, Member>, R> callBack) {
        String currentDay = DateUtil.getCurrentDay();
        String fileName = String.format(FilePathConst.DAY_SCORE_JSON_FILE, currentDay);
        String content = JsonUtil.getStringFromFile(fileName);

        Map<String, Member> memberMap;
        if (StringUtil.isEmpty(content)) {
            memberMap = listener.onCreate(fileName);
        } else {
            MapType type = new MapType(Map.class, new Type[]{String.class, Member.class});
            memberMap = JSON.parseObject(content, type);
        }
        if (sync) {
            //清空数据
            ScoreUtil.clearCurrentDayCache(memberMap);
            //同步禅道BUG
            syncBug(memberMap);
            //更新昨日数据 判断今日排行第一的人
            Member member = ScoreUtil.getYesterdayMember();
            if (member != null) {
                memberMap.get(member.getZentaoId()).getStatus().setTaskSuccess11(true);
            }

            // Utils.pushTask10or11(memberMap);
        }
        R r = callBack.exec(memberMap);
        //保存数据
        JsonUtil.saveJsonFile(memberMap, fileName);
        return r;
    }

    public static <R> R getMembers(Fun<Map<String, Member>, R> callBack) {
        return getMembers(false, callBack);
    }

    private static void syncBug(Map<String, Member> memberMap) {

        Set<String> products;
        // 获取产品列表
        products = ZentaoUtil.getProducts().keySet();
//         products = new HashSet<>();
//         products.add("9");

        log.info("[产品Id 列表]{} 总数 {}", products, products.size());
        // 遍历产品列表，获取当日创建
        for (String productId : products) {

            // 统计未确认 BUG // 遍历 BUG 列表，更新并保存未确认的 BUG 列表
            log.info("[统计未确认……] Product ID: {}", productId);
            execZentaoSearch(memberMap, ZentaoUtil.SEARCH_TODAY_OPENED_WITHOUT_CONFIRMED, productId, BugConst.ACTION_TYPE_OPEN);

            // 统计确认 BUG 得分 // 遍历 BUG 列表，更新并保存 QP 值
            log.info("[统计确认……] Product ID: {}", productId);
            execZentaoSearch(memberMap, ZentaoUtil.SEARCH_TODAY_OPENED_WITH_CONFIRMED, productId, BugConst.ACTION_TYPE_CONFIRM);

            // 统计关闭 BUG 得分 // 遍历 BUG 列表，更新并保存 QP 值
            log.info("[统计关闭……] Product ID: {}", productId);
            execZentaoSearch(memberMap, ZentaoUtil.SEARCH_TODAY_CLOSED, productId, BugConst.ACTION_TYPE_CLOSE);

            log.info("[统计僵尸BUG……] Product ID: {}", productId);
            execZentaoSearch(memberMap, ZentaoUtil.SEARCH_ZOMBIE_BUG, productId, BugConst.ACTION_TYPE_ZOMBIE);
        }

        log.info("[当日 Quality Point 获取情况统计完毕]");
    }

    /**
     * 获取产品数据
     *
     * @param memberMap  数据
     * @param type       类型
     * @param productId  产品
     * @param actionType 产品类型
     */
    private static void execZentaoSearch(Map<String, Member> memberMap, Integer type, String productId, String actionType) {
        String searchResult = ZentaoUtil.getSearchResult(type, ZentaoUtil.zentaoCookie, productId);
        execUpdate(memberMap, searchResult, actionType);
    }

    private static void execUpdate(Map<String, Member> memberMap, String content, String type) {
        String bugsStr = JSONObject.parseObject(content).getJSONObject("data").getJSONArray("bugs").toJSONString();
        List<Bug> bugs = JSON.parseArray(bugsStr, Bug.class);

        for (Bug bug : bugs) {
            Utils.updateQualityPointAndSave(memberMap, bug, type, false);
        }
    }


    public static Member getYesterdayMember() {
        String yesterday = DateUtil.getYesterday();
        String fileName = String.format(FilePathConst.DAY_SCORE_JSON_FILE, yesterday);
        String content = JsonUtil.getStringFromFile(fileName);
        if (StringUtil.isEmpty(content)) {
            return null;
        }
        MapType type = new MapType(Map.class, new Type[]{String.class, Member.class});
        Map<String, Member> memberMap = JSON.parseObject(content, type);

        if (memberMap == null || memberMap.size() == 0) {
            return null;
        }

        ArrayList<Member> members = (ArrayList<Member>) memberMap.values().stream().sorted(Comparator.comparing(Member::getQualityPoint)).collect(Collectors.toList());
        Collections.reverse(members);
        if (members.size() == 1) {
            if (members.get(0).getQualityPoint() > 0) {
                return members.get(0);
            }
        }
        if (members.size() >= 2) {
            if (members.get(0).getQualityPoint() == members.get(1).getQualityPoint()) {
                return null;
            }
            if (members.get(0).getQualityPoint() > 0) {
                return members.get(0);
            }
        }
        return null;
    }

    public static HashMap<String, Member> createScore(String fileName) {
        log.error("文件地址：{}", FilePathConst.MEMBERS_JSON_FILE);
        return UserUtil.listUer(true, userInfos -> {
            HashMap<String, Member> members = new HashMap<>();
            // 生成今日的统计报告 MemberBean -> Member
            for (UserInfo user : userInfos) {
                Member member = Member.create(user);
                members.put(member.getZentaoId(), member);
            }
            return members;
        });
    }


    public static void clearCurrentDayCache(Collection<Member> members) {
        for (Member member : members) {
            clearCurrentDayCache(member);
        }
    }

    public static void clearCurrentDayCache(Map<String, Member> memberMap) {
        clearCurrentDayCache(memberMap.values());
    }

    public static void clearCurrentDayCache(Member member) {
//        member.setQualityPoint(0);
        member.setOpenedBugs(new ArrayList<>());

        member.setOpen(new HashMap<>());
        member.getOpen().put(JsonKeysConst.S1, 0);
        member.getOpen().put(JsonKeysConst.S2, 0);
        member.getOpen().put(JsonKeysConst.S3, 0);
        member.getOpen().put(JsonKeysConst.S4, 0);

        member.setClose(new HashMap<>());
        member.getClose().put(JsonKeysConst.S1, 0);
        member.getClose().put(JsonKeysConst.S2, 0);
        member.getClose().put(JsonKeysConst.S3, 0);
        member.getClose().put(JsonKeysConst.S4, 0);
    }


}
