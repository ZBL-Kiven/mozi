package com.cityfruit.mozi.lucky52.controller;

import com.cityfruit.mozi.lucky52.constant.RequestPathConst;
import com.cityfruit.mozi.lucky52.parameter.TrelloActionRequestParam;
import com.cityfruit.mozi.lucky52.parameter.ZentaoNoticeRequestParam;
import com.cityfruit.mozi.lucky52.service.ScoreService;
import com.cityfruit.mozi.lucky52.service.TrelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
public class TrelloController {

    @Resource
    TrelloService trelloService;

    @ResponseBody
    @RequestMapping(value = RequestPathConst.TRELLO_ACTION_WEB_HOOK, method = RequestMethod.POST)
    public void move(@RequestBody TrelloActionRequestParam trelloActionRequestParam) {
        log.info("[RequestPathConst: {}] text: {}", RequestPathConst.TRELLO_ACTION_WEB_HOOK, trelloActionRequestParam.getTrelloId());
        trelloService.move(trelloActionRequestParam);
    }

}