package com.worm.little.controller;

import com.worm.little.service.UserService;
import com.worm.little.vo.BaseOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     * 前往用户列表页面
     *
     * @return
     */
    @RequestMapping(value = "/user_manage", method = RequestMethod.GET)
    public Object getUserList() {
        return "user_manage";
    }

    /**
     * 查询用户列表
     *
     * @param keyword
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/user_manage_list", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserList(@RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "page_num", defaultValue = "1", required = false) Integer pageNum,
                              @RequestParam(value = "page_size", defaultValue = "20", required = false) Integer pageSize,
                              HttpServletResponse response,
                              HttpServletRequest request) {

        BaseOut result = userService.getUserList(keyword, pageNum, pageSize);
        return result;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/user_delete", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteUserById(@RequestParam(value = "id", required = false) Long id,
                              HttpServletResponse response,
                              HttpServletRequest request) {

        BaseOut result = userService.deleteUserById(id);
        return result;
    }
}
