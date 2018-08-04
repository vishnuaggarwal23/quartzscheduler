package com.vishnu.aggarwal.rest.filters;

/*
Created by vishnu on 22/5/18 2:02 PM
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@Order(HIGHEST_PRECEDENCE)
@CommonsLog
public class RequestFilter implements Filter {

    private final ObjectMapper objectMapper;

    public RequestFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //NOOP
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ID = randomNumeric(10);
        request.setAttribute(CUSTOM_REQUEST_ID, ID);
        /*HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("requestUrl", httpServletRequest.getRequestURL());
        data.put("requestUri", httpServletRequest.getRequestURI());
        data.put("requestedSessionId", httpServletRequest.getRequestedSessionId());
        data.put("contextPath", httpServletRequest.getContextPath());
        data.put("remoteAddress", httpServletRequest.getRemoteAddr());
        data.put("requestUser", httpServletRequest.getRemoteUser());
        data.put("requestPort", httpServletRequest.getRemotePort());
        data.put("requestHost", httpServletRequest.getRemoteHost());
        data.put("method", httpServletRequest.getMethod());
        data.put("sessionId", WebUtils.getSessionId(httpServletRequest));
        data.put("parameters", httpServletRequest.getParameterMap());
        data.put("authenticationType", httpServletRequest.getAuthType());
        data.put("headers", convertEnumerationToMap(httpServletRequest.getHeaderNames(), httpServletRequest));
        data.put("cookies", httpServletRequest.getCookies());
        data.put("queryString", httpServletRequest.getQueryString());

        log.info("*********** [Request Filter Logging ID " + ID + "] **************");
        try {
            log.info(convertMapToJsonString(data) + "\n");
        } catch (Exception e) {
            log.error(getStackTrace(e) + "\n");
        }*/
        chain.doFilter(request, response);
    }

    private Map convertEnumerationToMap(Enumeration<String> enumeration, HttpServletRequest request) {
        Map<String, String> header = new HashMap<String, String>();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            header.put(key, value);
        }
        return header;
    }

    private String convertMapToJsonString(Map data) throws Exception {
        return objectMapper.writeValueAsString(data);
    }

    @Override
    public void destroy() {
        //NOOP
    }
}
