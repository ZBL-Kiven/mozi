package com.cityfruit.mozi.lucky52.parameter;

import com.cityfruit.mozi.comman.util.StringUtil;
import lombok.Data;

/**
 * @author tianyuheng
 * @date 2020/02/13
 */
@Data
public class BearyChatRequestParam {

    /**
     * 机器人 token
     */
    private String token;

    /**
     * 消息发送时间
     */
    private long ts;

    /**
     * 消息内容
     */
    private String text;

    /**
     * 触发词
     */
    private String trigger_word;

    /**
     * your_domain
     */
    private String subdomain;

    /**
     * your channel
     */
    private String channel_name;

    /**
     * your name
     */
    private String user_name;

    /**
     * 校验群组名称
     *
     * @return true / false
     */
    public boolean checkChannel(String channelName) {
        return !StringUtil.isEmpty(channel_name) && channel_name.equals(channelName);
    }

}
