package com.cityfruit.mozi.lucky52.entity;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.lucky52.constant.BugConst;
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

}
