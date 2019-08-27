package com.worm.little.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * <p>
 * Created by Administrator on 2019/8/27.
 */

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        if (user == null || user.equals("")) {
//            response.sendRedirect("/index.html");// 未登录用户跳转至登录页
            // 未登录，给出错误信息，
            request.setAttribute("msg", "无权限请先登录");
            // 获取request返回页面到登录页
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;
        }
        return true;// 已登录用户放行
    }
}
