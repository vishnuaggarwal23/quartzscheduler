package com.vishnu.aggarwal.core.config;

/*
Created by vishnu on 1/3/18 2:42 PM
*/

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The type Base message resolver.
 */
@Configuration
public class BaseMessageResolverImpl implements BaseMessageResolver {

    private final MessageSource messageSource;

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
        return isNotBlank(messageCode) ? messageSource.getMessage(messageCode, null, ROOT) : EMPTY;
    }

    @Override
    public String getMessage(String messageCode, Object... args) {
        if (isNotBlank(messageCode)) {
            if (allNotNull(args)) {
                return messageSource.getMessage(messageCode, args, ROOT);
            }
            return messageSource.getMessage(messageCode, null, ROOT);
        }
        return EMPTY;
    }
}
