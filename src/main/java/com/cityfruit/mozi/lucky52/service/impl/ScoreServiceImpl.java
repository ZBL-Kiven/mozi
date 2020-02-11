package com.cityfruit.mozi.lucky52.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.HttpUtil;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.constant.BugConstants;
import com.cityfruit.mozi.lucky52.constant.FilePath;
import com.cityfruit.mozi.lucky52.constant.JsonField;
import com.cityfruit.mozi.lucky52.entity.Bug;
import com.cityfruit.mozi.lucky52.parameter.ZentaoNoticeRequestParam;
import com.cityfruit.mozi.lucky52.service.ScoreService;
import com.cityfruit.mozi.lucky52.util.Utils;
import com.cityfruit.mozi.lucky52.util.ZentaoUtil;
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
        Bug bug = new Bug(jsonBug.getJSONObject("bug"));
        // 读取得分统计 json 文件
        JSONObject jsonScore = JsonUtil.getJsonFromFile(FilePath.SCORE_JSON_FILE);
        assert jsonScore != null;
        // 校验日期
        Utils.checkDate(jsonScore);
        // 更新 VP 值并保存
        Utils.updateValuePointAndSave(jsonScore, bug, actionType);
    }

}
