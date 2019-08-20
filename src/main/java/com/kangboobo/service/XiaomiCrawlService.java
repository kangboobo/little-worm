package com.kangboobo.service;

import com.alibaba.fastjson.JSONObject;
import com.kangboobo.utils.HttpCilentUtil;
import com.kangboobo.utils.IdWorker;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/8/20.
 */
@Service
public class XiaomiCrawlService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String URL = "https://game.xiaomi.com/api/getViewpointList";

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private HttpCilentUtil httpCilentUtil;

    public Object gameCommentCrawl(String gameCode) {
        // 组织请求参数
        StringBuffer params = new StringBuffer();
        try {
            params.append("uuid=0");
            params.append("&gameId=" + URLEncoder.encode(gameCode, "utf-8"));
            params.append("&sortType=4");
            params.append("&page=1");
            params.append("&pageSize=10");
            params.append("&dataType=1");
            params.append("&relObjId=" + URLEncoder.encode(gameCode, "utf-8"));
            params.append("&relObjType=1");
            params.append("&owner=1");
            String url = URL + "?" + params;
            String responseContent = httpCilentUtil.doGet(url, gameCode);

            JSONObject json = JSONObject.parseObject(responseContent);
            logger.info("序列号"+idWorker.nextId());

        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }

    public static void main(String[] args){
        String gameCode = "232132";
        // 组织请求参数
        StringBuffer params = new StringBuffer();
        HttpCilentUtil httpCilentUtil = new HttpCilentUtil();
        try {
            params.append("uuid=0");
            params.append("&gameId=" + URLEncoder.encode(gameCode, "utf-8"));
            params.append("&sortType=4");
            params.append("&page=1");
            params.append("&pageSize=10");
            params.append("&dataType=1");
            params.append("&relObjId=" + URLEncoder.encode(gameCode, "utf-8"));
            params.append("&relObjType=1");
            params.append("&owner=1");
            String url = URL + "?" + params;
            String responseContent = httpCilentUtil.doGet(url, gameCode);

            JSONObject json = JSONObject.parseObject(responseContent);
            System.out.print(json);

            IdWorker idWorker = new IdWorker(1, 1);
            System.out.print("序列号"+idWorker.nextId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
