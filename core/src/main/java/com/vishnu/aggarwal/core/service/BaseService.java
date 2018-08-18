package com.vishnu.aggarwal.core.service;

/*
Created by vishnu on 5/3/18 10:37 AM
*/

import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import org.springframework.beans.factory.annotation.Autowired;

import static java.text.MessageFormat.format;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
        return isNotBlank(messageCode) ? baseMessageResolver.getMessage(messageCode) : EMPTY;
    }

    /**
     * Gets message.
     *
     * @param messageCode the message code
     * @param messageArgs the message args
     * @return the message
     */
    protected String getMessage(String messageCode, Object... messageArgs) {
        if (isNotBlank(messageCode)) {
            if (allNotNull(messageArgs)) {
                return baseMessageResolver.getMessage(messageCode, messageArgs);
            }
            return baseMessageResolver.getMessage(messageCode);
        }
        return EMPTY;
    }

    /**
     * Format message string.
     *
     * @param messagePattern   the message pattern
     * @param messageArguments the message arguments
     * @return the string
     */
    protected String formatMessage(String messagePattern, Object... messageArguments) {
        if (isNotBlank(messagePattern)) {
            if (allNotNull(messageArguments)) {
                return format(messagePattern, messageArguments);
            }
            return format(messagePattern);
        }
        return EMPTY;
    }
}
