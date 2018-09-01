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
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static org.springframework.http.HttpStatus.*;

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
    public ApplicationConfig(
            RequestInterceptor requestInterceptor,
            AuthenticationInterceptor authenticationInterceptor,
            LoginInterceptor loginInterceptor,
            LogoutInterceptor logoutInterceptor) {
        this.requestInterceptor = requestInterceptor;
        this.authenticationInterceptor = authenticationInterceptor;
        this.loginInterceptor = loginInterceptor;
        this.logoutInterceptor = logoutInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).excludePathPatterns(ADMIN_EXCLUDE_REQUEST_INTERCEPTOR_PATTERN);
        registry.addInterceptor(authenticationInterceptor).addPathPatterns(ADMIN_AUTHENTICATION_INTERCEPTOR_INCLUDE_PATTERN).excludePathPatterns(ADMIN_AUTHENTICATION_INTERCEPTOR_EXCLUDE_PATTERN);
        registry.addInterceptor(loginInterceptor).addPathPatterns(ADMIN_LOGIN_INTERCEPTOR_INCLUDE_PATTERN).excludePathPatterns(ADMIN_LOGIN_INTERCEPTOR_EXCLUDE_PATTERN);
        registry.addInterceptor(logoutInterceptor).addPathPatterns(ADMIN_LOGOUT_INTERCEPTOR_INCLUDE_PATTERN).excludePathPatterns(ADMIN_LOGOUT_INTERCEPTOR_EXCLUDE_PATTERN);
    }

    /**
     * Layout dialect layout dialect.
     *
     * @return the layout dialect
     */
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addStatusController("/401", UNAUTHORIZED);
        registry.addStatusController("/403", FORBIDDEN);
        registry.addStatusController("/404", NOT_FOUND);
        registry.addStatusController("/500", INTERNAL_SERVER_ERROR);
        registry.addViewController("/401").setStatusCode(UNAUTHORIZED).setViewName("error/401");
        registry.addViewController("/403").setStatusCode(FORBIDDEN).setViewName("error/403");
        registry.addViewController("/404").setStatusCode(NOT_FOUND).setViewName("error/404");
        registry.addViewController("/500").setStatusCode(INTERNAL_SERVER_ERROR).setViewName("error/500");
    }
}
