package com.cityfruit.mozi.comman.util;

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

    public static String cookies = "zentaosid=9kfif5uhfkd6d0esmt2c2f7e42";

    /**
     * 发送 GET 请求
     *
     * @param url 请求 URL
     * @return 相应参数字符串
     */
    public static String get(String url) {
        String result = null;
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader(HTTP.CONTENT_TYPE, "application/json");
                httpGet.setHeader("Cookie", cookies);
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
     * @param url  请求 URL
     * @param body 请求体
     * @return 响应参数字符串
     */
    public static String post(String url, Map<String, Object> body) {
        String result = null;
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
                httpPost.setHeader("Cookie", cookies);
                httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(body),
                        ContentType.create("text/json", "UTF-8")));
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

}
