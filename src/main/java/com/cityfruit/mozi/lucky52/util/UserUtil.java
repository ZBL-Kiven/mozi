package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSON;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.entity.UserInfo;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;

import java.util.List;

public class UserUtil {

    public static <R> R getUser(boolean save, String userName, Fun<UserInfo, R> callback) {
        //生成今日 members
        List<UserInfo> userInfos = listUser();

        UserInfo user = null;
        for (UserInfo userInfo : userInfos) {
            if (userInfo.getName().equals(userName)) {
                user = userInfo;
                break;
            }
        }
        R r = callback.exec(user);

        if (save) {
            //保存数据
            save(userInfos);
        }
        return r;
    }

    public static <R> R listUer(boolean save, Fun<List<UserInfo>, R> callback) {
        //生成今日 members
        List<UserInfo> userInfos = listUser();
        R r = callback.exec(userInfos);
        if (save) {
            //保存数据
            save(userInfos);
        }
        return r;
    }

    private static List<UserInfo> listUser() {
        String membersStr = JsonUtil.getStringFromFile(FilePathConst.MEMBERS_JSON_FILE);
        return JSON.parseArray(membersStr, UserInfo.class);
    }

    public static void save(List<UserInfo> userInfos) {
        JsonUtil.saveJsonFile(userInfos, FilePathConst.MEMBERS_JSON_FILE);
    }

}
