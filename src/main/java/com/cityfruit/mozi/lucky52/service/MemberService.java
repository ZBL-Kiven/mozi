package com.cityfruit.mozi.lucky52.service;

import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.parameter.BearyChatRequestParam;

import java.util.List;

/**
 * @author tianyuheng
 */
public interface MemberService {

    /**
     * 获取成员信息
     *
     * @return 成员信息列表
     */
    List<Member> getMembers();

    /**
     * 获取推送所有人 QP
     *
     * @return 所有人 QP
     */
    String getPushQualityPoint();

    /**
     * 获取个人得分 QP 推送
     *
     * @param bearyChatRequestParam BC 请求参数
     * @return 个人得分 QP 推送内容
     */
    String getQualityPoint(BearyChatRequestParam bearyChatRequestParam);

    /**
     * 获取个人品质碎片数量
     *
     * @param bearyChatRequestParam BC 请求参数
     * @return 个人品质碎片数量
     */
    String getQualityFragment(BearyChatRequestParam bearyChatRequestParam);

}
