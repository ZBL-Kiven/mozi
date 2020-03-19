package com.cityfruit.mozi.lucky52.entity;

import com.cityfruit.mozi.lucky52.enums.BugStatus;
import lombok.Data;

@Data
public class BugAction {
    
    /**
     * 操作人
     */
    private String actor;

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BugStatus getAction() {
        return action;
    }

    public void setAction(BugStatus action) {
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
