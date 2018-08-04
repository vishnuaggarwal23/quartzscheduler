package com.vishnu.aggarwal.core.config;

import org.springframework.context.annotation.Configuration;

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
}
