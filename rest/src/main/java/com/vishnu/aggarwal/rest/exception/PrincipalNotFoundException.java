package com.vishnu.aggarwal.rest.exception;

/*
Created by vishnu on 22/8/18 10:29 AM
*/

import org.springframework.security.core.AuthenticationException;

/**
 * The type Principal not found exception.
 */
public class PrincipalNotFoundException extends AuthenticationException {
    /**
     * Instantiates a new Principal not found exception.
     *
     * @param msg the msg
     * @param t   the t
     */
    public PrincipalNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Instantiates a new Principal not found exception.
     *
     * @param msg the msg
     */
    public PrincipalNotFoundException(String msg) {
        super(msg);
    }
}
