package com.cityfruit.mozi.lucky52.controller;

import com.cityfruit.mozi.lucky52.constant.RequestPathConst;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.parameter.BearyChatRequestParam;
import com.cityfruit.mozi.lucky52.parameter.BearyChatResponseParam;
import com.cityfruit.mozi.lucky52.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
@Slf4j
@RestController
public class MemberController {

    @Resource
    MemberService memberService;

    @ResponseBody
    @RequestMapping(value = RequestPathConst.GET_MEMBERS, method = RequestMethod.GET)
    public List<Member> getMembers() {
        log.info("[RequestPathConst: {}]", RequestPathConst.GET_MEMBERS);
        return memberService.getMembers();
    }

    @ResponseBody
    @RequestMapping(value = RequestPathConst.GET_QUALITY_POINT, method = RequestMethod.POST)
    public BearyChatResponseParam getQualityPoint(@RequestBody BearyChatRequestParam bearyChatRequestParam) {
        BearyChatResponseParam bearyChatResponseParam = new BearyChatResponseParam();
        log.info("[RequestPathConst: {}] Param: {}", RequestPathConst.GET_QUALITY_POINT, bearyChatRequestParam);
        bearyChatResponseParam.setText(memberService.getQualityPoint(bearyChatRequestParam));
        return bearyChatResponseParam;
    }

    @ResponseBody
    @RequestMapping(value = RequestPathConst.GET_QUALITY_FRAGMENT, method = RequestMethod.POST)
    public BearyChatResponseParam getQualityFragment(@RequestBody BearyChatRequestParam bearyChatRequestParam) {
        BearyChatResponseParam bearyChatResponseParam = new BearyChatResponseParam();
        log.info("[RequestPathConst: {}] Param: {}", RequestPathConst.GET_QUALITY_FRAGMENT, bearyChatRequestParam);
        bearyChatResponseParam.setText(memberService.getQualityFragment(bearyChatRequestParam));
        return bearyChatResponseParam;
    }

}
