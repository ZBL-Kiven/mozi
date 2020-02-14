package com.cityfruit.mozi.lucky52.service;

import com.cityfruit.mozi.lucky52.parameter.BearyChatRequestParam;

/**
 * @author tianyuheng
 * @date 2020/02/13
 */
public interface TreasureBoxService {

    /**
     * 开宝箱
     *
     * @param bearyChatRequestParam BC 机器人请求参数
     * @return BC 机器人响应参数
     */
    String openTreasureBox(BearyChatRequestParam bearyChatRequestParam);

}
