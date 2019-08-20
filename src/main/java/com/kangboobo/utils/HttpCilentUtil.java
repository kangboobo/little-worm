package com.kangboobo.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by kangboobo on 2019/8/20.
 */
@Service
public class HttpCilentUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String doGet(String url, String gameCode) {
        String responseContent = null;
        // 创建http客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            logger.info("http请求响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                responseContent = EntityUtils.toString(responseEntity);
                logger.info("http请求响应内容长度为:" + responseEntity.getContentLength());
                logger.info("http请求响应内容为:" + responseContent);
            }
        } catch (Exception e) {
            logger.error("http请求失败 !!! gameCode={}, url={}", gameCode, url, e);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("HttpCilentUtil 释放资源失败 !!! ", e);
            }
        }

        return responseContent;
    }
}
