package com.vishnu.aggarwal.rest.exception;

/*
Created by vishnu on 22/8/18 10:29 AM
*/

import org.springframework.security.core.AuthenticationException;

public class PrincipalNotFoundException extends AuthenticationException {
    public PrincipalNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public PrincipalNotFoundException(String msg) {
        super(msg);
    }
}
