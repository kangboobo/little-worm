package com.worm.little.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.worm.little.entity.CrawlCommentXiaomi;
import com.worm.little.mapper.CrawlCommentXiaomiMapper;
import com.worm.little.utils.HttpCilentUtil;
import com.worm.little.utils.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 小米爬虫服务
 * <p>
 * Created by Administrator on 2019/8/20.
 */
@Service
public class CrawlXiaomiService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String URL = "https://game.xiaomi.com/api/getViewpointList";
    private static final Integer PAGE_SIZE = 100;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private HttpCilentUtil httpCilentUtil;

    @Autowired
    private CrawlCommentXiaomiMapper crawlCommentXiaomiMapper;

    public Object gameCommentCrawl(Long userId, Integer gameCode) {
        // 组织请求参数
        StringBuffer params = new StringBuffer();
        try {
            // 获取存储的最新评论
            Map<String, Object> param = new HashMap<>();
            param.put("userId", userId);
            param.put("gameCode", gameCode);
            CrawlCommentXiaomi LastlCommentXiaomi = crawlCommentXiaomiMapper.getLastComment(param);

            // 循环爬取每页数据
            Integer totalPageNum = 1;
            for (int pageNum = 1; pageNum < totalPageNum; pageNum++) {
                String url = this.getHttpRequestParams(URL, pageNum, PAGE_SIZE, gameCode);
                String pageResponseContent = httpCilentUtil.doGet(url);
                JSONObject pageJson = JSONObject.parseObject(pageResponseContent);// 解析响应json
                // 第一页时获取总评论数，并计算总页数
                if (pageNum == 1) {
                    Integer totalRecordCnt = pageJson.getInteger("totalRecordCnt");// 总评论数
                    totalPageNum = (totalRecordCnt + PAGE_SIZE - 1) / PAGE_SIZE;// 计算页数
                }
                // 解析评论数据
                if (pageJson != null) {
                    JSONArray viewpoints = pageJson.getJSONArray("viewpoints");
                    if (viewpoints != null) {
                        for (int i = 0; i < viewpoints.size(); i++) {
                            JSONObject viewpoint = (JSONObject) viewpoints.get(i);
                            JSONObject userInfo = viewpoint.getJSONObject("userInfo");
                            String viewpointId = viewpoint.getString("viewpointId");
                            String uuid = userInfo.getString("uuid");
                            String nickname = userInfo.getString("nickname");
                            Integer sex = userInfo.getInteger("sex");
                            String content = viewpoint.getString("content");
                            Integer score = viewpoint.getInteger("score");
                            Integer likeCnt = viewpoint.getInteger("likeCnt");
                            Integer replyCnt = viewpoint.getInteger("replyCnt");
                            Integer viewCount = viewpoint.getInteger("viewCount");
                            Date createTime = viewpoint.getDate("createTime");
                            Date updateTime = viewpoint.getDate("updateTime");
                            Integer playDuration = viewpoint.getInteger("playDuration");
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }

    /**
     * 组织HTTP请求参数
     *
     * @param url
     * @param pageNum
     * @param pageSize
     * @param gameCode
     * @return
     */
    private String getHttpRequestParams(String url, Integer pageNum, Integer pageSize, Integer gameCode) {
        StringBuffer params = new StringBuffer();
        params.append(url).append("?");
        params.append("uuid=0");
        params.append("&gameId=").append(gameCode);
        params.append("&sortType=4");
        params.append("&page=").append(pageNum);
        params.append("&pageSize=").append(pageSize);
        params.append("&dataType=1");
        params.append("&relObjId=").append(gameCode);
        params.append("&relObjType=1");
        params.append("&owner=1");
        return params.toString();
    }

    public static void main(String[] args) throws Exception {
        Integer gameCode = 62281356;
        HttpCilentUtil httpCilentUtil = new HttpCilentUtil();


        // 循环爬取每页数据
        Integer totalPageNum = 1;
        for (int pageNum = 1; pageNum <= totalPageNum; pageNum++) {
            StringBuffer params = new StringBuffer();
            params.append(URL).append("?");
            params.append("uuid=0");
            params.append("&gameId=").append(gameCode);
            params.append("&sortType=4");
            params.append("&page=").append(pageNum);
            params.append("&pageSize=").append(10);
            params.append("&dataType=1");
            params.append("&relObjId=").append(gameCode);
            params.append("&relObjType=1");
            params.append("&owner=1");
            String pageResponseContent = httpCilentUtil.doGet(params.toString());
            JSONObject pageJson = JSONObject.parseObject(pageResponseContent);
            if (pageNum == 1) {
                Integer totalRecordCnt = pageJson.getInteger("totalRecordCnt");// 评论条数
                totalPageNum = (totalRecordCnt + PAGE_SIZE - 1) / PAGE_SIZE;// 计算页数
            }
            if (pageJson != null) {
                JSONArray viewpoints = pageJson.getJSONArray("viewpoints");
                if (viewpoints != null) {
                    for (int i = 0; i < viewpoints.size(); i++) {
                        JSONObject viewpoint = (JSONObject) viewpoints.get(i);
                        JSONObject userInfo = viewpoint.getJSONObject("userInfo");
                        String viewpointId = viewpoint.getString("viewpointId");
                        String uuid = userInfo.getString("uuid");
                        String nickname = userInfo.getString("nickname");
                        Integer sex = userInfo.getInteger("sex");
                        String content = viewpoint.getString("content");
                        Integer score = viewpoint.getInteger("score");
                        Integer likeCnt = viewpoint.getInteger("likeCnt");
                        Integer replyCnt = viewpoint.getInteger("replyCnt");
                        Integer viewCount = viewpoint.getInteger("viewCount");
                        Date createTime = viewpoint.getDate("createTime");
                        Date updateTime = viewpoint.getDate("updateTime");
                        Integer playDuration = viewpoint.getInteger("playDuration");
                    }
                }
            }
        }
//        try {
//            String title = "员工任务档案明细数据";
//            // 标题
//            List<String> titles = Lists.newArrayList();
//            titles.add("序号");
//            titles.add("姓名");
//            titles.add("所属岗位");
//            // 数据
//            List<List<String>> values = new ArrayList<>();
//            List<String> sheetValueList = new ArrayList<>();
//            sheetValueList.add(String.valueOf(1));// 序号
//            sheetValueList.add("张三");//姓名
//            sheetValueList.add("岗位");//所属岗位
//            values.add(sheetValueList);
//            File file = ExcelUtils.exportDyExcelFile(titles,values, "导出文件","sheet1", "C:\\Users\\Administrator\\Downloads");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
