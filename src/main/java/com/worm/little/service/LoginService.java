package com.worm.little.service;

import com.worm.little.mapper.SysUserMapper;
import com.worm.little.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 登录服务类
 *
 * Created by Administrator on 2019/7/23.
 */
@Service
public class LoginService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysUserMapper sysUserMapper;

    public Boolean login(String userName, String password, Map<String, Object> map){
        Boolean loginFlag = false;
        try {
            SysUser sysUser = sysUserMapper.selectByUserName(userName);
            if (sysUser != null) {
                if (!sysUser.getPassword().equals(password)) {
                    map.put("msg", "用户密码错误，请重新输入");
                } else {
                    loginFlag = true;
                }
            } else {
                map.put("msg", "用户不存在");
            }
        }catch (Exception e){
            logger.error("LoginService login error, userName={}, password={}", userName, password, e);
            map.put("msg", "系统异常，登录失败");
            loginFlag = false;
        }
        return loginFlag;
    }
}