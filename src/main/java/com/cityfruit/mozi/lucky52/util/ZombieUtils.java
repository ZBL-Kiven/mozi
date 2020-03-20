package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSON;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.entity.UserInfo;
import com.cityfruit.mozi.lucky52.entity.ZombieInfo;
import com.cityfruit.mozi.lucky52.tools.MapType;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ZombieUtils {

    public static <R> R mapZombie(boolean save, String currentDay, Fun<Map<String, ZombieInfo>, R> callback) {
        //生成今日 members
        Map<String, ZombieInfo> zombies = mapZombie(currentDay);
        if (zombies == null) {
            log.info("准备生成 僵尸 BUG 文件 ：{}",currentDay);
            zombies = UserUtil.listUer(false, userInfos -> {
                Map<String, ZombieInfo> map = new HashMap<>();
                for (UserInfo bean : userInfos) {
                    map.put(bean.getBearyChatId(), new ZombieInfo(bean.getBearyChatId(), bean.getName()));
                }
                return map;
            });
        }else{
            log.info("获取 僵尸 BUG 缓存文件 ：{}",currentDay);
        }
        R r = callback.exec(zombies);
        if (save) {
            //保存数据
            save(zombies, currentDay);
        }
        return r;
    }

    private static Map<String, ZombieInfo> mapZombie(String currentDay) {
        MapType type = new MapType(Map.class, new Type[]{String.class, ZombieInfo.class});
        String content = JsonUtil.getStringFromFile(String.format(FilePathConst.ZOMBIE_JSON_FILE, currentDay));
        if (StringUtil.isEmpty(content)) {
            return null;
        }
        return JSON.parseObject(content, type);
    }

    public static void save(Map<String, ZombieInfo> userInfos, String currentDay) {
        JsonUtil.saveJsonFile(userInfos, String.format(FilePathConst.ZOMBIE_JSON_FILE, currentDay));
    }

    public static boolean exists() {
        return exists(DateUtil.getCurrentDay());
    }

    public static boolean exists(String currentDay) {
        return new File(String.format(FilePathConst.ZOMBIE_JSON_FILE, currentDay)).exists();
    }

}
