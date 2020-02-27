package com.cityfruit.mozi.lucky52.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.parameter.BearyChatRequestParam;
import com.cityfruit.mozi.lucky52.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        List<Member> members = new ArrayList<>(8);
        // 从 JSON 文件中获取用户
        JSONObject jsonScore = JsonUtil.getJsonObjectFromFile(FilePathConst.SCORE_JSON_FILE);
        assert jsonScore != null;
        JSONObject jsonMembers = jsonScore.getJSONObject(JsonKeysConst.MEMBERS);
        // 将用户添加至返回列表
        for (String memberName : jsonMembers.keySet()) {
            Member member = new Member(jsonMembers.getJSONObject(memberName));
            members.add(member);
        }
        // 按 QP 得分排序
        return members.stream().sorted(Comparator.comparing(Member::getQualityPoint)).collect(Collectors.toList());
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
