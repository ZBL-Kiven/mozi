package com.cityfruit.mozi.lucky52.util;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.comman.util.HttpUtil;
import com.cityfruit.mozi.lucky52.constant.Url;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tianyuheng
 * @date 2020/02/11
 */
public class ZentaoUtil {

    /**
     * 今日创建 BUG，并且为已确认 BUG
     */
    public static final Integer SEARCH_TODAY_OPENED_WITH_CONFIRMED = 1;

    /**
     * 今日关闭 BUG
     */
    public static final Integer SEARCH_TODAY_CLOSED = 2;

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

    private static Map<Integer, Map<String, Object>> buildSearchQueryParam;

    static {
        buildSearchQueryParam = new HashMap<>();
        buildSearchQueryParam.put(SEARCH_TODAY_OPENED_WITH_CONFIRMED, new HashMap<String, Object>() {{
            put("fieldconfirmed", "ZERO");
            put("fieldproduct", "");
            put("fieldmodule", "ZERO");
            put("fieldseverity", "0");
            put("fieldpri", "0");
            put("andOr1", "AND");
            put("field1", "openedDate");
            put("operator1", "between");
            put("value1", "$today");
            put("andOr4", "AND");
            put("field4", "confirmed");
            put("operator4", "=");
            put("value4", "1");
        }});
        buildSearchQueryParam.put(SEARCH_TODAY_CLOSED, new HashMap<String, Object>() {{
            put("fieldconfirmed", "ZERO");
            put("fieldproduct", "");
            put("fieldmodule", "ZERO");
            put("fieldseverity", "0");
            put("fieldpri", "0");
            put("andOr1", "AND");
            put("field1", "closedDate");
            put("operator1", "between");
            put("value1", "$today");
            put("andOr4", "AND");
            put("field4", "confirmed");
            put("operator4", "=");
            put("value4", "1");
        }});
    }

    public static String zentaoSid = "9kfif5uhfkd6d0esmt2c2f7e42";

    public static String zentaoCookie = "zentaosid=" + zentaoSid + "; path=/";

    /**
     * 禅道登录
     */
    public static void login() {
        String getSessionIdResponse = HttpUtil.get(Url.ZENTAO_GET_SESSION_ID, "");
        ZentaoUtil.zentaoSid = JSONObject.parseObject(getSessionIdResponse).getJSONObject("data").getString("sessionID");
        String getLoginResponse = HttpUtil.post(Url.ZENTAO_LOGIN + "?zentaosid=" + zentaoSid, LOGIN_PARAM, zentaoCookie);
        assert !getLoginResponse.isEmpty();
    }

    /**
     * 获取产品列表
     *
     * @return 产品列表 Map<\编号, 名称>
     */
    public static Map<String, Object> getProducts() {
        String getProductsResponse = HttpUtil.get(Url.ZENTAO_PRODUCTS, zentaoCookie);
        if (getProductsResponse.startsWith(ZENTAO_NOT_LOGGED_IN_PREFIX)) {
            login();
            getProductsResponse = HttpUtil.get(Url.ZENTAO_PRODUCTS, zentaoCookie);
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
        buildSearchQueryParam.get(searchType).replace("fieldproduct", productId);
        String buildSearchQueryResponse = HttpUtil.post(Url.ZENTAO_BUILD_SEARCH_QUERY, buildSearchQueryParam.get(searchType), zentaoCookie);
        if (buildSearchQueryResponse.startsWith(ZENTAO_NOT_LOGGED_IN_PREFIX)) {
            login();
            HttpUtil.post(Url.ZENTAO_BUILD_SEARCH_QUERY, buildSearchQueryParam.get(searchType), zentaoCookie);
        }
        result = HttpUtil.get("http://cf-issue.i-mocca.com/bug-browse-" + productId + "-0-bySearch-myQueryID.json", zentaoCookie);
        return result;
    }

}
