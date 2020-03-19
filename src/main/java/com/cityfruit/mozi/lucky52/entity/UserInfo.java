package com.cityfruit.mozi.lucky52.entity;

import com.cityfruit.mozi.lucky52.enums.Role;
import lombok.Data;

@Data
public class UserInfo {
    public String getBearyChatId() {
        return bearyChatId;
    }

    public void setBearyChatId(String bearyChatId) {
        this.bearyChatId = bearyChatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZentaoId() {
        return zentaoId;
    }

    public void setZentaoId(String zentaoId) {
        this.zentaoId = zentaoId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getQualityFragment() {
        return qualityFragment;
    }

    public void setQualityFragment(int qualityFragment) {
        this.qualityFragment = qualityFragment;
    }

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
     * @param bearyChatId
     * @param name
     * @param zentaoId
     */
    public UserInfo(String bearyChatId, String name, String zentaoId, String role) {
        this.bearyChatId = bearyChatId;
        this.name = name;
        this.zentaoId = zentaoId;
        this.role = role;
    }
}


