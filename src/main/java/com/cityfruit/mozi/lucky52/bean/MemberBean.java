package com.cityfruit.mozi.lucky52.bean;

import lombok.Data;

@Data
public class MemberBean {

    private String bearyChatId;

    private String name;

    private String zentaoId;

    public MemberBean() {
    }

    /**
     *
     * @param bearyChatId
     * @param name
     * @param zentaoId
     */
    public MemberBean(String bearyChatId, String name, String zentaoId) {
        this.bearyChatId = bearyChatId;
        this.name = name;
        this.zentaoId = zentaoId;
    }
}


