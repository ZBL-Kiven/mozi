package com.cityfruit.mozi.lucky52.service;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.lucky52.entity.Bug;
import com.cityfruit.mozi.lucky52.parameter.ZentaoNoticeRequestParam;

/**
 * @author tianyuheng
 * @date 2020/02/06
 */
public interface ScoreService {

    /**
     * 处理从禅道接收到的通知
     *
     * @param zentaoNoticeRequestParam 禅道通知请求参数
     */
    void score(ZentaoNoticeRequestParam zentaoNoticeRequestParam);

}
