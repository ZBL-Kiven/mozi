package com.cityfruit.mozi.comman.util;

import com.alibaba.fastjson.JSONObject;
import com.cityfruit.mozi.lucky52.util.ZentaoUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.Map;

/**
 * @author tianyuheng
 * @date 2020/02/06
 */
public class HttpUtil {

    public static final String CONTENT_TYPE_JSON = "application/json";

    public static final String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";

    /**
     * 发送 GET 请求
     *
     * @param url 请求 URL
     * @return 相应参数字符串
     */
    public static String get(String url, String cookie) {
        String result = null;
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader(HTTP.CONTENT_TYPE, "application/json");
                httpGet.setHeader("Cookie", cookie);
                client = HttpClients.createDefault();
                response = client.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity, "UTF-8");
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送 POST 请求
     *
     * @param url         请求 URL
     * @param body        请求体
     * @param cookie      请求携带的 Cookie
     * @param contentType 请求编码
     * @return 响应参数字符串
     */
    public static String post(String url, Map<String, Object> body, String cookie, String contentType) {
        String result = null;
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
                httpPost.setHeader("Cookie", cookie);
                if (contentType.equals(CONTENT_TYPE_FORM_URLENCODED)) {
                    httpPost.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_FORM_URLENCODED);
                    StringBuilder formBodyBuffer = new StringBuilder();
                    if (body != null && !body.isEmpty()) {
                        for (Map.Entry<String, Object> e : body.entrySet()) {
                            formBodyBuffer.append(e.getKey());
                            formBodyBuffer.append("=");
                            formBodyBuffer.append(e.getValue());
                            formBodyBuffer.append("&");
                        }
                        httpPost.setEntity(new StringEntity(formBodyBuffer.toString()));
                    }
                } else {
                    httpPost.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON);
                    httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(body),
                            ContentType.create("text/json", "UTF-8")));

                }
                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity, "UTF-8");
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        String searchResult = get("http://cf-issue.i-mocca.com/bug-browse-5-0-unclosed.json", ZentaoUtil.zentaoCookie);
        System.out.println(JSONObject.parseObject(searchResult).getJSONObject("data").toString());
    }
}
