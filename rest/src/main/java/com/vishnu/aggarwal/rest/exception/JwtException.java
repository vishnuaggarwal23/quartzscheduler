package com.vishnu.aggarwal.rest.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * The type Jwt exception.
 */
/*
Created by vishnu on 21/4/18 6:16 PM
*/public class JwtException extends AuthenticationException{

    /**
     * Instantiates a new Jwt exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public JwtException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Instantiates a new Jwt exception.
     *
     * @param message the message
     */
    public JwtException(String message) {
        super(message);
    }
}
