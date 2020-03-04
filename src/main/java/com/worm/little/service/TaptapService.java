package com.worm.little.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worm.little.constans.Constans;
import com.worm.little.entity.CrawlCommentXiaomi;
import com.worm.little.entity.TaptapNewGame;
import com.worm.little.entity.UserCrawlRecord;
import com.worm.little.mapper.CrawlCommentXiaomiMapper;
import com.worm.little.mapper.TaptapNewGameMapper;
import com.worm.little.mapper.UserCrawlRecordMapper;
import com.worm.little.utils.ExcelUtils;
import com.worm.little.utils.HttpCilentUtil;
import com.worm.little.utils.IdWorker;
import com.worm.little.vo.CrawlCommentXiaomiVo;
import com.worm.little.vo.TaptapNewGameVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.client.utils.DateUtils;
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

    private static final Integer BATCH_COMMIT_SIZE = 100;

    @Autowired
    private IdWorker idWorker;

    @Value("${export.file.path}")
    private String filePath;

    @Autowired
    private TaptapNewGameMapper taptapNewGameMapper;


    /**
     * 查询taptap预约游戏
     *
     * @param param
     * @return
     */
    public Map<String, Object> getSubscribeGameList(Map<String, Object> param, Integer pageNum, Integer pageSize) {
        logger.info("查询taptap预约游戏");
        Map<String, Object> result = new HashMap<>();
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<TaptapNewGame> taptapNewGames = taptapNewGameMapper.getGameList(param);
        PageInfo pageInfo = new PageInfo(taptapNewGames);
        List<TaptapNewGame> list = pageInfo.getList();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        List<TaptapNewGameVo> resultList = new ArrayList<>(pageSize);
        for (int i = 0; i < list.size(); i++) {
            TaptapNewGame taptapNewGame = list.get(i);
            taptapNewGame.setSort(i + 1 + pageSize * (pageNum - 1));
            TaptapNewGameVo vo = new TaptapNewGameVo();
            BeanUtils.copyProperties(taptapNewGame, vo);
            vo.setCountStr(Objects.isNull(taptapNewGame.getCount()) ? "" : taptapNewGame.getCount().toString());
            vo.setCreateTimeStr(DateFormatUtils.format(taptapNewGame.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            resultList.add(vo);
        }
        pageInfo.setList(resultList);
        result.put("size", taptapNewGameMapper.selectAll().size());
        result.put("data", pageInfo);
        return result;
    }

    /**
     * 导出taptap预约新游
     *
     * @param param
     * @return
     * @throws Exception
     */
    public File exportSubscribeGame(Map<String, Object> param) throws Exception {
        logger.info("taptap预约新游导出");
        // 查询存储的最新游戏
        TaptapNewGame lastGame = taptapNewGameMapper.getLastGame(param);
        // 爬取预约新游
        this.crawTaptapNewGame(lastGame);
        // 查询所有预约新游
        List<TaptapNewGame> taptapNewGames = taptapNewGameMapper.selectAll();
        // 组织标题
        List<String> titles = Lists.newArrayList();
        titles.add("序号");
        titles.add("名称");
        titles.add("评分");
        titles.add("预约量");
        titles.add("厂商");
        titles.add("开发商");
        titles.add("发行商");
        titles.add("落地页");
        // 组织导出数据
        List<List<String>> values = Lists.newArrayList();
        int seqNo = 0;
        if (!CollectionUtils.isEmpty(taptapNewGames)) {
            for (TaptapNewGame taptapNewGame : taptapNewGames) {
                List<String> valueList = new ArrayList<>();
                valueList.add(String.valueOf(seqNo));//序号
                valueList.add(Objects.isNull(taptapNewGame.getGameName()) ? "" : taptapNewGame.getGameName());// 标题
                valueList.add(Objects.isNull(taptapNewGame.getScore()) ? "" : taptapNewGame.getScore());// 评分
                valueList.add(Objects.isNull(taptapNewGame.getCount()) ? "" : taptapNewGame.getCount().toString());// 预约量
                valueList.add(Objects.isNull(taptapNewGame.getCompanyName()) ? "" : taptapNewGame.getCompanyName());// 公司
                valueList.add(Objects.isNull(taptapNewGame.getDeveloperName()) ? "" : taptapNewGame.getDeveloperName());// 开放商
                valueList.add(Objects.isNull(taptapNewGame.getPublisherName()) ? "" : taptapNewGame.getPublisherName());// 发行商
                valueList.add(Objects.isNull(taptapNewGame.getUrl()) ? "" : taptapNewGame.getUrl());// 链接
                values.add(valueList);
            }
        }
        String fileName = "taptap预约新游_" + DateUtils.formatDate(new Date(), "yyyyMMdd");
        return ExcelUtils.exportDynamicExcelFile(titles, values, fileName, "sheet1", filePath);
    }

    /**
     * 爬取taptap预约新游
     *
     * @return
     * @throws Exception
     */
    public void crawTaptapNewGame(TaptapNewGame lastGame) throws Exception {
        logger.info("taptap预约新游爬取");
        String lastGameCode = null;
        Integer sort = 1;
        if (Objects.nonNull(lastGame)) {
            lastGameCode = lastGame.getGameCode();
        }
        List<TaptapNewGame> taptapNewGames = Lists.newArrayList();
        String companyNameTag = "厂商";
        String developerNameTag = "开发商";
        String publisherNameTag = "发行商";
        int pageNum = 1;
        boolean hasNext = true;
        Date now = new Date();
        while (hasNext) {
            String url = this.getHttpRequestParams(URL, pageNum);
            Document document = Jsoup.connect(url).get();
            if (document == null) {
                break;
            }
            Elements appListElement = document.getElementsByClass("taptap-app-item swiper-slide");
            if (CollectionUtils.isEmpty(appListElement)) {
                break;
            }
            int j = 1;
            for (Element appElement : appListElement) {
                j++;
                String title = "";
                String score = "";
                String yuyueCount = "";
                String companyName = "";
                String developerName = "";
                String publisherName = "";
                String appUrl = "";
                String gameCode = "";
                TaptapNewGame taptapNewGame = new TaptapNewGame();
                Elements titileElements = appElement.getElementsByClass("flex-text");
                if (CollectionUtils.isEmpty(titileElements)) {
                    continue;
                }
                title = titileElements.get(0).text();
                if (StringUtils.isEmpty(title)) {
                    continue;
                }
                logger.info("sort={}, title={}", sort, title);
                Elements urlElements = appElement.getElementsByClass("item-caption-title flex-text-overflow taptap-link");
                appUrl = CollectionUtils.isEmpty(urlElements) ? null : urlElements.get(0).attr("href");
                if (StringUtils.isEmpty(appUrl)) {
                    continue;
                }
                gameCode = appUrl.substring(appUrl.lastIndexOf("/") + 1);
                if (StringUtils.isEmpty(gameCode)) {
                    continue;
                }
                // 爬取到上次爬取的最后一个游戏时停止爬取
                if (StringUtils.isNotEmpty(lastGameCode) && Objects.equals(lastGameCode, gameCode)) {
                    hasNext = false;
                    break;
                }
                try {
                    Document appDocument = Jsoup.connect(appUrl).get();
                    if (Objects.isNull(appDocument)) {
                        continue;
                    }
                    Elements scoreElements = appDocument.getElementsByClass("app-rating-score");
                    if (!CollectionUtils.isEmpty(scoreElements)) {
                        score = scoreElements.get(0).text();
                        if (StringUtils.isEmpty(score)) {
                            score = "";
                        }
                    }
                    Elements countElements = appDocument.getElementsByClass("count-stats");
                    if (!CollectionUtils.isEmpty(countElements)) {
                        yuyueCount = countElements.get(0).text();
                        if (StringUtils.isNotEmpty(yuyueCount) && yuyueCount.contains("人预约")) {
                            yuyueCount = yuyueCount.replace("人预约", "");
                        } else {
                            yuyueCount = "";
                        }
                    }
                    Elements companyElements = appDocument.getElementsByClass("header-text-author");
                    if (CollectionUtils.isEmpty(companyElements)) {
                        continue;
                    }
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
                } catch (Exception e) {
                    logger.error("taptap预约新游导出 error, appUrl={}", appUrl, e);
                    continue;
                }
                taptapNewGame.setId(idWorker.nextId());
                taptapNewGame.setSort(sort++);
                taptapNewGame.setGameName(title);// 标题
                taptapNewGame.setGameCode(gameCode);
                taptapNewGame.setScore(score);// 评分
                taptapNewGame.setCount(StringUtils.isNotEmpty(yuyueCount) ? Integer.parseInt(yuyueCount.trim()) : null);// 预约量
                taptapNewGame.setCompanyName(companyName);// 公司
                taptapNewGame.setDeveloperName(developerName);// 开放商
                taptapNewGame.setPublisherName(publisherName);// 发行商
                taptapNewGame.setUrl(appUrl);// 链接
                taptapNewGame.setCreateTime(now);
                taptapNewGames.add(taptapNewGame);
            }
            pageNum++;
            // 爬取结果不为空时保存到数据库
            if (!CollectionUtils.isEmpty(taptapNewGames)) {
                // 分批保存数据
                List<List<TaptapNewGame>> partition = com.google.common.collect.Lists.partition(taptapNewGames, BATCH_COMMIT_SIZE);
                partition.forEach(p -> {
                    taptapNewGameMapper.batchInsert(p);
                });
                taptapNewGames.clear();// 保存成功后清空list，避免内存泄漏
            }
        }
    }

    /**
     * 本地导出taptap预约新游
     *
     * @param param
     * @return
     * @throws Exception
     */
    public File localExportSubscribeGame(Map<String, Object> param) throws Exception {
        logger.info("taptap预约新游导出");
        // 组织标题
        List<String> titles = Lists.newArrayList();
        titles.add("序号");
        titles.add("名称");
        titles.add("评分");
        titles.add("预约量");
        titles.add("厂商");
        titles.add("开发商");
        titles.add("发行商");
        titles.add("落地页");
        // 组织导出数据
        List<List<String>> values = Lists.newArrayList();
        int seqNo = 0;
        String companyNameTag = "厂商";
        String developerNameTag = "开发商";
        String publisherNameTag = "发行商";
        int pageNum = 1;
        boolean hasNext = true;
        while (hasNext) {
            String url = this.getHttpRequestParams(URL, pageNum);
            Document document = Jsoup.connect(url).get();
            if (document == null) {
                break;
            }
            Elements appListElement = document.getElementsByClass("taptap-app-item swiper-slide");
            if (CollectionUtils.isEmpty(appListElement)) {
                break;
            }
            int j = 1;
            for (Element appElement : appListElement) {
                j++;
                seqNo++;
                String title = "";
                String score = "";
                String yuyueCount = "";
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
                logger.info("seqNo={}, title={}", seqNo, title);
                Elements urlElements = appElement.getElementsByClass("item-caption-title flex-text-overflow taptap-link");
                appUrl = CollectionUtils.isEmpty(urlElements) ? null : urlElements.get(0).attr("href");
                if (StringUtils.isEmpty(appUrl)) {
                    continue;
                }
                try {
                    Document appDocument = Jsoup.connect(appUrl).get();
                    if (Objects.isNull(appDocument)) {
                        continue;
                    }
                    Elements scoreElements = appDocument.getElementsByClass("app-rating-score");
                    if (!CollectionUtils.isEmpty(scoreElements)) {
                        score = scoreElements.get(0).text();
                        if (StringUtils.isEmpty(score)) {
                            score = "";
                        }
                    }
                    Elements countElements = appDocument.getElementsByClass("count-stats");
                    if (!CollectionUtils.isEmpty(countElements)) {
                        yuyueCount = countElements.get(0).text();
                        if (StringUtils.isNotEmpty(yuyueCount)) {
                            yuyueCount = yuyueCount.replace("人预约", "");
                        } else {
                            yuyueCount = "";
                        }
                    }
                    Elements companyElements = appDocument.getElementsByClass("header-text-author");
                    if (CollectionUtils.isEmpty(companyElements)) {
                        continue;
                    }
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
                } catch (Exception e) {
                    System.out.println("taptap预约新游导出 error, appUrl = " + appUrl);
                    e.printStackTrace();
                }
                valueList.add(String.valueOf(seqNo));//序号
                valueList.add(title);// 标题
                valueList.add(score);// 评分
                valueList.add(yuyueCount);// 预约量
                valueList.add(companyName);// 公司
                valueList.add(developerName);// 开放商
                valueList.add(publisherName);// 发行商
                valueList.add(appUrl);// 链接
                values.add(valueList);
            }
            pageNum++;
        }
        String fileName = "taptap预约新游_" + DateUtils.formatDate(new Date(), "yyyyMMdd");
        return ExcelUtils.exportDynamicExcelFile(titles, values, fileName, "sheet1", "D:\\work\\git\\little-worm\\export");
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
        TaptapService taptapService = new TaptapService();
        taptapService.localExportSubscribeGame(new HashMap<>());
    }

}
