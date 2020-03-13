package com.cityfruit.mozi.lucky52.util;

import com.cityfruit.mozi.comman.util.HttpUtil;
import com.cityfruit.mozi.lucky52.constant.UrlConst;

import java.util.HashMap;
import java.util.Map;

public class TrelloUtil {

    /**
     * 註冊移動的事件
     *
     * @param callbackURL 觸發的 url
     * @param idModel     觸發的 ID
     */
    public static void registeMoveWebHook(String callbackURL, String idModel) {
        registeWebHook("此事件為拖動 Trello 卡片事件", callbackURL, idModel, true);
    }

    /**
     * 註冊 Trello WebHook
     *
     * @param description hook 描述
     * @param callbackURL hook 的url
     * @param idModel     hook 的唯一標識
     * @param active      是否啟用
     */
    private static void registeWebHook(String description, String callbackURL, String idModel, Boolean active) {
        Map<String, Object> params = new HashMap<>();
        params.put("description", description);
        params.put("callbackURL", callbackURL);
        params.put("idModel", idModel);
        params.put("active", active);
        params.put("key", TrelloConstant.KEY);
        params.put("token", TrelloConstant.TOKEN);
        String post = HttpUtil.post(TrelloConstant.TRELLO_POST_WEBHOOK, params, "", "");
        System.out.println("-=-=-=-=-=-=-=-=-" + post);
    }

    public static String getTeamList(){
//        HttpUtil.post(UrlConst.Tre)
        return "";
    }
}
