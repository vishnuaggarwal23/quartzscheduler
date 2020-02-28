package com.vishnu.aggarwal.quartz.admin.thymeleaf.config;

/*
Created by vishnu on 9/3/18 10:25 AM
*/

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * The type Rest application config.
 */
@Component
public class RestApplicationConfig {

    @Value("${rest.application.baseUrl}")
    private String baseUrl;

    @Value("${rest.application.contextPath}")
    private String contextPath;

    @Value("${rest.application.host}")
    private String host;

    @Value(("${rest.application.port}"))
    private Integer port;

    /**
     * Rest application url string.
     *
     * @return the string
     */
    public String restApplicationUrl() {
        return format("%s%s", this.baseUrl, this.contextPath).trim();
    }

    /**
     * Rest application url string.
     *
     * @param baseUri the base uri
     * @return the string
     */
    public String restApplicationUrl(final String baseUri) {
        return format("%s%s", restApplicationUrl(), baseUri).trim();
    }
}
