package com.cityfruit.mozi.lucky52.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.HttpUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.constant.BugConstants;
import com.cityfruit.mozi.lucky52.constant.FilePath;
import com.cityfruit.mozi.lucky52.entity.Bug;
import com.cityfruit.mozi.lucky52.parameter.ZentaoNoticeRequestParam;
import com.cityfruit.mozi.lucky52.service.ScoreService;
import com.cityfruit.mozi.lucky52.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author tianyuheng
 * @date 2020/02/06
 */
@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService {

    /**
     * 处理从禅道接收到的通知
     *
     * @param zentaoNoticeRequestParam 禅道通知请求参数
     */
    @Override
    public void score(ZentaoNoticeRequestParam zentaoNoticeRequestParam) {
        String text = zentaoNoticeRequestParam.getText();
        // 禅道通知信息为空，跳出
        if (text.isEmpty()) {
            return;
        }
        // 操作类型
        String actionType = StringUtil.getZentaoNoticeType(text);
        // 操作类型不是确认/关闭 BUG，跳出
        if (actionType == null) {
            return;
        }
        String bugViewString = HttpUtil.get(StringUtil.getZentaoBugUrl(text));
        assert !bugViewString.startsWith("<html>");
        JSONObject jsonBug = JSONObject.parseObject(bugViewString).getJSONObject("data");
        assert jsonBug != null;
        // Bug 对象创建
        Bug bug = new Bug(jsonBug);
        // 读取得分统计 json 文件
        JSONObject jsonScore = null;
        try {
            jsonScore = JsonUtil.getJsonFromFile(FilePath.SCORE_JSON_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert jsonScore != null;
        // 校验日期
        Utils.checkDate(jsonScore);
        JSONObject jsonMembers = jsonScore.getJSONObject("members");
        try {
            // 校验是否为有效创建/关闭
            if (bug.checkValid(actionType, jsonMembers)) {
                // 判断创建/关闭，增加相应分数
                if (actionType.equals(BugConstants.ACTION_TYPE_CONFIRM)) {
                    // 成员
                    JSONObject jsonMember = jsonMembers.getJSONObject(bug.getOpenedBy());
                    float valuePoint = jsonMember.getFloatValue("valuePoint");
                    // 增加 VP 值
                    float valuePointAdd = BugConstants.VALUE_POINTS.get(actionType).get(bug.getSeverity());
                    valuePoint += valuePointAdd;
                    jsonMember.put("valuePoint", valuePoint);
                    log.info("{} 获得 Value Point：{} 分，当前总共 {} 分", jsonMember.getString("name"), valuePointAdd, valuePoint);
                } else if (actionType.equals(BugConstants.ACTION_TYPE_CLOSE)) {
                    JSONObject jsonMember = jsonMembers.getJSONObject(bug.getClosedBy());
                    float valuePoint = jsonMember.getFloatValue("valuePoint");
                    float valuePointAdd = BugConstants.VALUE_POINTS.get(actionType).get(bug.getSeverity());
                    valuePoint += valuePointAdd;
                    jsonMember.put("valuePoint", valuePoint);
                    log.info("{} 获得 Value Point：{} 分，当前总共 {} 分", jsonMember.getString("name"), valuePointAdd, valuePoint);
                }
                // 保存 json 文件
                try {
                    JsonUtil.saveJsonFile(jsonScore, FilePath.SCORE_JSON_FILE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
