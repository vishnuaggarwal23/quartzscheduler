package com.vishnu.aggarwal.admin.config;

import com.vishnu.aggarwal.admin.interceptor.AuthenticationInterceptor;
import com.vishnu.aggarwal.admin.interceptor.LoginInterceptor;
import com.vishnu.aggarwal.admin.interceptor.LogoutInterceptor;
import com.vishnu.aggarwal.admin.interceptor.RequestInterceptor;
import com.vishnu.aggarwal.core.config.WebConfig;
import lombok.extern.apachecommons.CommonsLog;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * The type Web config.
 */
@Configuration
@CommonsLog
public class ApplicationConfig extends WebConfig {
    private final RequestInterceptor requestInterceptor;
    private final AuthenticationInterceptor authenticationInterceptor;
    private final LoginInterceptor loginInterceptor;
    private final LogoutInterceptor logoutInterceptor;

    /**
     * Instantiates a new Web config.
     *
     * @param requestInterceptor        the request interceptor
     * @param authenticationInterceptor the authentication interceptor
     * @param loginInterceptor          the login interceptor
     * @param logoutInterceptor         the logout interceptor
     */
    @Autowired
    public ApplicationConfig(RequestInterceptor requestInterceptor, AuthenticationInterceptor authenticationInterceptor, LoginInterceptor loginInterceptor, LogoutInterceptor logoutInterceptor) {
        this.requestInterceptor = requestInterceptor;
        this.authenticationInterceptor = authenticationInterceptor;
        this.loginInterceptor = loginInterceptor;
        this.logoutInterceptor = logoutInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).excludePathPatterns("/**/js/**/", "/**/css/**/", "/**/img/**/", "/**/font/**/", "/**/fonts/**/", "/**/webjars/**/", "/**/webjar/**/");
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/**/web/user/**", "/**/web/quartz/**").excludePathPatterns("/**/web", "/**/web/", "/**/web/forgotPassword", "/**/js/**/", "/**/css/**/", "/**/img/**/", "/**/font/**/", "/**/fonts/**/", "/**/webjars/**/", "/**/webjar/**/");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**/web", "/**/web/").excludePathPatterns("/**/js/**/", "/**/css/**/", "/**/img/**/", "/**/font/**/", "/**/fonts/**/", "/**/webjars/**/", "/**/webjar/**/");
        registry.addInterceptor(logoutInterceptor).addPathPatterns("/**/api/user/logout", "/**/api/user/logout/").excludePathPatterns("/**/js/**/", "/**/css/**/", "/**/img/**/", "/**/font/**/", "/**/fonts/**/", "/**/webjars/**/", "/**/webjar/**/");
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
