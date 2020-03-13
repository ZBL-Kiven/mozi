package com.cityfruit.mozi.lucky52.parameter;

import lombok.Data;

@Data
public class TrelloActionRequestParam {

    //trello 卡片的 ID
    private String trelloId;

    //從哪個卡片來的
    private String fromGroup;

    //到哪個卡片去
    private String toGroup;

    //執行的是哪一個用戶
    private String actionUserId;

}
