package com.cityfruit.mozi.lucky52.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.entity.MemberOperation;
import com.cityfruit.mozi.lucky52.parameter.BearyChatRequestParam;
import com.cityfruit.mozi.lucky52.service.MemberService;
import com.cityfruit.mozi.lucky52.util.BearyChatPushUtil;
import com.cityfruit.mozi.lucky52.util.OperationsUtil;
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
                // 添加操作记录
                OperationsUtil.addOperation(member.getName(), OperationsUtil.OPERATION_GET_QUALITY_POINT);
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
                // 添加操作记录
                OperationsUtil.addOperation(member.getName(), OperationsUtil.OPERATION_GET_QUALITY_FRAGMENT);
                return BearyChatConst.getQualityFragment(member);
            }
        }
        return BearyChatConst.noRights(bearyChatRequestParam.getUser_name());
    }

    /**
     * 查看每个用户使用 @eva 三个命令的次数
     *
     * @return 每个用户使用 @eva 三个命令的次数的列表
     */
    @Override
    public List<MemberOperation> getOperations() {
        Map<String, MemberOperation> memberOperationMap = new HashMap<>(16);
        // 当日零点时间戳
        long todayTs = System.currentTimeMillis() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        JSONArray jsonMembers = JsonUtil.getJsonArrayFromFile(FilePathConst.MEMBERS_JSON_FILE);
        // 初始化所有成员
        for (int i = 0; i < jsonMembers.size(); i++) {
            MemberOperation memberOperation = new MemberOperation();
            memberOperation.setName(jsonMembers.getJSONObject(i).getString(JsonKeysConst.NAME));
            memberOperationMap.put(memberOperation.getName(), memberOperation);
        }
        // 遍历操作记录
        JSONArray jsonOperations = JsonUtil.getJsonArrayFromFile(FilePathConst.OPERATIONS_JSON_FILE);
        for (int i = 0; i < jsonOperations.size(); i++) {
            JSONObject jsonOperation = jsonOperations.getJSONObject(i);
            String name = jsonOperation.getString(JsonKeysConst.NAME);
            String operation = jsonOperation.getString(JsonKeysConst.OPERATION);
            switch (operation) {
                case OperationsUtil.OPERATION_OPEN_TREASURE_BOX: {
                    memberOperationMap.get(name).setTotalOpenTreasureBox(memberOperationMap.get(name).getTotalOpenTreasureBox() + 1);
                    // 当天统计
                    if (jsonOperation.getLongValue(JsonKeysConst.TS) > todayTs) {
                        memberOperationMap.get(name).setDailyOpenTreasureBox(memberOperationMap.get(name).getDailyOpenTreasureBox() + 1);
                    }
                    break;
                }
                case OperationsUtil.OPERATION_GET_QUALITY_POINT: {
                    memberOperationMap.get(name).setTotalGetQualityPoint(memberOperationMap.get(name).getTotalGetQualityPoint() + 1);
                    if (jsonOperation.getLongValue(JsonKeysConst.TS) > todayTs) {
                        memberOperationMap.get(name).setDailyGetQualityPoint(memberOperationMap.get(name).getDailyGetQualityPoint() + 1);
                    }
                    break;
                }
                case OperationsUtil.OPERATION_GET_QUALITY_FRAGMENT: {
                    memberOperationMap.get(name).setTotalGetQualityFragment(memberOperationMap.get(name).getTotalGetQualityFragment() + 1);
                    if (jsonOperation.getLongValue(JsonKeysConst.TS) > todayTs) {
                        memberOperationMap.get(name).setDailyGetQualityFragment(memberOperationMap.get(name).getDailyGetQualityFragment() + 1);
                    }
                    break;
                }
            }
        }
        // 封装统计结果
        List<MemberOperation> memberOperationList = new ArrayList<>(16);
        for (String name : memberOperationMap.keySet()) {
            memberOperationList.add(memberOperationMap.get(name));
        }
        return memberOperationList;
    }

}
