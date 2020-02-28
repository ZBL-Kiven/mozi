package com.cityfruit.mozi.lucky52.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.HttpUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.constant.BugConst;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.RegexStringConst;
import com.cityfruit.mozi.lucky52.entity.Bug;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.parameter.ZentaoNoticeRequestParam;
import com.cityfruit.mozi.lucky52.service.ScoreService;
import com.cityfruit.mozi.lucky52.util.ScoreUtil;
import com.cityfruit.mozi.lucky52.util.Utils;
import com.cityfruit.mozi.lucky52.util.ZentaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        // 获取 BUG 操作类型
        String actionType = null;
        String[] textArray = text.split(RegexStringConst.ZENTAO_NOTICE_TYPE_SPLIT);
        if (textArray[0].contains(BugConst.ACTION_TYPE_CONFIRM)) {
            actionType = BugConst.ACTION_TYPE_CONFIRM;
        } else if (textArray[0].contains(BugConst.ACTION_TYPE_CLOSE)) {
            actionType = BugConst.ACTION_TYPE_CLOSE;
        } else if (textArray[0].contains(BugConst.ACTION_TYPE_OPEN)) {
            actionType = BugConst.ACTION_TYPE_OPEN;
        } else if (textArray[0].contains(BugConst.ACTION_TYPE_RESOLVE)) {
            actionType = BugConst.ACTION_TYPE_RESOLVE;
        }
        // 操作类型不是创建/确认/解决/关闭 BUG，跳出
        if (actionType == null) {
            return;
        }
        // 请求禅道 BUG 详情链接
        String bugViewString = HttpUtil.get(StringUtil.getZentaoBugUrl(text), ZentaoUtil.zentaoCookie);
        // 未登录禅道，登录、重新请求
        if (bugViewString.startsWith(ZentaoUtil.ZENTAO_NOT_LOGGED_IN_PREFIX)) {
            ZentaoUtil.login();
            HttpUtil.get(StringUtil.getZentaoBugUrl(text), ZentaoUtil.zentaoCookie);
        }
        // 获取 FastJson 对象的 BUG 详情
        JSONObject jsonBug = JSONObject.parseObject(bugViewString).getJSONObject("data");
        assert jsonBug != null;
        // Bug 对象创建
        Bug bug = JSON.parseObject(jsonBug.getJSONObject("bug").toJSONString(), Bug.class);

        String type = actionType;


        ScoreUtil.getMembers(map -> {
            // 更新 QP 值并保存
            Utils.updateQualityPointAndSave(map, bug, type, false);
            return true;
        });
    }

}
