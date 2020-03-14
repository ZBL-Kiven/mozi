package com.cityfruit.mozi.lucky52.entity;

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
     * 是否解决
     */
    private boolean confirmed;

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

    /**
     * 指派人 用于统计 僵尸bug
     */
    private String assignedTo;

    /**
     * 当前 BUG 状态
     */
    private String status;

    /**
     * 指派日期
     */
    private String assignedDate;
    /**
     * BUG 类型
     */
    private String type;
    /**
     * 激活次数
     */
    private String activatedCount;
    /**
     * 最后激活一次日期
     */
    private String activatedDate;
    /**
     * 最后解决日期
     */
    private String resolvedDate;
    /**
     * 最后一次操作人
     */
    private String lastEditedBy;

    /**
     * 最后一次修改日期
     */
    private String lastEditedDate;
}
