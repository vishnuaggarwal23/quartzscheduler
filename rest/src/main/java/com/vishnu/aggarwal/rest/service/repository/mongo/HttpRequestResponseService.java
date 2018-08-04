package com.vishnu.aggarwal.rest.service.repository.mongo;

/*
Created by vishnu on 24/5/18 10:28 AM
*/

import com.vishnu.aggarwal.rest.document.HttpRequest;
import com.vishnu.aggarwal.rest.document.HttpRequestResponse;
import com.vishnu.aggarwal.rest.document.HttpResponse;
import com.vishnu.aggarwal.rest.repository.mongo.HttpRequestResponseRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.web.util.WebUtils.*;

@Service
@CommonsLog
public class HttpRequestResponseService {

    @Autowired
    HttpRequestResponseRepository httpRequestResponseRepository;

    public void save(ContentCachingRequestWrapper httpServletRequestToLog, ContentCachingResponseWrapper httpServletResponseToLog) {
        HttpRequest httpRequest = new HttpRequest();
        HttpResponse httpResponse = new HttpResponse();
        HttpRequestResponse httpRequestResponse = new HttpRequestResponse();

        httpRequest.setUrl(httpServletRequestToLog.getRequestURL().toString());
        httpRequest.setUri(httpServletRequestToLog.getRequestURI());
        httpRequest.setRequestedSessionId(httpServletRequestToLog.getRequestedSessionId());
        httpRequest.setContextPath(httpServletRequestToLog.getContextPath());
        httpRequest.setRemoteAddress(httpServletRequestToLog.getRemoteAddr());
        httpRequest.setRemoteUser(httpServletRequestToLog.getRemoteUser());
        httpRequest.setRemotePort(httpServletRequestToLog.getRemotePort());
        httpRequest.setRemoteHost(httpServletRequestToLog.getRemoteHost());
        httpRequest.setMethod(httpServletRequestToLog.getMethod());
        httpRequest.setSessionId(getSessionId(httpServletRequestToLog));
        httpRequest.setParameters(httpServletRequestToLog.getParameterMap());
        httpRequest.setAuthenticationType(httpServletRequestToLog.getAuthType());
        httpRequest.setHeaders(convertRequestHeadersToMap(httpServletRequestToLog.getHeaderNames(), httpServletRequestToLog));
        httpRequest.setCookies(httpServletRequestToLog.getCookies());
        httpRequest.setQueryString(httpServletRequestToLog.getQueryString());
        httpRequest.setCustomRequestId(httpServletRequestToLog.getAttribute(CUSTOM_REQUEST_ID).toString());
        httpRequest.setLocale(httpServletRequestToLog.getLocale());
        httpRequest.setCharacterEncoding(httpServletRequestToLog.getCharacterEncoding());
        httpRequest.setContentType(httpServletRequestToLog.getContentType());
        try {
            httpRequest.setPayload(getRequestDataPayload(httpServletRequestToLog));
        } catch (IOException e) {
            log.error("**************** Error while fetching Request Payload for [Request Id ] " + httpServletRequestToLog.getAttribute(CUSTOM_REQUEST_ID).toString() + " ***************");
            log.error(getStackTrace(e));
        }

        httpResponse.setStatus(httpServletResponseToLog.getStatus());
        httpResponse.setHeaders(convertResponseHeadersToMap(httpServletResponseToLog.getHeaderNames(), httpServletResponseToLog));
        httpResponse.setContentType(httpServletResponseToLog.getContentType());
        httpResponse.setLocale(httpServletResponseToLog.getLocale());
        httpResponse.setCharacterEncoding(httpServletResponseToLog.getCharacterEncoding());
        httpResponse.setCustomRequestId(httpServletRequestToLog.getAttribute(CUSTOM_REQUEST_ID).toString());
        try {
            httpResponse.setPayload(getResponseDataPayload(httpServletResponseToLog));
        } catch (IOException e) {
            log.error("**************** Error while fetching Response Payload for [Request Id ] " + httpServletRequestToLog.getAttribute(CUSTOM_REQUEST_ID).toString() + " ***************");
            log.error(getStackTrace(e));
        }

        httpRequestResponse.setHttpRequest(httpRequest);
        httpRequestResponse.setHttpResponse(httpResponse);
        httpRequestResponseRepository.save(httpRequestResponse);
    }

    private Map convertRequestHeadersToMap(Enumeration<String> enumeration, final ContentCachingRequestWrapper httpServletRequestToLog) {
        Map<String, String> map = new HashMap<String, String>();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = httpServletRequestToLog.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    private Map convertResponseHeadersToMap(Collection<String> collection, final ContentCachingResponseWrapper httpServletResponseToLog) {
        Map<String, String> map = new HashMap<String, String>();
        for (String key : collection) {
            String value = httpServletResponseToLog.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    private String getResponseDataPayload(final ContentCachingResponseWrapper httpServletResponseToLog) throws IOException {
        String payload = EMPTY;
        ContentCachingResponseWrapper contentCachingResponseWrapper = getNativeResponse(httpServletResponseToLog, ContentCachingResponseWrapper.class);
        byte[] bytes = contentCachingResponseWrapper.getContentAsByteArray();
        if (bytes.length > 0) {
            payload = new String(bytes, 0, bytes.length, contentCachingResponseWrapper.getCharacterEncoding());
        }
        contentCachingResponseWrapper.copyBodyToResponse();
        return payload;
    }

    private String getRequestDataPayload(final ContentCachingRequestWrapper httpServletRequestToLog) throws IOException {
        String payload = EMPTY;
        ContentCachingRequestWrapper contentCachingRequestWrapper = getNativeRequest(httpServletRequestToLog, ContentCachingRequestWrapper.class);
        byte[] bytes = contentCachingRequestWrapper.getContentAsByteArray();
        if (bytes.length > 0) {
            payload = new String(bytes, 0, bytes.length, contentCachingRequestWrapper.getCharacterEncoding());
        }
        return payload;
    }
}
