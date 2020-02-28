package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSON;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.bean.MemberBean;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.tools.MapType;

import java.lang.reflect.Type;
import java.util.*;

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

    public static <R> R getMembers(Fun<Map<String, Member>, R> callBack) {
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
        R r = callBack.exec(memberMap);
        //保存数据
        JsonUtil.saveJsonFile(memberMap, fileName);
        return r;
    }

    public static HashMap<String, Member> createScore(String fileName) {
        HashMap<String, Member> members = new HashMap<>();
        //生成今日 members
        String membersStr = JsonUtil.getStringFromFile(FilePathConst.MEMBERS_JSON_FILE);

        List<MemberBean> memberBeans = JSON.parseArray(membersStr, MemberBean.class);

        // 生成今日的统计报告 MemberBean -> Member
        for (MemberBean memberBean : memberBeans) {
            Member member = Member.create(memberBean);
            members.put(member.getZentaoId(), member);
        }
        return members;
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
