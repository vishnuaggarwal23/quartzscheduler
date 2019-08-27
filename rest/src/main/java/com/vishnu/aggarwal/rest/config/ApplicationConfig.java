package com.vishnu.aggarwal.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.config.WebConfig;
import com.vishnu.aggarwal.rest.config.security.AccessDeniedHandler;
import com.vishnu.aggarwal.rest.config.security.LogoutHandler;
import com.vishnu.aggarwal.rest.config.security.LogoutSuccessHandler;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interceptor.RequestInterceptor;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.REST_EXCLUDE_REQUEST_INTERCEPTOR_PATTERN;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.UTC;
import static java.time.ZoneId.of;
import static java.util.TimeZone.getTimeZone;

@Configuration
@CommonsLog
public class ApplicationConfig extends WebConfig {
    private final RequestInterceptor requestInterceptor;
    private final ObjectMapper objectMapper;
    private final BaseMessageResolver baseMessageResolver;
    private final Gson gson;
    private final UserService userService;


    @Autowired
    public ApplicationConfig(
            RequestInterceptor requestInterceptor,
            ObjectMapper objectMapper,
            BaseMessageResolver baseMessageResolver,
            Gson gson,
            UserService userService) {
        this.requestInterceptor = requestInterceptor;
        this.objectMapper = objectMapper;
        this.baseMessageResolver = baseMessageResolver;
        this.gson = gson;
        this.userService = userService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).excludePathPatterns(REST_EXCLUDE_REQUEST_INTERCEPTOR_PATTERN);
    }

    @Bean
    @Primary
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    @Bean
    @Primary
    UserDetailsChecker accountStatusUserDetailsCheck() {
        return new AccountStatusUserDetailsChecker();
    }

    @Bean
    @Primary
    LogoutHandler logoutHandler() {
        return new LogoutHandler();
    }

    @Bean
    @Primary
    LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler();
    }

    @Bean
    @Primary
    AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler(baseMessageResolver, gson, objectMapper);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converters.stream()
                .filter(it -> MappingJackson2HttpMessageConverter.class.isAssignableFrom(it.getClass()))
                .findAny()
                .orElse(new MappingJackson2HttpMessageConverter());

        ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
        objectMapper.setTimeZone(getTimeZone(of(UTC)));
        objectMapper.setSerializationInclusion(NON_NULL);
        objectMapper.setSerializationInclusion(NON_EMPTY);
        converters.add(mappingJackson2HttpMessageConverter);
        super.configureMessageConverters(converters);
    }

    @Bean(value = "auditorAware")
    public AuditorAware<User> auditorAware() {
        return new AuditorAwareImpl(userService);
    }
}
