package com.cityfruit.mozi.comman.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.BugConstants;
import com.cityfruit.mozi.lucky52.constant.FilePath;
import com.cityfruit.mozi.lucky52.constant.JsonField;
import com.cityfruit.mozi.lucky52.entity.Bug;
import com.cityfruit.mozi.lucky52.util.Utils;
import com.cityfruit.mozi.lucky52.util.ZentaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author tianyuheng
 * @date 2020/02/11
 */
@Slf4j
@Component
public class Runner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        initValuePoint();
    }

    private void initValuePoint() {
        log.info("[开始统计当日 Value Point 获取情况……]");
        // 从 JSON 文件中获取用户
        JSONObject jsonScore = JsonUtil.getJsonFromFile(FilePath.SCORE_JSON_FILE);
        assert jsonScore != null;
        JSONObject jsonMembers = jsonScore.getJSONObject(JsonField.MEMBERS);
        // 更新日期
        jsonScore.put(JsonField.TS, DateUtil.getTodayTimeMillis());
        // 清零统计
        for (String member : jsonMembers.keySet()) {
            jsonMembers.getJSONObject(member).put(JsonField.VALUE_POINT, 0);
        }
        // 获取产品列表
        Map<String, Object> products = ZentaoUtil.getProducts();
        // 遍历产品列表，获取当日创建
        for (String productId : products.keySet()) {
            // 统计创建 BUG 得分
            String searchResult = ZentaoUtil.getSearchResult(ZentaoUtil.SEARCH_TODAY_OPENED_WITH_CONFIRMED, ZentaoUtil.zentaoCookie, productId);
            JSONArray jsonBugs = JSONObject.parseObject(searchResult).getJSONObject("data").getJSONArray("bugs");
            // 遍历 BUG 列表，更新并保存 VP 值
            for (int i = 0; i < jsonBugs.size(); i++) {
                Bug bug = new Bug(jsonBugs.getJSONObject(i));
                Utils.updateValuePointAndSave(jsonScore, bug, BugConstants.ACTION_TYPE_CONFIRM, false);
            }
            // 统计关闭 BUG 得分
            searchResult = ZentaoUtil.getSearchResult(ZentaoUtil.SEARCH_TODAY_CLOSED, ZentaoUtil.zentaoCookie, productId);
            jsonBugs = JSONObject.parseObject(searchResult).getJSONObject("data").getJSONArray("bugs");
            // 遍历 BUG 列表，更新并保存 VP 值
            for (int i = 0; i < jsonBugs.size(); i++) {
                Bug bug = new Bug(jsonBugs.getJSONObject(i));
                Utils.updateValuePointAndSave(jsonScore, bug, BugConstants.ACTION_TYPE_CLOSE, false);
            }
        }
        log.info("[当日 Value Point 获取情况统计完毕]");
    }

}
