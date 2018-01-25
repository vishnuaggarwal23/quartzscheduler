package com.vishnu.aggarwal.rest.interceptor;

import lombok.extern.apachecommons.CommonsLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CommonsLog
public class RequestInterceptor extends com.vishnu.aggarwal.core.interceptor.RequestInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }
}
