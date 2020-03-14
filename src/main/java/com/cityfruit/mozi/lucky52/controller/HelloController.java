package com.cityfruit.mozi.lucky52.controller;

import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.lucky52.constant.RequestPathConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author tianyuheng
 * @date 2020/02/06
 */
@Slf4j
@RestController
public class HelloController {

    @ResponseBody
    @RequestMapping(value = RequestPathConst.HELLO, method = RequestMethod.GET)
    public String hello() {
        log.info("[{} RequestPathConst: {}]",
                DateUtil.getCurrentDateTimes(),
                RequestPathConst.HELLO);
        return "Fine, thank you. And you?";
    }

}
