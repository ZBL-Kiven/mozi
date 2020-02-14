package com.cityfruit.mozi.lucky52.controller;

import com.cityfruit.mozi.lucky52.constant.RequestPathConst;
import com.cityfruit.mozi.lucky52.parameter.ZentaoNoticeRequestParam;
import com.cityfruit.mozi.lucky52.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author tianyuheng
 * @date 2020/02/06
 */
@Slf4j
@RestController
public class ScoreController {

    @Resource
    ScoreService scoreService;

    @ResponseBody
    @RequestMapping(value = RequestPathConst.ZENTAO_NOTICE_WEB_HOOK, method = RequestMethod.POST)
    public void score(@RequestBody ZentaoNoticeRequestParam zentaoNoticeRequestParam) {
        log.info("[RequestPathConst: {}] text: {}", RequestPathConst.ZENTAO_NOTICE_WEB_HOOK, zentaoNoticeRequestParam.getText());
        scoreService.score(zentaoNoticeRequestParam);
    }

}
