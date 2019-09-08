package com.worm.little.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worm.little.constans.ResultMsg;
import com.worm.little.entity.SysUser;
import com.worm.little.mapper.SysUserMapper;
import com.worm.little.utils.IdWorker;
import com.worm.little.vo.BaseOut;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    private IdWorker idWorker;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 获取用户列表
     *
     * @param keyword 姓名关键词
     * @return 用户列表数据
     */
    public BaseOut getUserList(String keyword, Integer pageNum, Integer pageSize) {
        BaseOut result = new BaseOut();
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> sysUsers = null;
        if (null != keyword && StringUtils.isNotEmpty(keyword.trim())) {
            sysUsers = sysUserMapper.selectByKeyword(keyword.trim());
        } else {
            sysUsers = sysUserMapper.selectAll();
        }
        PageInfo pageInfo = new PageInfo(sysUsers);
        /**返回结果*/
        if (CollectionUtils.isEmpty(sysUsers)) {
            result.setCode(1);
            result.setMsg(ResultMsg.NOT_EXISTS_DATA);
        } else {
            result.setCode(0);
            result.setMsg(ResultMsg.SELECT_SUCCESS);
            result.setPageData(pageInfo);
        }
        return result;
    }

    /**
     * 新增用户
     *
     * @param sysUser
     */
    public BaseOut insertUser(SysUser sysUser) {
        BaseOut result = new BaseOut();
        sysUser.setId(idWorker.nextId());
        sysUser.setStatus("1");
        Integer count = sysUserMapper.insert(sysUser);
        /**返回结果*/
        if (count != null && count>0) {
            result.setCode(0);
            result.setMsg(ResultMsg.INSERT_SUCCESS);
        } else {
            result.setCode(1);
            result.setMsg(ResultMsg.INSERT_FAIL);
        }
        return result;
    }

    /**
     * 编辑用户
     *
     * @param sysUser
     */
    public BaseOut updateUser(SysUser sysUser) {
        BaseOut result = new BaseOut();
        Integer count = sysUserMapper.updateByPrimaryKey(sysUser);
        /**返回结果*/
        if (count != null && count>0) {
            result.setCode(0);
            result.setMsg(ResultMsg.UPDATE_SUCCESS);
        } else {
            result.setCode(1);
            result.setMsg(ResultMsg.UPDATE_FAIL);
        }
        return result;
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    public BaseOut deleteUserById(Long id) {
        BaseOut result = new BaseOut();
        Integer count = sysUserMapper.deleteByPrimaryKey(id);
        /**返回结果*/
        if (count != null && count>0) {
            result.setCode(0);
            result.setMsg(ResultMsg.DELETE_SUCCESS);
        } else {
            result.setCode(1);
            result.setMsg(ResultMsg.DELETE_FAIL);
        }
        return result;
    }
}
