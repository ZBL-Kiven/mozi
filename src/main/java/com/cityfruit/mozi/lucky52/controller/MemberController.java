package com.cityfruit.mozi.lucky52.controller;

import com.cityfruit.mozi.lucky52.constant.RequestPath;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping(value = RequestPath.GET_MEMBERS, method = RequestMethod.GET)
    public List<Member> getMembers() {
        log.info("[RequestPath: {}]", RequestPath.GET_MEMBERS);
        return memberService.getMembers();
    }

}
