package com.cityfruit.mozi.lucky52.entity;

import lombok.Data;

@Data
public class BugAction {
    
    /**
     * 操作人
     */
    private String actor;

    /**
     * 操作日期
     */
    private String date;

    /**
     * 操作状态
     */
    private BugStatus action;

    /**
     * 操作ID
     */
    private int id;
}
