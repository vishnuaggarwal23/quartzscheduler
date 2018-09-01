package com.vishnu.aggarwal.core.filters;

/*
Created by vishnu on 22/5/18 2:02 PM
*/

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import java.io.IOException;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@Order(HIGHEST_PRECEDENCE)
@CommonsLog
public class RequestFilter implements Filter {

    public RequestFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        //NOOP
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setAttribute(CUSTOM_REQUEST_ID, randomNumeric(10));
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //NOOP
    }
}
