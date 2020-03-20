package com.cityfruit.mozi.lucky52.entity;

import com.cityfruit.mozi.lucky52.enums.Role;
import lombok.Data;

@Data
public class UserInfo {

    private String bearyChatId;

    private String name;

    private String zentaoId;

    private String role;

    /**
     * 质量碎片
     */
    private int qualityFragment = 0;

    /**
     * 将UserId 字符串转化为 Enum 类型
     */
    public Role getUserRole() {
        return Role.valueOf(role);
    }

    public UserInfo() {
    }

    /**
     * @param bearyChatId bc id
     * @param name user name
     * @param zentaoId zentao id
     */
    public UserInfo(String bearyChatId, String name, String zentaoId, String role) {
        this.bearyChatId = bearyChatId;
        this.name = name;
        this.zentaoId = zentaoId;
        this.role = role;
    }
}


