package com.cityfruit.mozi.lucky52.entity;

import lombok.Data;

@Data
public class RecordInfo {
    private long ts;
    private String name;
    private float qualityPoint;
    private boolean opened;
    private int qualityFragment;
    private boolean exchanged = false;
}
