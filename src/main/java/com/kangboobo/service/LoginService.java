package com.kangboobo.service;

import com.kangboobo.mapper.SysUserMapper;
import com.kangboobo.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录服务类
 *
 * Created by Administrator on 2019/7/23.
 */
@Service
public class LoginService {

    @Autowired
    private SysUserMapper sysUserMapper;

    public Object login(String userName, String password){
        SysUser sysUser = sysUserMapper.selectByUserName(userName);
        if(sysUser != null){
            if(!sysUser.getPassword().equals(password)){
                return "用户密码错误";
            }else{
                return "登陆成功";
            }
        }else{
            return "用户不存在";
        }
    }
}
