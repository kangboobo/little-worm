package com.kangboobo.constans;

/**
 * Created by Administrator on 2019/8/22.
 */
public enum SingleEnum {
    INSTANCE;

    public void call() {
        System.out.println("SingleEnum:" + this.hashCode());
    }
}
