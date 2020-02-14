package com.cityfruit.mozi.comman.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.BugConst;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
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
        initQualityPoint();
    }

    private void initQualityPoint() {
        log.info("[开始统计当日 Quality Point 获取情况……]");
        // 校验日期、清空统计
        Utils.checkDateAndClearData();
        // 从 JSON 文件中获取用户
        JSONObject jsonScore = JsonUtil.getJsonObjectFromFile(FilePathConst.SCORE_JSON_FILE);
        // 获取产品列表
        Map<String, Object> products = ZentaoUtil.getProducts();
        // 遍历产品列表，获取当日创建
        for (String productId : products.keySet()) {
            // 统计未确认 BUG
            log.info("[统计未确认……] Product ID: {}", productId);
            String searchResult = ZentaoUtil.getSearchResult(ZentaoUtil.SEARCH_TODAY_OPENED_WITHOUT_CONFIRMED, ZentaoUtil.zentaoCookie, productId);
            JSONArray jsonBugs = JSONObject.parseObject(searchResult).getJSONObject("data").getJSONArray("bugs");
            // 遍历 BUG 列表，更新并保存未确认的 BUG 列表
            for (int i = 0; i < jsonBugs.size(); i++) {
                Bug bug = new Bug(jsonBugs.getJSONObject(i));
                log.info("{}", bug);
                Utils.updateQualityPointAndSave(jsonScore, bug, BugConst.ACTION_TYPE_OPEN, false);
            }
            // 统计确认 BUG 得分
            log.info("[统计确认……] Product ID: {}", productId);
            searchResult = ZentaoUtil.getSearchResult(ZentaoUtil.SEARCH_TODAY_OPENED_WITH_CONFIRMED, ZentaoUtil.zentaoCookie, productId);
            jsonBugs = JSONObject.parseObject(searchResult).getJSONObject("data").getJSONArray("bugs");
            // 遍历 BUG 列表，更新并保存 QP 值
            for (int i = 0; i < jsonBugs.size(); i++) {
                Bug bug = new Bug(jsonBugs.getJSONObject(i));
                log.info("{}", bug);
                Utils.updateQualityPointAndSave(jsonScore, bug, BugConst.ACTION_TYPE_CONFIRM, false);
            }
            // 统计关闭 BUG 得分
            log.info("[统计关闭……] Product ID: {}", productId);
            searchResult = ZentaoUtil.getSearchResult(ZentaoUtil.SEARCH_TODAY_CLOSED, ZentaoUtil.zentaoCookie, productId);
            jsonBugs = JSONObject.parseObject(searchResult).getJSONObject("data").getJSONArray("bugs");
            // 遍历 BUG 列表，更新并保存 QP 值
            for (int i = 0; i < jsonBugs.size(); i++) {
                Bug bug = new Bug(jsonBugs.getJSONObject(i));
                log.info("{}", bug);
                Utils.updateQualityPointAndSave(jsonScore, bug, BugConst.ACTION_TYPE_CLOSE, false);
            }
        }
        log.info("[当日 Quality Point 获取情况统计完毕]");
    }

}
