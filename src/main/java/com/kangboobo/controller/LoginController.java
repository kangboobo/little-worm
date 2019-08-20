package com.kangboobo.controller;

import com.kangboobo.service.LoginService;
import jdk.internal.org.objectweb.asm.commons.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录管理类
 *
 * Created by Administrator on 2019/7/23.
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestParam(value = "form-username", required = true) String userName,
                        @RequestParam(value = "form-password", required = true)String password) {

        return loginService.login(userName, password);
    }
}
