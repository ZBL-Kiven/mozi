package com.cityfruit.mozi.lucky52.entity;

import lombok.Data;

@Data
public class UserInfo {

    private String bearyChatId;

    private String name;

    private String zentaoId;

    /**
     * 质量碎片
     */
    private int qualityFragment = 0;

    public UserInfo() {
    }

    /**
     * @param bearyChatId
     * @param name
     * @param zentaoId
     */
    public UserInfo(String bearyChatId, String name, String zentaoId) {
        this.bearyChatId = bearyChatId;
        this.name = name;
        this.zentaoId = zentaoId;
    }

}


