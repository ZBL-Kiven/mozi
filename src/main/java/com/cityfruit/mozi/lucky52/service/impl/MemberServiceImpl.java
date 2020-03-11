package com.cityfruit.mozi.lucky52.service.impl;

import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.parameter.BearyChatRequestParam;
import com.cityfruit.mozi.lucky52.service.MemberService;
import com.cityfruit.mozi.lucky52.util.BearyChatPushUtil;
import com.cityfruit.mozi.lucky52.util.ScoreUtil;
import com.cityfruit.mozi.lucky52.util.Utils;
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
        int memberSize = members.size();
        //formart 所需的内容
        int contentType = -1;
        //击败的百分比
        String perOfWins = "";
        float scoreDiff = 0.0f;
        if (memberSize > 0) {
            Member firstScoreMember = members.get(0);
            Member lastScoreMember = members.get(memberSize - 1);
            float firstMemberScore = firstScoreMember.getQualityPoint();
            float lastMemberScore = lastScoreMember.getQualityPoint();
            ArrayList<Float> allScores = getQualityPoints(members);
            if (firstMemberScore == lastMemberScore) {
                contentType = BearyChatConst.TYPE_QP_SCROE_EQUEALS;
            }
            for (Member member : members) {
                float currentScore = member.getQualityPoint();
                int firstIndex = allScores.indexOf(currentScore);
                if (contentType == -1) {
                    //第一名
                    if (firstIndex == 0) {
                        int lastIndex = allScores.lastIndexOf(currentScore);
                        contentType = BearyChatConst.TYPE_QP_SCORE_FIRST;
                        perOfWins = Utils.getPercentOfWins(lastIndex + 1, memberSize);
                    } else {
                        contentType = BearyChatConst.TYPE_QP_SCORE_NOT_FIRST;
                        scoreDiff = firstMemberScore - currentScore;
                    }
                }
                if (member.getBearyChatId().equals(bearyChatRequestParam.getUser_name())) {
                    return BearyChatConst.getQualityPoint(member,firstScoreMember, currentScore, contentType, perOfWins, scoreDiff);
                }
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

    /**
     * 获取所有人员的 qp 分数集合
     *
     * @return
     */
    private ArrayList<Float> getQualityPoints(List<Member> members) {
        ArrayList<Float> qpScores = new ArrayList<>();
        if (members.size() > 0) {
            for (Member member : members) {
                float currentQpScore = member.getQualityPoint();
                qpScores.add(currentQpScore);
            }
            return qpScores;
        } else {
            return qpScores;
        }

    }
}
