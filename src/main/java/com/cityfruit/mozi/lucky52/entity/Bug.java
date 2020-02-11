package com.cityfruit.mozi.lucky52.entity;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.lucky52.constant.BugConstants;
import lombok.Data;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
@Data
public class Bug {

    /**
     * BUG ID
     */
    private String id;

    /**
     * 严重级别
     */
    private String severity;

    /**
     * 创建人
     */
    private String openedBy;

    /**
     * 创建日期
     */
    private String openedDate;

    /**
     * 是否需要确认
     */
    private boolean needConfirm;

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
     * 关闭日期
     */
    private String closedDate;

    public Bug(JSONObject bugDetail) {
        id = bugDetail.getString("id");
        severity = bugDetail.getString("severity");
        openedBy = bugDetail.getString("openedBy");
        openedDate = bugDetail.getString("openedDate");
        needConfirm = bugDetail.getBoolean("needconfirm");
        resolution = bugDetail.getString("resolution");
        resolvedBy = bugDetail.getString("resolvedBy");
        closedBy = bugDetail.getString("closedBy");
        closedDate = bugDetail.getString("closedDate");
    }

    /**
     * 校验是否为有效创建/关闭的 BUG
     * 当 BUG 被确认后，判断此 BUG 是否为今日创建、创建人是否为 QA
     * 当 BUG 被关闭后，判断此 BUG 的解决方式是否符合要求、关闭人是否为 QA
     *
     * @param members QA 成员得分 JSON 对象
     * @return 有效：true；无效：false
     */
    public boolean checkValid(String actionType, JSONObject members)  {
        long today = DateUtil.getTodayTimeMillis();
        return (actionType.equals(BugConstants.ACTION_TYPE_CONFIRM) && (DateUtil.getTimeMillisFromBug(openedDate) > today && members.containsKey(openedBy)))
                || (actionType.equals(BugConstants.ACTION_TYPE_CLOSE) && (!resolvedBy.isEmpty()) && (BugConstants.BUG_RESOLUTIONS.contains(resolution) && members.containsKey(closedBy)));
    }

}
