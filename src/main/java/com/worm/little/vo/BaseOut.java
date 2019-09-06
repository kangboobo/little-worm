package com.worm.little.vo;

import com.github.pagehelper.PageInfo;

/**
 * 接口基础返回类
 * <p>
 * Created by Administrator on 2019/9/6.
 */
public class BaseOut {
    private int code = 0;   // 返回码

    private String msg;     // 返回信息

    private String insertFlag; // 新增结果

    private String deleteFlag; // 删除结果

    private String updateFlag; // 更新结果

    private Object infoData;    // 详情数据

    private PageInfo pageData;    // 列表数据

    public BaseOut() {
    }

    public BaseOut(int code) {
        this.code = code;
    }

    public BaseOut(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseOut(int code, String msg, Object infoData) {
        this.code = code;
        this.msg = msg;
        this.infoData = infoData;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInsertFlag() {
        return insertFlag;
    }

    public void setInsertFlag(String insertFlag) {
        this.insertFlag = insertFlag;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public Object getInfoData() {
        return infoData;
    }

    public void setInfoData(Object infoData) {
        this.infoData = infoData;
    }

    public PageInfo getPageData() {
        return pageData;
    }

    public void setPageData(PageInfo pageData) {
        this.pageData = pageData;
    }
}
