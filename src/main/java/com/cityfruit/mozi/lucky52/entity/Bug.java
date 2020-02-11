package com.cityfruit.mozi.lucky52.entity;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.lucky52.constant.BugConstants;
import lombok.Data;

import java.text.ParseException;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
@Data
public class Bug {

    /**
     * 创建人
     */
    private String openedBy;

    /**
     * 创建日期
     */
    private String openedDate;

    /**
     * 确认人
     */
    private String confirmedBy;

    /**
     * 解决人
     */
    private String resolvedBy;

    /**
     * 解决方案
     */
    private String resolution;

    /**
     * 关闭人
     */
    private String closedBy;

    /**
     * 严重级别
     */
    private String severity;

    public Bug(JSONObject jsonBug) {
        JSONObject bugDetail = jsonBug.getJSONObject("bug");
        openedBy = bugDetail.getString("openedBy");
        openedDate = bugDetail.getString("openedDate");
        resolution = bugDetail.getString("resolution");
        resolvedBy = bugDetail.getString("resolvedBy");
        closedBy = bugDetail.getString("closedBy");
        severity = bugDetail.getString("severity");
    }

    /**
     * 校验是否为有效创建/关闭的 BUG
     * 当 BUG 被确认后，判断此 BUG 是否为今日创建、创建人是否为 QA
     * 当 BUG 被关闭后，判断此 BUG 的解决方式是否符合要求、关闭人是否为 QA
     *
     * @param members QA 成员得分 JSON 对象
     * @return 有效：true；无效：false
     * @throws ParseException 时间格式化异常
     */
    public boolean checkValid(String actionType, JSONObject members) throws ParseException {
        return (actionType.equals(BugConstants.ACTION_TYPE_CONFIRM) && (DateUtil.getTimeMillisFromBug(openedDate) > DateUtil.getTodayTimeMillis() && members.containsKey(openedBy)))
                || (actionType.equals(BugConstants.ACTION_TYPE_CLOSE) && (!resolvedBy.isEmpty()) && (BugConstants.BUG_RESOLUTIONS.contains(resolution) && members.containsKey(closedBy)));
    }

}
