package com.vishnu.aggarwal.admin.config;

import com.vishnu.aggarwal.admin.interceptor.RequestInterceptor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@CommonsLog
public class WebConfig extends com.vishnu.aggarwal.core.config.WebConfig {
    @Autowired
    RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
    }
}
