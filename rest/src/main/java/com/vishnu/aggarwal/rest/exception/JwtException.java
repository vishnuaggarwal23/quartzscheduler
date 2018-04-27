package com.vishnu.aggarwal.rest.exception;

import org.springframework.security.core.AuthenticationException;

/*
Created by vishnu on 21/4/18 6:16 PM
*/public class JwtException extends AuthenticationException{

    public JwtException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public JwtException(String message) {
        super(message);
    }
}
