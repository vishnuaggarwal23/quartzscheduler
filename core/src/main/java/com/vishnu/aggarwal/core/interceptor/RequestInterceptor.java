package com.vishnu.aggarwal.core.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static java.util.TimeZone.getDefault;

/**
 * The type Request interceptor.
 */
@CommonsLog
public abstract class RequestInterceptor implements HandlerInterceptor {

    private static String ID;
    /**
     * The Object mapper.
     */
    @Autowired
    ObjectMapper objectMapper;
    private LocalDateTime startTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        ID = RandomStringUtils.randomNumeric(10);
        startTime = now(getDefault().toZoneId());

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("requestUrl", request.getRequestURL());
        data.put("requestUri", request.getRequestURI());
        data.put("requestedSessionId", request.getRequestedSessionId());
        data.put("contextPath", request.getContextPath());
        data.put("remoteAddress", request.getRemoteAddr());
        data.put("requestUser", request.getRemoteUser());
        data.put("requestPort", request.getRemotePort());
        data.put("requestHost", request.getRemoteHost());
        data.put("method", request.getMethod());
        data.put("sessionId", WebUtils.getSessionId(request));
        data.put("parameters", request.getParameterMap());
        data.put("authenticationType", request.getAuthType());
        data.put("headers", convertEnumerationToMap(request.getHeaderNames(), request));
        data.put("cookies", request.getCookies());
        data.put("queryString", request.getQueryString());
//        request.setAttribute(CUSTOM_REQUEST_ID, ID);

        log.info("*************[PREHANDLE Request Interceptor ID " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Request Logging ***************\n " + convertMapToJsonString(data) + "\n");

        return true;
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

    private String convertMapToJsonString(Map data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Duration duration = between(startTime, now(getDefault().toZoneId()));
        log.info("*************************************************************************************");
        log.info("[AFTERCOMPLETION Request Interceptor ID " + ID + "] Total Running Time for Request Interceptor ID " + ID + " is " + Math.abs(duration.toMinutes()) + " Minutes and " + Math.abs(duration.getSeconds()) + " seconds");
        log.info("*************************************************************************************");
        request.removeAttribute(CUSTOM_REQUEST_ID);
    }
}
