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
}
