package com.vishnu.aggarwal.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.UTF8;
import static java.lang.Boolean.TRUE;

@CommonsLog
public abstract class WebConfig implements WebMvcConfigurer {
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Primary
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message");
        messageSource.setFallbackToSystemLocale(TRUE);
        messageSource.setUseCodeAsDefaultMessage(TRUE);
        messageSource.setDefaultEncoding(UTF8);
        return messageSource;
    }
}
