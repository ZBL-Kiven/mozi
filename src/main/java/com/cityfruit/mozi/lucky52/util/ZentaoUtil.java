package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.DateUtil;
import com.cityfruit.mozi.comman.util.HttpUtil;
import com.cityfruit.mozi.lucky52.constant.UrlConst;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tianyuheng
 * @date 2020/02/11
 */
@Slf4j
public class ZentaoUtil {

    /**
     * 今日创建 BUG，并且未确认
     */
    public static final Integer SEARCH_TODAY_OPENED_WITHOUT_CONFIRMED = 0;

    /**
     * 今日创建 BUG，并且为已确认 BUG
     */
    public static final Integer SEARCH_TODAY_OPENED_WITH_CONFIRMED = 1;

    /**
     * 今日关闭 BUG
     */
    public static final Integer SEARCH_TODAY_CLOSED = 2;

    /**
     * 僵尸 BUG
     */
    public static final Integer SEARCH_ZOMBIE_BUG = 3;

    /**
     * 禅道未登录前缀
     */
    public static final String ZENTAO_NOT_LOGGED_IN_PREFIX = "<html>";

    /**
     * 禅道登录参数
     */
    private static final Map<String, Object> LOGIN_PARAM;

    static {
        LOGIN_PARAM = new HashMap<>();
        LOGIN_PARAM.put("account", "cfruit.tyh");
        LOGIN_PARAM.put("password", "48551Tyh");
    }

    /**
     * 禅道创建搜索条件参数
     */
    private static Map<Integer, Map<String, Object>> buildSearchQueryParam;

