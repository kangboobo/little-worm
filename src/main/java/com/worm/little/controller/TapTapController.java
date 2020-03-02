package com.worm.little.controller;


import com.worm.little.service.TaptapService;
import com.worm.little.utils.ExportExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Tap爬虫控制器
 * <p>
 * Created by Administrator on 2019/8/29.
 */
@Controller
public class TapTapController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TaptapService taptapService;

    @Autowired
    private ExportExcelUtil exportExcelUtil;

    /**
     * 前往taptap游戏页面
     *
     * @return
     */
    @RequestMapping(value = "/taptap_game", method = RequestMethod.GET)
    public Object getGameComment() {
        return "taptap_game";
    }

    /**
     * 导出taptap预约新游
     *
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/taptap_subscribe_export", method = RequestMethod.GET)
    public Object exportSubscribe(Model model,
                                  HttpServletResponse response,
                                  HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");// 从该用户的session中获取用户id
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        try {
            File file = taptapService.exportSubscribe(param);
            if (file == null) {
                model.addAttribute("code", 1);
                model.addAttribute("msg", "导出数据为空");
                return null;
            }
            // 下载文件到客户端
            exportExcelUtil.exportExcelFromLocal(request, response, file);
            // 下载完毕后删除服务端文件
            if (file.exists()) {
                file.getAbsoluteFile().delete();
            }
        } catch (Exception e) {
            logger.error("/taptap_subscribe_export error", e);
            model.addAttribute("code", 2);
            model.addAttribute("msg", "导出文件失败");
            return null;
        }

        model.addAttribute("code", 0);
        model.addAttribute("msg", "导出文件成功");
        return null;
    }
}
