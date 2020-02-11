package com.cityfruit.mozi.lucky52.parameter;

import lombok.Data;

/**
 * @author tianyuheng
 * @date 2020/02/06
 */
@Data
public class ZentaoNoticeRequestParam {

    /**
     * 对象类型，可以为空
     */
    private String objectType;

    /**
     * 对象ID，可以为空
     */
    private String objectID;

    /**
     * 关联产品ID，可以为空
     */
    private String product;

    /**
     * 关联项目ID，可以为空
     */
    private String project;

    /**
     * 动作，可以为空
     */
    private String action;

    /**
     * 操作者，可以为空
     */
    private String actor;

    /**
     * 操作时间，可以为空
     */
    private String date;

    /**
     * 备注，可以为空
     */
    private String comment;

    /**
     * 操作内容，包含操作对象的url，必选。
     */
    private String text;

}
