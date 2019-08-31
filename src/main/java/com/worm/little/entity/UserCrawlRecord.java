package com.worm.little.entity;

import java.util.Date;

public class UserCrawlRecord {
    private Integer id;

    private Integer userId;

    private Integer systemCode;

    private Integer gameCode;

    private Integer crawlCount;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(Integer systemCode) {
        this.systemCode = systemCode;
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public Integer getCrawlCount() {
        return crawlCount;
    }

    public void setCrawlCount(Integer crawlCount) {
        this.crawlCount = crawlCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}