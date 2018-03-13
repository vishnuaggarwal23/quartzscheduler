package com.vishnu.aggarwal.core.service;

/*
Created by vishnu on 5/3/18 10:37 AM
*/

import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import org.springframework.beans.factory.annotation.Autowired;

import static java.text.MessageFormat.format;

/**
 * The type Base service.
 */
public abstract class BaseService {

    /**
     * The Base message resolver.
     */
    @Autowired
    BaseMessageResolver baseMessageResolver;

    /**
     * Gets message.
     *
     * @param messageCode the message code
     * @return the message
     */
    protected String getMessage(String messageCode) {
        return baseMessageResolver.getMessage(messageCode);
    }

    protected String formatMessage(String messagePattern, Object... messageArguments) {
        return format(messagePattern, messageArguments);
    }
}
