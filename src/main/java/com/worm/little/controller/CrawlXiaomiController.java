package com.worm.little.controller;


import com.github.pagehelper.PageInfo;
import com.worm.little.entity.CrawlCommentXiaomi;
import com.worm.little.service.CrawlXiaomiService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 小米爬虫控制器
 * <p>
 * Created by Administrator on 2019/8/29.
 */
@Controller
public class CrawlXiaomiController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrawlXiaomiService crawlXiaomiService;

    /**
     * 前往小米评论数据页面
     *
     * @return
     */
    @RequestMapping(value = "/xiaomi_comment", method = RequestMethod.GET)
    public Object getGameComment() {
        return "xiaomi_comment";
    }

    /**
     * 查询小米评论数据
     *
     * @param gameCode 游戏id
     * @param model
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/xiaomi_comment_list", method = RequestMethod.GET)
    public Object getGameCommentList(@RequestParam(value = "game_code", required = false) String gameCode,
                                     @RequestParam(value = "start_date", required = false) String startDate,
                                     @RequestParam(value = "end_date", required = false) String endDate,
                                     Model model,
                                     HttpServletResponse response,
                                     HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");// 从该用户的session中获取用户id
        Map<String,Object> param = new HashMap<>();
        param.put("userId", userId);
        if(StringUtils.isNotEmpty(gameCode)) {
            param.put("gameCode", gameCode.trim());
        }
        if(StringUtils.isNotEmpty(startDate)) {
            param.put("startDate", startDate);
        }
        if(StringUtils.isNotEmpty(endDate)) {
            param.put("endDate", endDate);
        }
        PageInfo<CrawlCommentXiaomi> pageInfo = crawlXiaomiService.getGameCommentList(param);
        //放在请求域中
        model.addAttribute("pageInfo", pageInfo);
        return "xiaomi_comment::table_refresh";
    }

    /**
     * 爬取小米游戏论坛评论数据
     *
     * @param gameCode 游戏id
     * @param model
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/xiaomi_crawl", method = RequestMethod.GET)
    public Object gameCommentCrawl(@RequestParam(value = "game_code", required = false) String gameCode,
                                   @RequestParam(value = "start_date", required = false) String startDate,
                                   @RequestParam(value = "end_date", required = false) String endDate,
                                   Model model,
                                   HttpServletResponse response,
                                   HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");// 从该用户的session中获取用户id
        return crawlXiaomiService.gameCommentCrawl(userId, Long.parseLong(gameCode));
    }
}
