package com.cityfruit.mozi.lucky52.entity;

import lombok.Data;

@Data
public class StatBug {

    /**
     * BUG 级别
     */
    private String severity;

    /**
     * Bug Id
     */
    private String id;

    /**
     * 创建人
     */
    private String openUser;

    /**
     * 关闭人
     */
    private String closeUser;

    /**
     * 已确定
     */
    private boolean opened;

    /**
     * 已关闭
     */
    private boolean close;
}
