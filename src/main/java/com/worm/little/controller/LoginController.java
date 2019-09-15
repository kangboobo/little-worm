package com.worm.little.controller;

import com.worm.little.service.LoginService;
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
import java.util.Map;

/**
 * 登录管理类
 * <p>
 * Created by Administrator on 2019/7/23.
 */
@Controller
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoginService loginService;

    /**
     * 跳转到登录页
     * @param model
     * @param response
     * @return
     */
    @RequestMapping("/")
    public String index(Model model, HttpServletResponse response) {
        model.addAttribute("name", "simonsfan");
        return "login";
    }

    /**
     * 跳转到首页
     * @param model
     * @param response
     * @return
     */
    @RequestMapping("/index")
    public String home(Map<String, Object> map, Model model, HttpServletResponse response) {
        loginService.index(map);
        return "index";
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param map
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestParam(value = "userName", required = true) String userName,
                        @RequestParam(value = "password", required = true) String password,
                        Map<String, Object> map,
                        HttpServletResponse response,
                        HttpServletRequest request) {

        if (loginService.login(userName, password, map)) {
            request.getSession().setAttribute("userName", userName);//用户名存入该用户的session中
            request.getSession().setAttribute("userId", map.get("userId"));//用户id存入该用户的session中
            return "redirect:/index";
        } else {
            return "login";
        }
    }
}
