package com.cityfruit.mozi.lucky52.service.impl;

import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.entity.TrelloMoveInfo;
import com.cityfruit.mozi.lucky52.parameter.TrelloActionRequestParam;
import com.cityfruit.mozi.lucky52.service.TrelloService;
import com.cityfruit.mozi.lucky52.util.BearyChatPushUtil;
import com.cityfruit.mozi.lucky52.util.ScoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class TrelloServiceImpl implements TrelloService {

    @Override
    public void move(TrelloActionRequestParam requestParam) {
        boolean memberIsNull = ScoreUtil.getMembers(map -> {
            Collection<Member> values = map.values();
            for (Member value : values) {
                //找出 trello 對應的用戶
                String trelloId = value.getTrelloId();
                if (trelloId != null && trelloId.equals(requestParam.getActionUserId())) {
                    checkMeetConditions(value, requestParam);
                    return true;
                }
            }
            return false;
        });
        if (!memberIsNull) {
            log.info("【trello 上的用戶數據不同步】{} ", requestParam.getActionUserId());
        }
    }

    /**
     * 進行 判斷是否符合增加 QP 條件
     *
     * @param member
     * @param requestParam
     */
    private void checkMeetConditions(Member member, TrelloActionRequestParam requestParam) {
        if (member != null
                && isMoveSuccessful(requestParam.getFromGroup(), requestParam.getToGroup())
                && isRepeatMove(member, requestParam)) {
            setMemberTrello(member, requestParam);
            //進行推送消息到 BC
            float qualityPoint = member.getQualityPoint();
            // 推送至 BC 群组
            BearyChatPushUtil.pushBcTrelloMove(member.getBearyChatId(), member.getQualityPoint());
        }
    }

    /**
     * 是否有重複的滿足條件
     *
     * @param member
     * @param requestParam
     * @return
     */
    private Boolean isRepeatMove(Member member, TrelloActionRequestParam requestParam) {
        List<TrelloMoveInfo> trelloMove = member.getTrelloMove();
        for (TrelloMoveInfo trelloMoveInfo : trelloMove) {
            if (trelloMoveInfo.getTrelloId().equals(requestParam.getTrelloId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否滿足拖動並且觸發計分的條件
     *
     * @param fromGroup
     * @param toGroup
     * @return
     */
    private Boolean isMoveSuccessful(String fromGroup, String toGroup) {
        return fromGroup.contains("「待 QA 验收」") && toGroup.contains("「待 PO 验收」");
    }

    /**
     * 添加拖動紀錄
     *
     * @param member
     * @param requestParam
     */
    private void setMemberTrello(Member member, TrelloActionRequestParam requestParam) {
        TrelloMoveInfo trelloMoveInfo = new TrelloMoveInfo();
        trelloMoveInfo.setFromGroup(requestParam.getFromGroup());
        trelloMoveInfo.setMoveTs(System.currentTimeMillis());
        trelloMoveInfo.setToGroup(requestParam.getToGroup());
        trelloMoveInfo.setTrelloId(requestParam.getTrelloId());
        member.getTrelloMove().add(trelloMoveInfo);
    }
}
