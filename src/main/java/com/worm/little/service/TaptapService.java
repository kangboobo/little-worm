package com.worm.little.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worm.little.constans.Constans;
import com.worm.little.entity.CrawlCommentXiaomi;
import com.worm.little.entity.UserCrawlRecord;
import com.worm.little.mapper.CrawlCommentXiaomiMapper;
import com.worm.little.mapper.UserCrawlRecordMapper;
import com.worm.little.utils.ExcelUtils;
import com.worm.little.utils.HttpCilentUtil;
import com.worm.little.utils.IdWorker;
import com.worm.little.vo.CrawlCommentXiaomiVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.selector.Html;

import javax.xml.ws.http.HTTPException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * taptap爬虫服务
 * <p>
 * Created by Administrator on 2019/8/20.
 */
@Service
public class TaptapService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String URL = "https://www.taptap.com/category/e378";

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private HttpCilentUtil httpCilentUtil;

    @Value("${export.file.path}")
    private String filePath;

    /**
     * 导出评论数据
     *
     * @param param
     * @return
     * @throws Exception
     */
    public File exportComment(Map<String, Object> param) throws Exception {
        logger.info("小米游戏中心评论数据导出， userId={}, gameCode={}", param.get("userId"), param.get("gameCode"));

        // 组织导出数据
        List<String> titles = Lists.newArrayList();
        List<List<String>> values = Lists.newArrayList();
        titles.add("序号");
        titles.add("用户名");
        titles.add("性别");
        titles.add("评分");
        titles.add("评论");
        titles.add("回复评论");
        titles.add("点赞数");
        titles.add("回复数");
        titles.add("浏览数");
        titles.add("发表时间");
        titles.add("更新时间");
        titles.add("游戏时长");
        /*for (int i = 0; i < crawlCommentXiaomis.size(); i++) {
            CrawlCommentXiaomi crawlCommentXiaomi = crawlCommentXiaomis.get(i);
            crawlCommentXiaomi.setSort(i + 1);
            List<String> valueList = new ArrayList<>(13);
            valueList.add(String.valueOf(crawlCommentXiaomi.getSort()));//序号
            valueList.add(crawlCommentXiaomi.getNickname());// 用户名
            valueList.add(crawlCommentXiaomi.getSex());// 性别
            valueList.add(crawlCommentXiaomi.getScore() == null ? "" : crawlCommentXiaomi.getScore().toString());// 评分
            valueList.add(crawlCommentXiaomi.getContent());//评论
            valueList.add(crawlCommentXiaomi.getTopReply());//回复评论
            valueList.add(crawlCommentXiaomi.getLikeCount() == null ? "" : crawlCommentXiaomi.getLikeCount().toString());//点赞数
            valueList.add(crawlCommentXiaomi.getReplyCount() == null ? "" : crawlCommentXiaomi.getReplyCount().toString());//回复数
            valueList.add(crawlCommentXiaomi.getViewCount() == null ? "" : crawlCommentXiaomi.getViewCount().toString());//浏览数
            valueList.add(crawlCommentXiaomi.getCreateTime() == null ? "" : DateFormatUtils.format(crawlCommentXiaomi.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));//发表时间
            valueList.add(crawlCommentXiaomi.getUpdateTime() == null ? "" : DateFormatUtils.format(crawlCommentXiaomi.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));//更新时间
            valueList.add(crawlCommentXiaomi.getPlayDurationStr() == null ? "" : crawlCommentXiaomi.getPlayDurationStr());//游戏时长
            values.add(valueList);
        }*/
        return ExcelUtils.exportDynamicExcelFile(titles, values, "gameCode", "sheet1", filePath);
    }


    /**
     * 循环爬取每页数据并解析
     *
     * @param crawlCommentXiaomis
     * @param userId
     * @param gameCode
     * @param lastViewpointId
     */
    private Integer gameCommentCrawl(List<CrawlCommentXiaomi> crawlCommentXiaomis, Long userId, Integer gameCode, String lastViewpointId, String startDate, String endDate) throws Exception {
        Integer totalCount = 0;
        Integer totalPageNum = 45;
        boolean isBreak = false;
        for (int pageNum = 1; pageNum <= totalPageNum; pageNum++) {
            if (isBreak) {
                break;
            }
            String url = this.getHttpRequestParams(URL, pageNum);
            String pageResponseContent = httpCilentUtil.doGet(url);

        }
        return totalCount;
    }

    /**
     * 组织HTTP请求参数
     *
     * @param url
     * @param pageNum
     * @return
     */
    private String getHttpRequestParams(String url, Integer pageNum) {
        StringBuffer params = new StringBuffer();
        params.append(url).append("?");
        params.append("page=").append(pageNum);
        return params.toString();
    }


    public static void main(String[] args) throws Exception {
        HttpCilentUtil httpCilentUtil = new HttpCilentUtil();
        TaptapService taptapService = new TaptapService();
        String companyNameTag = "厂商";
        String developerNameTag = "开发商";
        String publisherNameTag = "发行商";
        // 循环爬取每页数据
        Integer totalPageNum = 45;
        List<String> titles = Lists.newArrayList();
        titles.add("序号");
        titles.add("名称");
        titles.add("厂商");
        titles.add("开发商");
        titles.add("发行商");
        titles.add("落地页");
        List<List<String>> values = Lists.newArrayList();
        int seqNo = 0;
        for (int pageNum = 1; pageNum <= totalPageNum; pageNum++) {
            String url = taptapService.getHttpRequestParams(URL, pageNum);
            System.out.println("page_url = " + url);
            Document document = Jsoup.connect(url).get();
            // System.out.println(pageResponseContent);
//            Document document = Jsoup.parse(pageResponseContent);
            if (document == null) {
                System.out.println("page_url document is null " + url);
                break;
            }
            Elements appListElement = document.getElementsByClass("taptap-app-item swiper-slide");
            // System.out.println(appListElement);
            if (CollectionUtils.isEmpty(appListElement)) {
                System.out.println("page_url appListElement is null " + url);
                break;
            }
            int j = 1;
            for (Element appElement : appListElement) {
                j++;
                seqNo++;
                String title = "";
                String companyName = "";
                String developerName = "";
                String publisherName = "";
                String appUrl = "";
                List<String> valueList = new ArrayList<>(13);
                Elements titileElements = appElement.getElementsByClass("flex-text");
                if (CollectionUtils.isEmpty(titileElements)) {
                    continue;
                }
                title = titileElements.get(0).text();
                if (StringUtils.isEmpty(title)) {
                    continue;
                }
                Elements urlElements = appElement.getElementsByClass("item-caption-title flex-text-overflow taptap-link");
                appUrl = CollectionUtils.isEmpty(urlElements) ? null : urlElements.get(0).attr("href");
                System.out.println("app_url = " + appUrl + " seqNo =" + seqNo);
                if (StringUtils.isNotEmpty(appUrl)) {
                    try {
//                        String appHtml = httpCilentUtil.doGet(appUrl);
                        Document appDocument = Jsoup.connect(appUrl).get();
//                        Document appDocument = Jsoup.parse(appHtml);
                        if (appDocument != null) {
                            Elements companyElements = appDocument.getElementsByClass("header-text-author");
                            if (!CollectionUtils.isEmpty(companyElements)) {

                                for (int i = 0; i < companyElements.size(); i++) {
                                    Elements companyInfoElements = companyElements.get(i).getElementsByTag("span");
                                    if (CollectionUtils.isEmpty(companyInfoElements)) {
                                        continue;
                                    }
                                    String type = companyInfoElements.get(0).text();
                                    if (type != null && (companyNameTag.equals(type)
                                            || type.contains(companyNameTag) || companyNameTag.contains(type))) {
                                        companyName = companyInfoElements.get(1).text();
                                    }
                                    if (type != null && (developerNameTag.equals(type)
                                            || type.contains(developerNameTag) || developerNameTag.contains(type))) {
                                        developerName = companyInfoElements.get(1).text();
                                    }
                                    if (type != null && (publisherNameTag.equals(type)
                                            || type.contains(publisherNameTag) || publisherNameTag.contains(type))) {
                                        publisherName = companyInfoElements.get(1).text();
                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        System.out.println("error appUrl = " + appUrl);
                        e.printStackTrace();
                    }
                }
                valueList.add(String.valueOf(seqNo));//序号
                valueList.add(title);
                valueList.add(companyName);//序号
                valueList.add(developerName);//序号
                valueList.add(publisherName);//序号
                valueList.add(appUrl);//序号
                values.add(valueList);
            }
            System.out.println("页j = " + j);
        }
        ExcelUtils.exportDynamicExcelFile(titles, values, "taptap新游预约", "sheet1", "D:\\work\\git\\little-worm\\export");
    }

}
