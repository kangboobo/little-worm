package com.worm.little.vo;

import com.worm.little.entity.TaptapNewGame;

public class TaptapNewGameVo extends TaptapNewGame {

    private String countStr;

    private String createTimeStr;

    public String getCountStr() {
        return countStr;
    }

    public void setCountStr(String countStr) {
        this.countStr = countStr;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }
}
