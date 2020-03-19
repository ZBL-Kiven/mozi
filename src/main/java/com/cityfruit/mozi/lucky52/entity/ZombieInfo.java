package com.cityfruit.mozi.lucky52.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ZombieInfo {

    private String bcName;

    private String name;

    private int zombieCount = 0;

    private List<String> zombieList = new ArrayList<>();

    public ZombieInfo(String bcName, String name) {
        this.bcName = bcName;
        this.name = name;
    }

    public ZombieInfo() {
    }

    public String getBcName() {
        return bcName;
    }

    public void setBcName(String bcName) {
        this.bcName = bcName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getZombieCount() {
        return zombieCount;
    }

    public void setZombieCount(int zombieCount) {
        this.zombieCount = zombieCount;
    }

    public List<String> getZombieList() {
        return zombieList;
    }

    public void setZombieList(List<String> zombieList) {
        this.zombieList = zombieList;
    }
}
