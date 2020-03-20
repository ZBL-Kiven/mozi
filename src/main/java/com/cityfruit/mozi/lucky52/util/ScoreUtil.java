package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSON;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.entity.UserInfo;
import com.cityfruit.mozi.lucky52.tools.MapType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        // currentDay = "2020-03-30";
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
            //同步 确定 和 关闭
            SyncBug.sync(memberMap, currentDay);
            SyncBug.syncPush(memberMap);
        }

        R r = callBack.exec(memberMap);
        //保存数据
        JsonUtil.saveJsonFile(memberMap, fileName);
        return r;
    }

    private static void calculateZombie(Map<String, Member> members) {
        for (Member value : members.values()) {
            value.getStatus().setTaskSuccess10(value.getZombieCount() == 0);
        }
    }

    public static <R> R getMembers(Fun<Map<String, Member>, R> callBack) {
        return getMembers(false, callBack);
    }

    public static HashMap<String, Member> createScore(String fileName) {
        log.info("文件地址：{}", FilePathConst.MEMBERS_JSON_FILE);
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

    public static void clearCurrentDayCache(Member member) {
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
        member.setZombieCount(0);
    }

}
