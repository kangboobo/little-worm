package com.kangboobo.constans;

import java.lang.reflect.Constructor;

/**
 * Created by Administrator on 2019/8/22.
 */
public class SingletonTest {

    public static void main(String[] args) throws Exception{
        Constructor constructor = SingleEnum.class.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        SingleEnum s1 = (SingleEnum)constructor.newInstance();
        SingleEnum s2 = (SingleEnum)constructor.newInstance();
        s1.call();
        s2.call();
        System.out.println(s1 == s2);
    }
}
