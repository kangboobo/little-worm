package com.worm.little.controller;

import com.worm.little.entity.SysUser;
import com.worm.little.service.UserService;
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
import java.util.List;

/**
 * 用户管理类
 * <p>
 * Created by Administrator on 2019/8/23.
 */
@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    /**
     * 查询用户列表
     *
     * @param keyword
     * @param model
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/userManage", method = RequestMethod.GET)
    public Object getUserList(@RequestParam(value = "keyword", required = false) String keyword,
                              Model model,
                              HttpServletResponse response,
                              HttpServletRequest request) {

        List<SysUser> sysUsers = userService.getUserList(keyword);
        //放在请求域中
        model.addAttribute("sys_users", sysUsers);
        model.addAttribute("keyword", keyword);
        return "user_manage";
    }
}
