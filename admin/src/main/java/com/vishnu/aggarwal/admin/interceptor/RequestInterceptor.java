package com.vishnu.aggarwal.admin.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@CommonsLog
public class RequestInterceptor extends com.vishnu.aggarwal.core.interceptor.RequestInterceptor {
    public RequestInterceptor(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }
}
