package com.vishnu.aggarwal.admin.interceptor;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Request interceptor.
 */
@Component
@CommonsLog
public class RequestInterceptor extends com.vishnu.aggarwal.core.interceptor.RequestInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }
}
