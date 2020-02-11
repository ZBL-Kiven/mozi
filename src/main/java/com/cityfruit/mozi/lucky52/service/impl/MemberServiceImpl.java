package com.cityfruit.mozi.lucky52.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.FilePath;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.service.MemberService;
import com.cityfruit.mozi.lucky52.util.Utils;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        JSONObject jsonScore = new JSONObject();
        try {
            // 从 JSON 文件中获取用户
            jsonScore = JsonUtil.getJsonFromFile(FilePath.SCORE_JSON_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert jsonScore != null;
        // 校验日期
        Utils.checkDate(jsonScore);
        JSONObject jsonMembers = jsonScore.getJSONObject("members");
        // 将用户添加至返回列表
        for (String memberName : jsonMembers.keySet()) {
            Member member = new Member(jsonMembers.getJSONObject(memberName));
            members.add(member);
        }
        return members;
    }

}
