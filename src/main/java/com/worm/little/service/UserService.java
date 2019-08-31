package com.worm.little.service;

import com.worm.little.entity.SysUser;
import com.worm.little.mapper.SysUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务类
 * <p>
 * Created by Administrator on 2019/8/23.
 */
@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysUserMapper sysUserMapper;

    public List<SysUser> getUserList(String keyword) {
        List<SysUser> sysUsers = new ArrayList<>();
        if (null != keyword && StringUtils.isNotEmpty(keyword.trim())) {
            sysUsers = sysUserMapper.selectByKeyword(keyword.trim());
        }else{
            sysUsers = sysUserMapper.selectAll();
        }
        return sysUsers;
    }
}
