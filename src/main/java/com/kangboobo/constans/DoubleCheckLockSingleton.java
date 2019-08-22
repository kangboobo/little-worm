package com.kangboobo.constans;

/**
 * Created by Administrator on 2019/8/22.
 */
public class DoubleCheckLockSingleton {
    private static volatile DoubleCheckLockSingleton instance;

    private DoubleCheckLockSingleton(){};
    
    public static DoubleCheckLockSingleton getInstance(){
        if(instance == null){
            synchronized (DoubleCheckLockSingleton.class){
                if (instance == null){
                    instance = new DoubleCheckLockSingleton();
                }
            }
        }
        return instance;
    }

    public void call() {
        System.out.println("SingleEnum:" + this.hashCode());
    }
}
