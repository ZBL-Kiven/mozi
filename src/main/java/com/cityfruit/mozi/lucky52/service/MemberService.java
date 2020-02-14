package com.cityfruit.mozi.lucky52.service;

import com.cityfruit.mozi.lucky52.entity.Member;

import java.util.List;

/**
 * @author tianyuheng
 */
public interface MemberService {

    /**
     * 获取成员信息
     *
     * @return 成员信息列表
     */
    List<Member> getMembers();

}
