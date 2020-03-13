package com.cityfruit.mozi.lucky52.entity;

import lombok.Data;

/**
 * Trello 移動成功的紀錄
 */
@Data
public class TrelloMoveInfo {

    //trello ID
    private String trelloId;

    //從哪里來
    private String fromGroup;

    //到哪裡去
    private String toGroup;

    //移動用戶
    private String trelloUserId;

    //移動時間
    private long moveTs;
}
