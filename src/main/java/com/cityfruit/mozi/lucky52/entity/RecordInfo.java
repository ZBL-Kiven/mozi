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

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQualityPoint() {
        return qualityPoint;
    }

    public void setQualityPoint(float qualityPoint) {
        this.qualityPoint = qualityPoint;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public int getQualityFragment() {
        return qualityFragment;
    }

    public void setQualityFragment(int qualityFragment) {
        this.qualityFragment = qualityFragment;
    }

    public boolean isExchanged() {
        return exchanged;
    }

    public void setExchanged(boolean exchanged) {
        this.exchanged = exchanged;
    }
}
