package com.worm.little.mapper;

import com.worm.little.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(SysUser record);

    SysUser selectByPrimaryKey(Integer userId);

    SysUser selectByUserName(String userName);

    List<SysUser> selectAll();

    int updateByPrimaryKey(SysUser record);
}