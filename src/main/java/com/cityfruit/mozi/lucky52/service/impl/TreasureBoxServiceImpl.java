package com.cityfruit.mozi.lucky52.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.JsonUtil;
import com.cityfruit.mozi.comman.util.StringUtil;
import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
import com.cityfruit.mozi.lucky52.constant.FilePathConst;
import com.cityfruit.mozi.lucky52.constant.JsonKeysConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.entity.RecordInfo;
import com.cityfruit.mozi.lucky52.parameter.BearyChatRequestParam;
import com.cityfruit.mozi.lucky52.service.TreasureBoxService;
import com.cityfruit.mozi.lucky52.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tianyuheng
 * @date 2020/02/13
 */
@Slf4j
@Service
public class TreasureBoxServiceImpl implements TreasureBoxService {

    /**
     * 开宝箱
     *
     * @param bearyChatRequestParam BC 机器人请求参数
     * @return BC 机器人响应参数
     */
    @Override
    public String openTreasureBox(BearyChatRequestParam bearyChatRequestParam) {

        String bearyChatName = bearyChatRequestParam.getUser_name();
        if (StringUtil.isEmpty(bearyChatName)) {
            return BearyChatConst.noOpeningRights(bearyChatName);
        }

        return ScoreUtil.getMembers(false, memberMap -> {
            Member member = null;
            for (Member bean : memberMap.values()) {
                if (bean.getBearyChatId().equals(bearyChatName)) {
                    // 添加操作记录
                    OperationsUtil.addOperation(bean.getName(), OperationsUtil.OPERATION_OPEN_TREASURE_BOX);
                    // 群组判断
                    if (!bearyChatRequestParam.checkChannel(BearyChatConst.OPEN_TREASURE_BOX_GROUP)) {
                        return BearyChatConst.INVALID_GROUP;
                    }
                    if (bean.getQualityPoint() >= TreasureBoxUtil.QP_50) {
                        member = bean;
                        break;
                    }
                }
            }

            if (member == null) {
                return BearyChatConst.noOpeningRights(bearyChatName);
            }

            if (member.isOpened()) {
                return BearyChatConst.opened(bearyChatName);
            }

            float qp = member.getQualityPoint();

            // 获取开宝箱结果
            int qualityFragment = TreasureBoxUtil.getQualityFragmentsByQualityPoint(qp);
            // 标记已开宝箱
            member.setOpened(true);

            UserUtil.getUser(true, member.getName(), user -> {
                if (user != null) {
                    // 增加品质碎片
                    user.setQualityFragment(user.getQualityFragment() + qualityFragment);
                    log.info("【保存品质星碎片】{} {}", user.getName(), user.getQualityFragment());
                } else {
                    log.info("【保存品质星碎片】{} ", "用户不存在");
                }
                return true;
            });

            // 开宝箱记录
            Member temp = member;

            RecordUtil.listRecord(true, recordInfos -> {
                RecordInfo info = new RecordInfo();
                info.setTs(System.currentTimeMillis());
                info.setName(temp.getName());
                info.setQualityPoint(qp);
                info.setOpened(true);
                info.setQualityFragment(qualityFragment);
                info.setExchanged(false);
                recordInfos.add(info);
                return recordInfos;
            });

            log.info("{}，打开了宝箱，获得了 {} 个品质碎片", member.getName(), qualityFragment);
            // 返回推送内容
            return BearyChatConst.openTreasureBoxResult(bearyChatName, qualityFragment);
        });
    }

}
