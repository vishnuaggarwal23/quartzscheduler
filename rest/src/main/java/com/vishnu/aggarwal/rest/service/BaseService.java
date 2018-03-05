package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 5/3/18 10:37 AM
*/

import com.vishnu.aggarwal.core.BaseMessageResolver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Base service.
 */
public class BaseService {

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
}
