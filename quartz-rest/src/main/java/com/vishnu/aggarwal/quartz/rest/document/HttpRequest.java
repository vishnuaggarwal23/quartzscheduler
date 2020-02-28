package com.vishnu.aggarwal.quartz.rest.document;

/*
Created by vishnu on 23/5/18 3:07 PM
*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.servlet.http.Cookie;
import java.util.Locale;
import java.util.Map;

@Document(collection = "http_request")
@Getter
@Setter
@CommonsLog
@ToString
public class HttpRequest extends BaseDocument<String> {

    @Id
    private String id;

    private String url;
    private String uri;
    private String requestedSessionId;
    private String contextPath;
    private String remoteAddress;
    private String remoteUser;
    private Integer remotePort;
    private String remoteHost;
    private String method;
    private String sessionId;
    private Map<String, String[]> parameters;
    private String authenticationType;
    private Map headers;
    private Cookie[] cookies;
    private String queryString;
    private String customRequestId;
    private String payload;
    private Locale locale;
    private String characterEncoding;
    private String contentType;
}
