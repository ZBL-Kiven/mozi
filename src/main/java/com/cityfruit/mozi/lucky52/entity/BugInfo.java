package com.cityfruit.mozi.lucky52.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class BugInfo {

    /**
     * 产品编号
     */
    private int productID;

    /**
     * BUG 标题
     */
    private String title;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * BUG 详情
     */
    private Bug bug;

    /**
     * BUG 操作步骤
     */
    private List<BugAction> action;

    public void setActions(Map<String, BugAction> actionMap) {
        action = new ArrayList<BugAction>(actionMap.values());
    }
}
