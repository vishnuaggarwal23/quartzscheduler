package com.vishnu.aggarwal.quartz.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * The interface Base message resolver.
 */
/*
Created by vishnu on 1/3/18 2:44 PM
*/
@Configuration(value = "baseMessageResolver")
public interface BaseMessageResolver {
    /**
     * Gets message.
     *
     * @param messageCode the message code
     * @return the message
     */
    String getMessage(String messageCode);

    String getMessage(String messageCode, Object... args);

    @NotNull String getMessage(final String messageCode, final String defaultMessage, @Nullable Object... args);
}
