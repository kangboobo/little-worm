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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;

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

    @Autowired
    private UserCrawlRecordMapper userCrawlRecordMapper;

    @Value("${export.file.path}")
    private String filePath;

    /**
     * 查询评论数据列表
     *
     * @param param
     * @return
     */
    public Map<String,Object> getGameCommentList(Map<String, Object> param, Integer pageNum, Integer pageSize) {
        Map<String,Object> result = new HashMap<>();
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<CrawlCommentXiaomi> crawlCommentXiaomis = crawlCommentXiaomiMapper.getCommentList(param);
        PageInfo pageInfo = new PageInfo(crawlCommentXiaomis);
        List<CrawlCommentXiaomi> list = pageInfo.getList();
        List<CrawlCommentXiaomiVo> resultList = new ArrayList<>(pageSize);
        for (int i = 0; i < list.size(); i++) {
            CrawlCommentXiaomi crawlCommentXiaomi = list.get(i);
            crawlCommentXiaomi.setSort(i + 1 + pageSize * (pageNum - 1));
            CrawlCommentXiaomiVo vo = new CrawlCommentXiaomiVo();
            BeanUtils.copyProperties(crawlCommentXiaomi, vo);
            vo.setCreateTimeStr(DateFormatUtils.format(crawlCommentXiaomi.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            vo.setUpdateTimeStr(DateFormatUtils.format(crawlCommentXiaomi.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            vo.setPlayDuration(crawlCommentXiaomi.getPlayDuration() == null ? 0 : crawlCommentXiaomi.getPlayDuration() / 1000);
            resultList.add(vo);
        }
        pageInfo.setList(resultList);
        result.put("size", crawlCommentXiaomiMapper.getCommentListCount(param));
        result.put("data", pageInfo);
        return result;
    }

    /**
     * 导出评论数据
     *
     * @param param
     * @return
     * @throws Exception
     */
    public File exportComment(Map<String, Object> param) throws Exception {
        String gameCode = (String) param.get("gameCode");
        List<CrawlCommentXiaomi> crawlCommentXiaomis = crawlCommentXiaomiMapper.getCommentList(param);
        if (CollectionUtils.isEmpty(crawlCommentXiaomis)) {

        }
        // 限制导出最大条数
        if (crawlCommentXiaomis.size() > Constans.EXPORT_COUNT_MAX_DEFAULT) {
            crawlCommentXiaomis = crawlCommentXiaomis.subList(0, Constans.EXPORT_COUNT_MAX_DEFAULT);
        }
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
        titles.add("游戏时长（秒）");
        for (int i = 0; i < crawlCommentXiaomis.size(); i++) {
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
            valueList.add(crawlCommentXiaomi.getPlayDuration() == null ? "" : String.valueOf(crawlCommentXiaomi.getPlayDuration() / 1000));//游戏时长
            values.add(valueList);
        }
        return ExcelUtils.exportDynamicExcelFile(titles, values, gameCode, "sheet1", filePath);
    }

    /**
     * 清空评论数据
     *
     * @param param
     * @return
     */
    public Map deleteComment(Map<String, Object> param) {
        Map resultMap = new HashMap();
        int deleteCount = crawlCommentXiaomiMapper.deleteCommentByParam(param);
        if (deleteCount > 0) {
            resultMap.put("deleted", "true");
        } else {
            resultMap.put("deleted", "false");
        }
        return resultMap;
    }

    /**
     * 爬取小米游戏评论数据
     *
     * @param userId
     * @param gameCode
     * @return
     */
    public Object gameCommentCrawl(Long userId, Long gameCode) {
        // 组织请求参数
        StringBuffer params = new StringBuffer();
        try {
            // 获取存储的最新评论，爬取到此评论后终止爬虫
            Map<String, Object> param = new HashMap<>();
            param.put("userId", userId);
            param.put("gameCode", gameCode);
            CrawlCommentXiaomi lastlCommentXiaomi = crawlCommentXiaomiMapper.getLastComment(param);
            String lastViewpointId = null;
            Integer sort = 1;
            if (null != lastlCommentXiaomi) {
                lastViewpointId = lastlCommentXiaomi.getViewpointId();
                sort = lastlCommentXiaomi.getSort() + 1;
            }

            // 循环爬取每页数据
            List<CrawlCommentXiaomi> crawlCommentXiaomis = new ArrayList<>();
            this.gameCommentCrawl(crawlCommentXiaomis, userId, gameCode, lastViewpointId);

            // 爬取结果不为空时保存到数据库
            if (!CollectionUtils.isEmpty(crawlCommentXiaomis)) {
                // 重新排序：按时间正序
                Collections.reverse(crawlCommentXiaomis);
                for (int i = 0; i < crawlCommentXiaomis.size(); i++) {
                    crawlCommentXiaomis.get(i).setSort(sort++);
                }
                crawlCommentXiaomiMapper.batchInsert(crawlCommentXiaomis);

                // 保存爬取记录
                UserCrawlRecord userCrawlRecord = new UserCrawlRecord();
                userCrawlRecord.setId(idWorker.nextId());
                userCrawlRecord.setUserId(userId);
                userCrawlRecord.setSystemCode(1);
                userCrawlRecord.setGameCode(gameCode.intValue());
                userCrawlRecord.setCrawlCount(crawlCommentXiaomis.size());
                userCrawlRecord.setCreateTime(new Date());
                userCrawlRecordMapper.insert(userCrawlRecord);
            }
        } catch (Exception e) {
            logger.error("小米游戏中心爬取异常， userId={}, gameCode={}", userId, gameCode, e);
        }

        return null;
    }

    /**
     * 循环爬取每页数据并解析
     *
     * @param crawlCommentXiaomis
     * @param userId
     * @param gameCode
     * @param lastViewpointId
     */
    private void gameCommentCrawl(List<CrawlCommentXiaomi> crawlCommentXiaomis, Long userId, Long gameCode, String lastViewpointId) {
        Integer totalPageNum = 1;
        for (int pageNum = 1; pageNum <= totalPageNum; pageNum++) {
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
                        if (StringUtils.isNotEmpty(lastViewpointId) && lastViewpointId.equals(viewpointId)) {
                            return; // 爬取到存储的最新评论后终止爬虫
                        }
                        Integer uuid = userInfo.getInteger("uuid");
                        String nickname = userInfo.getString("nickname");
                        Integer sex = userInfo.getInteger("sex");
                        String content = viewpoint.getString("content");
                        Integer score = viewpoint.getInteger("score");
                        Integer likeCnt = viewpoint.getInteger("likeCnt");
                        Integer replyCnt = viewpoint.getInteger("replyCnt");
                        Integer viewCount = viewpoint.getInteger("viewCount");
                        Date createTime = viewpoint.getDate("createTime");
                        Date updateTime = viewpoint.getDate("updateTime");
                        Long playDuration = viewpoint.getLong("playDuration");
                        CrawlCommentXiaomi crawlCommentXiaomi = new CrawlCommentXiaomi();
                        crawlCommentXiaomi.setId(idWorker.nextId());
                        crawlCommentXiaomi.setUserId(userId);
                        crawlCommentXiaomi.setGameCode(gameCode);
                        crawlCommentXiaomi.setViewpointId(viewpointId);
                        crawlCommentXiaomi.setUuid(uuid);
                        crawlCommentXiaomi.setNickname(StringUtils.isNotEmpty(nickname) ? nickname : "匿名用户");
                        if (sex != null) {
                            crawlCommentXiaomi.setSex(sex == 1 ? "男" : "女");
                        } else {
                            crawlCommentXiaomi.setSex("未知");
                        }
                        crawlCommentXiaomi.setContent(content);
                        crawlCommentXiaomi.setScore(score);
                        crawlCommentXiaomi.setLikeCount(likeCnt);
                        crawlCommentXiaomi.setReplyCount(replyCnt);
                        crawlCommentXiaomi.setViewCount(viewCount);
                        crawlCommentXiaomi.setCreateTime(createTime);
                        crawlCommentXiaomi.setUpdateTime(updateTime);
                        crawlCommentXiaomi.setPlayDuration(playDuration);
                        // 置顶回复
                        StringBuffer sb = new StringBuffer();
                        if (viewpoint.containsKey("topReplys")) {
                            JSONArray topReplys = viewpoint.getJSONArray("topReplys");
                            if (topReplys != null) {
                                for (int j = 0; j < (topReplys.size() > 2 ? 2 : topReplys.size()); j++) {
                                    JSONObject topReply = (JSONObject) topReplys.get(j);
                                    if (topReply.containsKey("content")) {
                                        if (j > 0) {
                                            sb.append(" || ");
                                        }
                                        sb.append(topReply.getString("content"));
                                    }
                                }
                            }
                        }
                        crawlCommentXiaomi.setTopReply(sb.toString());
                        crawlCommentXiaomis.add(crawlCommentXiaomi);
                    }
                }
            }
        }
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
    private String getHttpRequestParams(String url, Integer pageNum, Integer pageSize, Long gameCode) {
        StringBuffer params = new StringBuffer();
        params.append(url).append("?");
        params.append("uuid=0");
        params.append("&gameId=").append(gameCode);
        params.append("&sortType=1"); // 按最新排序
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
