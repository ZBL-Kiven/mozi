package com.cityfruit.mozi.lucky52.controller;

import com.cityfruit.mozi.lucky52.constant.RequestPathConst;
import com.cityfruit.mozi.lucky52.parameter.BearyChatRequestParam;
import com.cityfruit.mozi.lucky52.parameter.BearyChatResponseParam;
import com.cityfruit.mozi.lucky52.service.TreasureBoxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author tianyuheng
 * @date 2020/02/13
 */
@Slf4j
@RestController
public class TreasureBoxController {

    @Resource
    TreasureBoxService treasureBoxService;

    @ResponseBody
    @RequestMapping(value = RequestPathConst.OPEN_TREASURE_BOX, method = RequestMethod.POST)
    public BearyChatResponseParam openTreasureBox(@RequestBody BearyChatRequestParam bearyChatRequestParam) {
        BearyChatResponseParam bearyChatResponseParam = new BearyChatResponseParam();
        log.info("[RequestPathConst: {}] Param: {}", RequestPathConst.OPEN_TREASURE_BOX, bearyChatRequestParam);
        bearyChatResponseParam.setText(treasureBoxService.openTreasureBox(bearyChatRequestParam));
        return bearyChatResponseParam;
    }

}
