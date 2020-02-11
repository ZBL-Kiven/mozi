package com.cityfruit.mozi.lucky52.controller;

import com.cityfruit.mozi.lucky52.constant.RequestPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

/**
 * @author tianyuheng
 * @date 2020/02/06
 */
@Slf4j
@RestController
public class HelloController {

    @ResponseBody
    @RequestMapping(value = RequestPath.HELLO, method = RequestMethod.GET)
    public String hello() {
        log.info("[{} RequestPath: {}]",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()),
                RequestPath.HELLO);
        return "Hello!";
    }

}
