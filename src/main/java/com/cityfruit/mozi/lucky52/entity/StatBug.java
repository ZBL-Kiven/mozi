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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenUser() {
        return openUser;
    }

    public void setOpenUser(String openUser) {
        this.openUser = openUser;
    }

    public String getCloseUser() {
        return closeUser;
    }

    public void setCloseUser(String closeUser) {
        this.closeUser = closeUser;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }
}
