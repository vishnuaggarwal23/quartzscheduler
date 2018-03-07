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

import static java.lang.Boolean.FALSE;

/**
 * The type Message source config.
 */
@CommonsLog
@Configuration("customMessageSource")
public class MessageSourceConfig {

    /**
     * Message source message source.
     *
     * @return the message source
     */
    @Bean
    @Primary
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message");
        messageSource.setFallbackToSystemLocale(FALSE);
        return messageSource;
    }
}
