package com.vishnu.aggarwal.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.config.WebConfig;
import com.vishnu.aggarwal.rest.config.security.AccessDeniedHandler;
import com.vishnu.aggarwal.rest.config.security.LogoutHandler;
import com.vishnu.aggarwal.rest.config.security.LogoutSuccessHandler;
import com.vishnu.aggarwal.rest.filters.RequestFilter;
import com.vishnu.aggarwal.rest.filters.ResponseFilter;
import com.vishnu.aggarwal.rest.interceptor.RequestInterceptor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.lang.Boolean.TRUE;
import static java.time.ZoneId.of;
import static java.util.TimeZone.getTimeZone;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

/**
 * The type Web config.
 */
@Configuration
@CommonsLog
public class ApplicationConfig extends WebConfig {
    /**
     * The Request interceptor.
     */
    private final RequestInterceptor requestInterceptor;

    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new Application config.
     *
     * @param requestInterceptor the request interceptor
     * @param objectMapper
     */
    @Autowired
    public ApplicationConfig(RequestInterceptor requestInterceptor, ObjectMapper objectMapper) {
        this.requestInterceptor = requestInterceptor;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).excludePathPatterns("/**/js/**/", "/**/css/**/", "/**/img/**/", "/**/font/**/", "/**/fonts/**/", "/**/webjars/**/", "/**/webjar/**/");
    }

    /**
     * B crypt password encoder b crypt password encoder.
     *
     * @return the b crypt password encoder
     */
    @Bean
    @Primary
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    /**
     * Account status user details check user details checker.
     *
     * @return the user details checker
     */
    @Bean
    @Primary
    UserDetailsChecker accountStatusUserDetailsCheck() {
        return new AccountStatusUserDetailsChecker();
    }

    /**
     * Logout handler logout handler.
     *
     * @return the logout handler
     */
    @Bean
    @Primary
    LogoutHandler logoutHandler() {
        return new LogoutHandler();
    }

    /**
     * Logout success handler logout success handler.
     *
     * @return the logout success handler
     */
    @Bean
    @Primary
    LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler();
    }

    /**
     * Access denied handler access denied handler.
     *
     * @return the access denied handler
     */
    @Bean
    @Primary
    AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converters.stream()
                .filter(it -> MappingJackson2HttpMessageConverter.class.isAssignableFrom(it.getClass()))
                .findAny()
                .orElse(new MappingJackson2HttpMessageConverter());

        ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
        objectMapper.setTimeZone(getTimeZone(of("UTC")));
        objectMapper.setSerializationInclusion(NON_NULL);
        objectMapper.setSerializationInclusion(NON_EMPTY);
        converters.add(mappingJackson2HttpMessageConverter);
        super.configureMessageConverters(converters);
    }

    @Bean
    @Primary
    public FilterRegistrationBean<RequestFilter> requestFilter() {
        FilterRegistrationBean<RequestFilter> filterRegistrationBean = new FilterRegistrationBean<RequestFilter>();
        filterRegistrationBean.setName(RequestFilter.class.getName());
        filterRegistrationBean.setFilter(new RequestFilter(objectMapper));
        filterRegistrationBean.setOrder(HIGHEST_PRECEDENCE);
        filterRegistrationBean.setEnabled(TRUE);
        return filterRegistrationBean;
    }

    @Bean
    @Primary
    public FilterRegistrationBean<ResponseFilter> responseFilter() {
        FilterRegistrationBean<ResponseFilter> filterRegistrationBean = new FilterRegistrationBean<ResponseFilter>();
        filterRegistrationBean.setName(ResponseFilter.class.getName());
        filterRegistrationBean.setFilter(new ResponseFilter());
        filterRegistrationBean.setOrder(LOWEST_PRECEDENCE);
        filterRegistrationBean.setEnabled(TRUE);
        return filterRegistrationBean;
    }
}
