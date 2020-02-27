package com.cityfruit.mozi.lucky52.entity;

import lombok.Data;

@Data
public class TaskStatus {

    private boolean taskSuccess1 = false;
    private String taskName1 = "当天创建 Bug 总数为 1";
    private boolean taskPush1 = false;

    private boolean taskSuccess2 = false;
    private String taskName2 = "当天创建 Bug 总数为 5";
    private boolean taskPush2 = false;

    private boolean taskSuccess3 = false;
    private String taskName3 = "当天创建 Bug 总数为 15";
    private boolean taskPush3 = false;

    private boolean taskSuccess4 = false;
    private String taskName4 = "当天创建 Bug 总数为 25";
    private boolean taskPush4 = false;

    private boolean taskSuccess5 = false;
    private String taskName5 = "当天创建 Bug 总数为 30";
    private boolean taskPush5 = false;

    private boolean taskSuccess6 = false;
    private String taskName6 = "当天创建 S1 Bug 总数为 1";
    private boolean taskPush6 = false;

    private boolean taskSuccess7 = false;
    private String taskName7 = "当天创建 S2 Bug 总数为 3";
    private boolean taskPush7 = false;

    private boolean taskSuccess8 = false;
    private String taskName8 = "当天关闭 Bug 总数为 20";
    private boolean taskPush8 = false;

    private boolean taskSuccess9 = false;
    private String taskName9 = "当天关闭 S3+S4 Bug 总数为 30";
    private boolean taskPush9 = false;

    private boolean taskSuccess10 = false;
    private String taskName10 = "前一天手上僵尸 Bug = 0";
    private boolean taskPush10 = false;

    private boolean taskSuccess11 = false;
    private String taskName11 = "前一天 QP 得分第一名";
    private boolean taskPush11 = false;
}
