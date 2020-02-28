package com.cityfruit.mozi.comman.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.bean.MemberBean;
import com.cityfruit.mozi.lucky52.constant.BugConst;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.entity.Bug;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.tools.MapType;
import com.cityfruit.mozi.lucky52.util.ScoreUtil;
import com.cityfruit.mozi.lucky52.util.Utils;
import com.cityfruit.mozi.lucky52.util.ZentaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
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
//        initQualityPoint();
        init();
    }

    private void init() {
        ScoreUtil.setListener(ScoreUtil::createScore);

        ScoreUtil.getMembers(map -> {
            //清空数据
            ScoreUtil.clearCurrentDayCache(map);
            syncBug(map);
        });
    }

    private void syncBug(Map<String, Member> memberMap) {
        // 获取产品列表
        Map<String, Object> products = ZentaoUtil.getProducts();
        // 遍历产品列表，获取当日创建
        for (String productId : products.keySet()) {

            // 统计未确认 BUG // 遍历 BUG 列表，更新并保存未确认的 BUG 列表
            log.info("[统计未确认……] Product ID: {}", productId);
            execZentaoSearch(memberMap, ZentaoUtil.SEARCH_TODAY_OPENED_WITHOUT_CONFIRMED, productId, BugConst.ACTION_TYPE_OPEN);

            // 统计确认 BUG 得分 // 遍历 BUG 列表，更新并保存 QP 值
            log.info("[统计确认……] Product ID: {}", productId);
            execZentaoSearch(memberMap, ZentaoUtil.SEARCH_TODAY_OPENED_WITH_CONFIRMED, productId, BugConst.ACTION_TYPE_CONFIRM);

            // 统计关闭 BUG 得分 // 遍历 BUG 列表，更新并保存 QP 值
            log.info("[统计关闭……] Product ID: {}", productId);
            execZentaoSearch(memberMap, ZentaoUtil.SEARCH_TODAY_CLOSED, productId, BugConst.ACTION_TYPE_CLOSE);
        }

        log.info("[当日 Quality Point 获取情况统计完毕]");
    }


    private void execZentaoSearch(Map<String, Member> memberMap, Integer type, String productId, String actionType) {
        String searchResult = ZentaoUtil.getSearchResult(type, ZentaoUtil.zentaoCookie, productId);
        execUpdate(memberMap, searchResult, actionType);
    }

    private void execUpdate(Map<String, Member> memberMap, String content, String type) {
        String bugsStr = JSONObject.parseObject(content).getJSONObject("data").getJSONArray("bugs").toJSONString();
        List<Bug> bugs = JSON.parseArray(bugsStr, Bug.class);
        for (Bug bug : bugs) {
            log.info("{}", bug);
            Utils.updateQualityPointAndSave(memberMap, bug, type, false);
        }
    }

}
