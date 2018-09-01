package com.vishnu.aggarwal.admin.config;

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

    /**
     * Rest application url string.
     *
     * @return the string
     */
    public String restApplicationUrl() {
        return format("%s%s", this.baseUrl, this.contextPath);
    }

    /**
     * Rest application url string.
     *
     * @param baseUri the base uri
     * @return the string
     */
    public String restApplicationUrl(final String baseUri) {
        return format("%s%s%s", this.baseUrl, this.contextPath, baseUri);
    }
}
