package com.vishnu.aggarwal.rest.filters;

/*
Created by vishnu on 22/5/18 2:02 PM
*/

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import java.io.IOException;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;

@Order
@CommonsLog
public class ResponseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //NOOP
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ID = (String) request.getAttribute(CUSTOM_REQUEST_ID);
        log.info("*********** [Response for Request Logging ID " + ID + "] **************");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //NOOP
    }
}
