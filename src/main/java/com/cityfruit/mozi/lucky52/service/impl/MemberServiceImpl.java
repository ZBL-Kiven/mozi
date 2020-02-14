package com.cityfruit.mozi.lucky52.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<Member> members = new ArrayList<>(4);
        // 从 JSON 文件中获取用户
        JSONObject jsonScore = JsonUtil.getJsonObjectFromFile(FilePathConst.SCORE_JSON_FILE);
        assert jsonScore != null;
        JSONObject jsonMembers = jsonScore.getJSONObject(JsonKeysConst.MEMBERS);
        // 将用户添加至返回列表
        for (String memberName : jsonMembers.keySet()) {
            Member member = new Member(jsonMembers.getJSONObject(memberName));
            members.add(member);
        }
        return members;
    }

}
