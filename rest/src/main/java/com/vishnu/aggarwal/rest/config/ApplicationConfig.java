package com.vishnu.aggarwal.rest.config;

import com.vishnu.aggarwal.core.config.WebConfig;
import com.vishnu.aggarwal.rest.interceptor.RequestInterceptor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * The type Web config.
 */
@Configuration
@CommonsLog
public class ApplicationConfig extends WebConfig {
    /**
     * The Request interceptor.
     */
    @Autowired
    RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).excludePathPatterns("/**/js/**/", "/**/css/**/", "/**/img/**/", "/**/font/**/", "/**/fonts/**/");
    }
}