    static {
        buildSearchQueryParam = new HashMap<>();
        buildSearchQueryParam.put(SEARCH_TODAY_OPENED_WITHOUT_CONFIRMED, new HashMap<String, Object>() {{
            put("fieldtitle", "");
            put("fieldkeywords", "");
            put("fieldsteps", "");
            put("fieldassignedTo", "");
            put("fieldresolvedBy", "");
            put("fieldstatus", "");
            put("fieldconfirmed", "ZERO");
            put("fieldproduct", "");
            put("fieldplan", "");
            put("fieldmodule", "ZERO");
            put("fieldproject", "");
            put("fieldseverity", "0");
            put("fieldpri", "0");
            put("fieldtype", "");
            put("fieldos", "");
            put("fieldbrowser", "");
            put("fieldresolution", "");
            put("fieldtoTask", "");
            put("fieldtoStory", "");
            put("fieldopenedBy", "");
            put("fieldclosedBy", "");
            put("fieldlastEditedBy", "");
            put("fieldmailto", "");
            put("fieldopenedBuild", "");
            put("fieldresolvedBuild", "");
            put("fieldopenedDate", "");
            put("fieldassignedDate", "");
            put("fieldresolvedDate", "");
            put("fieldclosedDate", "");
            put("fieldlastEditedDate", "");
            put("fielddeadline", "");
            put("fieldid", "");
            put("andOr1", "AND");
            put("field1", "openedDate");
            put("operator1", "between");
            put("value1", "$today");
            put("andOr2", "or");
            put("field2", "title");
            put("operator2", "=");
            put("value2", "");
            put("andOr3", "or");
            put("field3", "title");
            put("operator3", "=");
            put("value3", "");
            put("andOr4", "AND");
            put("field4", "confirmed");
            put("operator4", "=");
            put("value4", "ZERO");
            put("andOr5", "or");
            put("field5", "title");
            put("operator5", "=");
            put("value5", "");
            put("andOr6", "or");
            put("field6", "title");
            put("operator6", "=");
            put("value6", "");
            put("module", "bug");
            put("actionURL", "");
            put("groupItems", "3");
            put("formType", "lite");
        }});
        buildSearchQueryParam.put(SEARCH_TODAY_OPENED_WITH_CONFIRMED, new HashMap<String, Object>() {{
            put("fieldtitle", "");
            put("fieldkeywords", "");
            put("fieldsteps", "");
            put("fieldassignedTo", "");
            put("fieldresolvedBy", "");
            put("fieldstatus", "");
            put("fieldconfirmed", "ZERO");
            put("fieldproduct", "");
            put("fieldplan", "");
            put("fieldmodule", "ZERO");
            put("fieldproject", "");
            put("fieldseverity", "0");
            put("fieldpri", "0");
            put("fieldtype", "");
            put("fieldos", "");
            put("fieldbrowser", "");
            put("fieldresolution", "");
            put("fieldtoTask", "");
            put("fieldtoStory", "");
            put("fieldopenedBy", "");
            put("fieldclosedBy", "");
            put("fieldlastEditedBy", "");
            put("fieldmailto", "");
            put("fieldopenedBuild", "");
            put("fieldresolvedBuild", "");
            put("fieldopenedDate", "");
            put("fieldassignedDate", "");
            put("fieldresolvedDate", "");
            put("fieldclosedDate", "");
            put("fieldlastEditedDate", "");
            put("fielddeadline", "");
            put("fieldid", "");
            put("andOr1", "AND");
            put("field1", "openedDate");
            put("operator1", "between");
            put("value1", "$today");
            put("andOr2", "or");
            put("field2", "title");
            put("operator2", "=");
            put("value2", "");
            put("andOr3", "or");
            put("field3", "title");
            put("operator3", "=");
            put("value3", "");
            put("andOr4", "AND");
            put("field4", "confirmed");
            put("operator4", "=");
            put("value4", "1");
            put("andOr5", "or");
            put("field5", "title");
            put("operator5", "=");
            put("value5", "");
            put("andOr6", "or");
            put("field6", "title");
            put("operator6", "=");
            put("value6", "");
            put("module", "bug");
            put("actionURL", "");
            put("groupItems", "3");
            put("formType", "lite");
        }});
        buildSearchQueryParam.put(SEARCH_TODAY_CLOSED, new HashMap<String, Object>() {{
            put("fieldtitle", "");
            put("fieldkeywords", "");
            put("fieldsteps", "");
            put("fieldassignedTo", "");
            put("fieldresolvedBy", "");
            put("fieldstatus", "");
            put("fieldconfirmed", "ZERO");
            put("fieldproduct", "");
            put("fieldplan", "");
            put("fieldmodule", "ZERO");
            put("fieldproject", "");
            put("fieldseverity", "0");
            put("fieldpri", "0");
            put("fieldtype", "");
            put("fieldos", "");
            put("fieldbrowser", "");
            put("fieldresolution", "");
            put("fieldtoTask", "");
            put("fieldtoStory", "");
            put("fieldopenedBy", "");
            put("fieldclosedBy", "");
            put("fieldlastEditedBy", "");
            put("fieldmailto", "");
            put("fieldopenedBuild", "");
            put("fieldresolvedBuild", "");
            put("fieldopenedDate", "");
            put("fieldassignedDate", "");
            put("fieldresolvedDate", "");
            put("fieldclosedDate", "");
            put("fieldlastEditedDate", "");
            put("fielddeadline", "");
            put("fieldid", "");
            put("andOr1", "AND");
            put("field1", "closedDate");
            put("operator1", "between");
            put("value1", "$today");
            put("andOr2", "or");
            put("field2", "title");
            put("operator2", "=");
            put("value2", "");
            put("andOr3", "or");
            put("field3", "title");
            put("operator3", "=");
            put("value3", "");
            put("andOr4", "AND");
            put("field4", "confirmed");
            put("operator4", "=");
            put("value4", "1");
            put("andOr5", "or");
            put("field5", "title");
            put("operator5", "=");
            put("value5", "");
            put("andOr6", "or");
            put("field6", "title");
            put("operator6", "=");
            put("value6", "");
            put("module", "bug");
            put("actionURL", "");
            put("groupItems", "3");
            put("formType", "lite");
        }});
        buildSearchQueryParam.put(SEARCH_ZOMBIE_BUG, new HashMap<String, Object>() {{
            put("fieldtitle", "");
            put("fieldkeywords", "");
            put("fieldsteps", "");
            put("fieldassignedTo", "");
            put("fieldresolvedBy", "");
            put("fieldstatus", "");
            put("fieldconfirmed", "ZERO");
            put("fieldproduct", "");
            put("fieldplan", "");
            put("fieldmodule", "ZERO");
            put("fieldproject", "");
            put("fieldseverity", "0");
            put("fieldpri", "0");
            put("fieldtype", "");
            put("fieldos", "");
            put("fieldbrowser", "");
            put("fieldresolution", "");
            put("fieldtoTask", "");
            put("fieldtoStory", "");
            put("fieldopenedBy", "");
            put("fieldclosedBy", "");
            put("fieldlastEditedBy", "");
            put("fieldmailto", "");
            put("fieldopenedBuild", "");
            put("fieldresolvedBuild", "");
            put("fieldopenedDate", "");
            put("fieldassignedDate", "");
            put("fieldresolvedDate", "");
            put("fieldclosedDate", "");
            put("fieldlastEditedDate", "");
            put("fielddeadline", "");
            put("fieldid", "");
            put("andOr1", "AND");
            put("field1", "status");
            put("operator1", "notinclude");
            put("value1", "closed");
            put("andOr2", "or");
            put("field2", "title");
            put("operator2", "=");
            put("value2", "");
            put("andOr3", "or");
            put("field3", "title");
            put("operator3", "=");
            put("value3", "");
            put("andOr4", "AND");
            put("field4", "openedDate");
            put("operator4", "<");
            put("value4", DateUtil.getZombieDate());
            put("andOr5", "or");
            put("field5", "title");
            put("operator5", "=");
            put("value5", "");
            put("andOr6", "or");
            put("field6", "title");
            put("operator6", "=");
            put("value6", "");
            put("module", "bug");
            put("actionURL", "");
            put("groupItems", "3");
            put("formType", "lite");
        }});
    }

