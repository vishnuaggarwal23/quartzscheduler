package com.vishnu.aggarwal.core.config;

/*
Created by vishnu on 1/3/18 2:59 PM
*/

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.UTF8;
import static java.lang.Boolean.TRUE;

@CommonsLog
@Configuration("customMessageSource")
public class CustomMessageSource {

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
