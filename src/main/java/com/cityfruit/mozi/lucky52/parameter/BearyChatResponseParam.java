package com.cityfruit.mozi.lucky52.parameter;

import com.cityfruit.mozi.lucky52.constant.BearyChatConst;
import lombok.Data;

/**
 * @author tianyuheng
 * @date 2020/02/13
 */
@Data
public class BearyChatResponseParam {

    private String text;

    public BearyChatResponseParam() {
        text = BearyChatConst.SUCCESS_MESSAGE;
    }

}
