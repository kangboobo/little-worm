package com.worm.little.entity;

import java.util.Date;

public class CrawlCommentXiaomi {
    private Long id;

    private Integer gameCode;

    private Integer sort;

    private String viewpointId;

    private Integer uuid;

    private String nickname;

    private String sex;

    private Integer score;

    private Integer likeCount;

    private Integer replyCount;

    private Integer viewCount;

    private Date createTime;

    private Date updateTime;

    private Integer playDuration;

    private String content;

    private String topReply;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getViewpointId() {
        return viewpointId;
    }

    public void setViewpointId(String viewpointId) {
        this.viewpointId = viewpointId == null ? null : viewpointId.trim();
    }

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(Integer playDuration) {
        this.playDuration = playDuration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getTopReply() {
        return topReply;
    }

    public void setTopReply(String topReply) {
        this.topReply = topReply == null ? null : topReply.trim();
    }
}