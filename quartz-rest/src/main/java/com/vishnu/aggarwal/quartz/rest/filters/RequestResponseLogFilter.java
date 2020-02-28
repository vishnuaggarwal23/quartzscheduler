/*
package com.vishnu.aggarwal.quartz.rest.filters;

*/
/*
Created by vishnu on 24/5/18 10:08 AM
*//*


import HttpRequestResponseService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@CommonsLog
@Configuration
@Order(value = LOWEST_PRECEDENCE)
public class RequestResponseLogFilter implements Filter {

    private final HttpRequestResponseService httpRequestResponseService;

    public RequestResponseLogFilter(HttpRequestResponseService httpRequestResponseService) {
        this.httpRequestResponseService = httpRequestResponseService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //NOOP
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String ID = randomNumeric(10);
//        request.setAttribute(CUSTOM_REQUEST_ID, ID);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        ContentCachingRequestWrapper httpServletRequestToLog = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper httpServletResponseToLog = new ContentCachingResponseWrapper(httpServletResponse);
        httpRequestResponseService.save(httpServletRequestToLog, httpServletResponseToLog);
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {
        //NOOP
    }
}
*/
