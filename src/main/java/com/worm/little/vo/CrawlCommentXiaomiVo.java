package com.worm.little.vo;

import com.worm.little.entity.CrawlCommentXiaomi;

public class CrawlCommentXiaomiVo extends CrawlCommentXiaomi {
    private String createTimeStr;

    private String updateTimeStr;

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }
}