    public static String zentaoSid = "9kfif5uhfkd6d0esmt2c2f7e42";

    public static String zentaoCookie = "zentaosid=" + zentaoSid + "; path=/";

    /**
     * 禅道登录
     */
    public static void login() {
        String getSessionIdResponse = HttpUtil.get(UrlConst.ZENTAO_GET_SESSION_ID, "");
        ZentaoUtil.zentaoSid = JSONObject.parseObject(getSessionIdResponse).getJSONObject("data").getString("sessionID");
        log.info("[Cookie 失效，重新登录。Session ID：{}]", zentaoSid);
        String getLoginResponse = HttpUtil.post(UrlConst.ZENTAO_LOGIN + "?zentaosid=" + zentaoSid, LOGIN_PARAM, zentaoCookie, HttpUtil.CONTENT_TYPE_JSON);
        assert !getLoginResponse.isEmpty();
    }

    /**
     * 获取产品列表
     *
     * @return 产品列表 Map<\编号, 名称>
     */
    public static Map<String, Object> getProducts() {
        String getProductsResponse = HttpUtil.get(UrlConst.ZENTAO_PRODUCTS, zentaoCookie);
        if (getProductsResponse.startsWith(ZENTAO_NOT_LOGGED_IN_PREFIX)) {
            login();
            getProductsResponse = HttpUtil.get(UrlConst.ZENTAO_PRODUCTS, zentaoCookie);
        }
        JSONObject jsonProducts = JSONObject.parseObject(getProductsResponse).getJSONObject("data").getJSONObject("products");
        return jsonProducts.getInnerMap();
    }

    /**
     * 创建搜索条件
     *
     * @param searchType   搜索类型
     * @param zentaoCookie 禅道 SessionId
     * @param productId    产品 ID
     */
    public static String getSearchResult(Integer searchType, String zentaoCookie, String productId) {
        String result;

        Map<String, Object> body = buildSearchQueryParam.get(searchType);
        body.replace("fieldproduct", productId);
        body.replace("actionURL", "/bug-browse-" + productId + "-0-bySearch-myQueryID.html");
        if (SEARCH_ZOMBIE_BUG.equals(searchType)) {
            body.replace("value4", DateUtil.getZombieDate());
        }
        
        // 请求搜索条件
        HttpUtil.post(UrlConst.ZENTAO_BUILD_SEARCH_QUERY, body, zentaoCookie, HttpUtil.CONTENT_TYPE_FORM_URLENCODED);
        result = HttpUtil.get("http://cf-issue.i-mocca.com/bug-browse-" + productId + "-0-bySearch-myQueryID.json", zentaoCookie);
        return result;
    }

}
