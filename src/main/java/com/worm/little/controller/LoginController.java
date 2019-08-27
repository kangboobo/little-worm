package com.worm.little.controller;

import com.worm.little.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录管理类
 *
 * Created by Administrator on 2019/7/23.
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;


    @RequestMapping("/")
    public String index(Model model, HttpServletResponse response) {
        model.addAttribute("name", "simonsfan");
        return "login";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public Object login(@RequestParam(value = "userName", required = true) String userName,
                        @RequestParam(value = "password", required = true)String password,
                        Map<String, Object> map) {

        if(loginService.login(userName, password, map)){
            return "index";
        }else{
            return "login";
        }
    }
}
