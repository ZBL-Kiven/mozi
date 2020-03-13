package com.cityfruit.mozi.lucky52.util;


/**
 * @author lp
 * @date: 2019/12/18 12:53 下午
 */
public class TrelloConstant {

    public static String KEY = "f7f019d00e78a670f175ab41faa160f6";
    public static String TOKEN = "7207893b06b3ed1d04466557dd384322cd3b2e975c466799d63abfd17a2ddcdb";

    private static String INDEX_URL = "https://trello.com/";
    private static String END_URL = "key=" + TrelloConstant.KEY + "&token=" + TrelloConstant.TOKEN;

    public static String ORGANIZATION_NAME = "sunpeople3";

    /**
     * 看板信息
     * param：sunpeople3
     * param: fields:open、all
     * return JSONArray
     */
    public static String BOARD_URL = INDEX_URL + "1/organizations/{og_name}/boards?filter=open&" + END_URL;
//     https://api.trello.com/1/organizations/sunpeople3/boards?filter=open&key=f7f019d00e78a670f175ab41faa160f6&token=7207893b06b3ed1d04466557dd384322cd3b2e975c466799d63abfd17a2ddcdb

    /**
     * 每个组的功能分类列表信息
     * param：groupId：EJmMEawk
     * param: fields:name、open
     * return JSONObject
     */
    public static String MENU_URL = INDEX_URL + "1/boards/{menu_link}/?fields=name&lists=open&list_fields=all&" + END_URL;
//    https://api.trello.com/1/boards/EJmMEawk/?fields=name&lists=open&list_fields=all&key=f7f019d00e78a670f175ab41faa160f6&token=7207893b06b3ed1d04466557dd384322cd3b2e975c466799d63abfd17a2ddcdb

    /**
     * 每个功能目录下的card列表信息
     * param：menuListID：5df7362f07a0747b6c23612e
     * fields=id,name,badges,labels,idChecklists
     * return JSONArray
     */
    public static String CARD_URL = INDEX_URL + "1/lists/{cardId}/cards?fields=all&" + END_URL;
//    https://api.trello.com/1/lists/5df7362f07a0747b6c23612e/cards?fields=all&key=f7f019d00e78a670f175ab41faa160f6&token=7207893b06b3ed1d04466557dd384322cd3b2e975c466799d63abfd17a2ddcdb

    /**
     * 每个card detail
     * param：cardID：5df1b38efaa727291e5f4691
     * return JSONArray
     */
    //    https://trello.com/1/cards/5dfb0ab905ce254aa5c58068?filter=all&key=f7f019d00e78a670f175ab41faa160f6&token=7207893b06b3ed1d04466557dd384322cd3b2e975c466799d63abfd17a2ddcdb

    /**
     * 每个card ac列表信息
     * param：cardID：5df1b38efaa727291e5f4691
     * return JSONArray
     */
    public static String CARD_AC_URL = INDEX_URL + "1/cards/{cardId}/checklists?checkItems=all" +
            "&checkItem_fields=name%2CnameData%2Cpos%2Cstate&filter=all&fields=all&" + END_URL;

    /**
     * 每个card 某个ac列表信息
     * param：ac ID：5df3137574612301b1bf265e
     * return JSONObject
     */
    public static String SINGLE_AC_URL = INDEX_URL + "1/checklists/5df1b38efaa727291e5f4691?" + END_URL;


    /**
     * 每个card lable
     * param：ac ID：5df3137574612301b1bf265e
     * return JSONArray
     */
    public static String LABEL_URL = INDEX_URL + "1/cards/{cardId}/labels?filter=all" + END_URL;
//    https://trello.com/1/cards/5dfb0ab905ce254aa5c58068/labels?filter=all&key=f7f019d00e78a670f175ab41faa160f6&token=7207893b06b3ed1d04466557dd384322cd3b2e975c466799d63abfd17a2ddcdb

    //    https://api.trello.com/1/lists/5e1be2585060da5717b2663b/cards?fields=all&key=f7f019d00e78a670f175ab41faa160f6&token=7207893b06b3ed1d04466557dd384322cd3b2e975c466799d63abfd17a2ddcdb
    /**
     * Trello 註冊 WebHook
     * POST
     */
    public static final String TRELLO_POST_WEBHOOK = "https://api.trello.com/1/webhooks/";

    /**
     * Trello 通過 註冊的 ID 獲取 Hook 信息
     */
    public static final String TRELLO_GET_WEBHOOK = "https://api.trello.com/1/webhooks/%s";

    /**
     * 通過 ID 獲取 HOOK 里某個字段信息
     */
    public static final String TRELLO_GET_WEBHOOK_FILED = "https://api.trello.com/1/webhooks/%s/%s";

    /**
     * 通過 ID 更新 WEBHOOK 信息
     */
    public static final String TRELLO_PUT_WEBHOOK = "https://api.trello.com/1/webhooks";

    /**
     * 通過 ID 刪除 WEBHOOK
     */
    public static final String TRELLO_DEL_WEBHOOK = "https://api.trello.com/1/webhooks/id";

    /**
     * 獲取團隊中的成員
     */
    public static final String TRELLO_GET_TEAM_MEMBER = "https://api.trello.com/1/organizations/%s/members";
}
