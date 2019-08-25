package com.kangboobo.service;

import com.alibaba.fastjson.JSONObject;
import com.kangboobo.utils.ExcelUtils;
import com.kangboobo.utils.HttpCilentUtil;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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

//    @Autowired
//    private IdWorker idWorker;

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
//            logger.info("序列号"+idWorker.nextId());

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
            String title = "员工任务档案明细数据";
            // 标题
            List<String> titles = Lists.newArrayList();
            titles.add("序号");
            titles.add("姓名");
            titles.add("所属岗位");
            // 数据
            List<List<String>> values = new ArrayList<>();
            List<String> sheetValueList = new ArrayList<>();
            sheetValueList.add(String.valueOf(1));// 序号
            sheetValueList.add("张三");//姓名
            sheetValueList.add("岗位");//所属岗位
            values.add(sheetValueList);
            File file = ExcelUtils.exportDyExcelFile(titles,values, "导出文件","sheet1", "C:\\Users\\Administrator\\Downloads");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
