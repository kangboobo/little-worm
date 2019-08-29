package com.worm.little.controller;


import com.worm.little.service.CrawlXiaomiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 小米爬虫控制器
 * <p>
 * Created by Administrator on 2019/8/29.
 */
@Controller
public class CrawlXiaomiController {

    @Autowired
    private CrawlXiaomiService crawlXiaomiService;

    /**
     * 爬取小米游戏论坛评论数据
     *
     * @param gameCode 游戏id
     * @param map
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/crawl/xiaomi", method = RequestMethod.POST)
    public Object gameCommentCrawl(@RequestParam(value = "gameCode", required = true) Integer gameCode,
                                   Map<String, Object> map,
                                   HttpServletResponse response,
                                   HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");// 从该用户的session中获取用户id
        return crawlXiaomiService.gameCommentCrawl(userId, gameCode);
    }
}
