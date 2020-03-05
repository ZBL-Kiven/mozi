package com.cityfruit.mozi.lucky52.service.impl;

import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.parameter.BearyChatRequestParam;
import com.cityfruit.mozi.lucky52.service.MemberService;
import com.cityfruit.mozi.lucky52.util.BearyChatPushUtil;
import com.cityfruit.mozi.lucky52.util.ScoreUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
@Service
public class MemberServiceImpl implements MemberService {

    /**
     * 获取成员信息
     *
     * @return 成员信息列表
     */
    @Override
    public List<Member> getMembers() {
        List<Member> members = ScoreUtil.getMembers(map -> new ArrayList<>(map.values()));
        // 按 QP 得分排序
        members = members.stream().sorted(Comparator.comparing(Member::getQualityPoint)).collect(Collectors.toList());
        Collections.reverse(members);
        return members;
    }

    @Override
    public String getPushQualityPoint() {
        return BearyChatPushUtil.getPushQualityPoint(getMembers());
    }

    /**
     * 获取个人得分 QP 推送
     *
     * @param bearyChatRequestParam BC 请求参数
     * @return 个人得分 QP 推送内容
     */
    @Override
    public String getQualityPoint(BearyChatRequestParam bearyChatRequestParam) {
        List<Member> members = getMembers();
        for (Member member :
                members) {
            if (member.getBearyChatId().equals(bearyChatRequestParam.getUser_name())) {
                return BearyChatConst.getQualityPoint(member);
            }
        }
        return BearyChatConst.noRights(bearyChatRequestParam.getUser_name());
    }

    /**
     * 获取个人品质碎片数量
     *
     * @param bearyChatRequestParam BC 请求参数
     * @return 个人品质碎片数量
     */
    @Override
    public String getQualityFragment(BearyChatRequestParam bearyChatRequestParam) {
        List<Member> members = getMembers();
        for (Member member :
                members) {
            if (member.getBearyChatId().equals(bearyChatRequestParam.getUser_name())) {
                return BearyChatConst.getQualityFragment(member);
            }
        }
        return BearyChatConst.noRights(bearyChatRequestParam.getUser_name());
    }

}
