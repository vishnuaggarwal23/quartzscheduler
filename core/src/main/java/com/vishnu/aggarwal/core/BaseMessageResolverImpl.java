package com.vishnu.aggarwal.core;

/*
Created by vishnu on 1/3/18 2:42 PM
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

/**
 * The type Base message resolver.
 */
@Configuration
public class BaseMessageResolverImpl implements BaseMessageResolver {

    private MessageSource messageSource;

    /**
     * Instantiates a new Base message resolver.
     *
     * @param messageSource the message source
     */
    @Autowired
    public BaseMessageResolverImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageCode) {
        return messageSource.getMessage(messageCode, null, Locale.ROOT);
    }

}
