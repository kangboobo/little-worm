package com.worm.little.controller;

import com.worm.little.entity.SysUser;
import com.worm.little.service.UserService;
import com.worm.little.vo.BaseOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
     * 跳转到新增用户页面
     *
     * @return
     */
    @RequestMapping(value = "/to_add_user", method = RequestMethod.GET)
    public Object updateUser(HttpServletResponse response,
                             HttpServletRequest request) {

        return "user_manage_add";
    }

    /**
     * 新增用户
     *
     * @return
     */
    @RequestMapping(value = "/user_add", method = RequestMethod.POST)
    public Object addUser(@RequestParam(value = "userId", required = false) String userId,
                          @RequestParam(value = "userName", required = false) String userName,
                          @RequestParam(value = "password", required = false) String password,
                          @RequestParam(value = "role", required = false) String role,
                          HttpServletResponse response,
                          HttpServletRequest request) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setUserName(userName);
        sysUser.setPassword(password);
        sysUser.setRole(role);
        BaseOut result = userService.insertUser(sysUser);
        return "user_manage";
    }

    /**
     * 跳转到编辑用户页面
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/to_edit_user", method = RequestMethod.GET)
    public Object updateUser(@RequestParam(value = "userId", required = false) String userId,
                             HttpServletResponse response,
                             HttpServletRequest request) {

        return "user_manage_edit";
    }

    /**
     * 编辑用户
     *
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/user_edit", method = RequestMethod.POST)
    public Object updateUser(@RequestBody SysUser sysUser,
                             HttpServletResponse response,
                             HttpServletRequest request) {

        BaseOut result = userService.updateUser(sysUser);
        return result;
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/user_delete", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteUserById(@RequestParam(value = "userId", required = false) String userId,
                                 HttpServletResponse response,
                                 HttpServletRequest request) {

        BaseOut result = userService.deleteUserById(userId);
        return result;
    }
}
