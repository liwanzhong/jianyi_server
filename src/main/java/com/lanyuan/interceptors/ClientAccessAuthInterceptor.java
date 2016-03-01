package com.lanyuan.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liwanzhong on 2016/3/1.
 */
public class ClientAccessAuthInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        //todo 进行验证签名(签名通过返回true，否则返回false)

        System.out.println("preHandle------------------>[ClientAccessAuthInterceptor]");
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle------------------>[ClientAccessAuthInterceptor]");
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {
        System.out.println("afterCompletion------------------>[ClientAccessAuthInterceptor]");
    }
}
