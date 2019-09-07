package com.worm.little.mapper;

import com.worm.little.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    List<SysUser> selectAll();

    int updateByPrimaryKey(SysUser record);

    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    SysUser selectByUserId(String userId);

    /**
     * 根据关键词搜索用户
     *
     * @param keyword
     * @return
     */
    List<SysUser> selectByKeyword(String keyword);
}