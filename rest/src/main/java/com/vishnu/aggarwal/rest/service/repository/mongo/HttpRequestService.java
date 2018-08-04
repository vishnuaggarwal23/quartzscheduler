package com.vishnu.aggarwal.rest.service.repository.mongo;

/*
Created by vishnu on 23/5/18 3:53 PM
*/

import com.vishnu.aggarwal.rest.document.HttpRequest;
import com.vishnu.aggarwal.rest.repository.mongo.HttpRequestRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static org.springframework.web.util.WebUtils.getSessionId;

@Service
@CommonsLog
public class HttpRequestService {

    @Autowired
    HttpRequestRepository httpRequestRepository;

    public void save(HttpServletRequest httpServletRequest) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(httpServletRequest.getRequestURL().toString());
        httpRequest.setUri(httpServletRequest.getRequestURI());
        httpRequest.setRequestedSessionId(httpServletRequest.getRequestedSessionId());
        httpRequest.setContextPath(httpServletRequest.getContextPath());
        httpRequest.setRemoteAddress(httpServletRequest.getRemoteAddr());
        httpRequest.setRemoteUser(httpServletRequest.getRemoteUser());
        httpRequest.setRemotePort(httpServletRequest.getRemotePort());
        httpRequest.setRemoteHost(httpServletRequest.getRemoteHost());
        httpRequest.setMethod(httpServletRequest.getMethod());
        httpRequest.setSessionId(getSessionId(httpServletRequest));
        httpRequest.setParameters(httpServletRequest.getParameterMap());
        httpRequest.setAuthenticationType(httpServletRequest.getAuthType());
        httpRequest.setHeaders(convertEnumerationToMap(httpServletRequest.getHeaderNames(), httpServletRequest));
        httpRequest.setCookies(httpServletRequest.getCookies());
        httpRequest.setQueryString(httpServletRequest.getQueryString());
        httpRequest.setCustomRequestId(httpServletRequest.getAttribute(CUSTOM_REQUEST_ID).toString());
        httpRequestRepository.save(httpRequest);
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
}
