package com.worm.little.interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 * <p>
 * Created by Administrator on 2019/8/27.
 */
public class WebMvcConfg implements WebMvcConfigurer {

    public final static String SESSION_KEY = "user";

    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截的请求，并排除几个不拦截的请求
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/index.html", "/", "/user/login");
    }
}
