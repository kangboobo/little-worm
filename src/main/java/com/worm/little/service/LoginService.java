package com.worm.little.service;

import com.worm.little.entity.SysUser;
import com.worm.little.entity.UserCrawlRecord;
import com.worm.little.mapper.CrawlCommentXiaomiMapper;
import com.worm.little.mapper.SysUserMapper;
import com.worm.little.mapper.UserCrawlRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 登录服务类
 * <p>
 * Created by Administrator on 2019/7/23.
 */
@Service
public class LoginService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private CrawlCommentXiaomiMapper crawlCommentXiaomiMapper;

    @Autowired
    private UserCrawlRecordMapper userCrawlRecordMapper;

    /**
     * 校验用户名密码
     *
     * @param userId   用户名
     * @param password 密码
     * @param map      响应信息
     * @return true:校验通过，false:校验失败
     */
    public Boolean login(String userId, String password, Map<String, Object> map) {
        Boolean loginFlag = false;
        try {
            SysUser sysUser = sysUserMapper.selectByUserId(userId);
            if (sysUser != null) {
                if (!sysUser.getPassword().equals(password)) {
                    map.put("msg", "用户密码错误，请重新输入!");
                } else {
                    loginFlag = true;
                }
                map.put("userId", sysUser.getId());
            } else {
                map.put("msg", "用户不存在!");
            }
        } catch (Exception e) {
            logger.error("LoginService login error, userId={}, password={}", userId, password, e);
            map.put("msg", "系统异常，登录失败!");
            loginFlag = false;
        }
        return loginFlag;
    }

    /**
     * 获取首页内容
     *
     * @return 首页内容
     */
    public void index(Map<String, Object> map) {
        // 获取用户数
        List<SysUser> sysUsers = sysUserMapper.selectAll();
        map.put("userCount", sysUsers == null ? 0 : sysUsers.size());

        // 获取爬取游戏数
        List<UserCrawlRecord> userCrawlRecords = userCrawlRecordMapper.selectAll();
        if (!CollectionUtils.isEmpty(userCrawlRecords)) {
            map.put("gameCount", userCrawlRecords.stream().map(m -> m.getGameCode()).distinct().count());
        } else {
            map.put("gameCount", 0);
        }

        // 获取爬取记录数
        Map param = new HashMap();
        Integer count = crawlCommentXiaomiMapper.getCommentListCount(param);
        map.put("commentCount", count == null ? 0 : count);

        // 系统运行天数
        Long days = 0L;
        Date now = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse("2019-09-15");
            Long time = now.getTime() - start.getTime();
            days = time / (1000 * 3600 * 24);
        } catch (Exception e) {
        }
        map.put("runDays", days == null ? 0 : days);

        // 获取用户爬取记录说明
        if (!CollectionUtils.isEmpty(userCrawlRecords)) {
            userCrawlRecords.sort(Comparator.comparing(UserCrawlRecord::getCreateTime).reversed());
            for (int i = 0; i < userCrawlRecords.size(); i++) {
                UserCrawlRecord userCrawlRecord = userCrawlRecords.get(i);
                SysUser sysUser = sysUserMapper.selectByPrimaryKey(userCrawlRecord.getUserId());
                map.put("userName" + i, sysUser.getUserName());
                Long createTime = userCrawlRecord.getCreateTime().getTime();
                Long beforeTime = (now.getTime() - createTime) / 1000;
                if (beforeTime < 3600) {
                    beforeTime = beforeTime / 60;
                    map.put("crawlTime" + i, beforeTime + " 分钟前");
                } else if (beforeTime < 86400) {
                    beforeTime = beforeTime / 3600;
                    map.put("crawlTime" + i, beforeTime + " 小时前");
                } else {
                    beforeTime = beforeTime / (3600 * 24);
                    map.put("crawlTime" + i, beforeTime + " 天前");
                }
                map.put("msg" + i, "爬取《" + userCrawlRecord.getGameCode() + "》，共爬取数据" + userCrawlRecord.getCrawlCount() + "条");
                if(i>5){
                    break;
                }
            }
        }
        map.put("userCrawlRecords", null);
    }
}
