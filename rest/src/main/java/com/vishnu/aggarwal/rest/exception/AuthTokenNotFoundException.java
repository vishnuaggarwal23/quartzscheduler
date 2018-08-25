package com.vishnu.aggarwal.rest.exception;

/*
Created by vishnu on 22/8/18 11:03 AM
*/
public class AuthTokenNotFoundException extends RuntimeException {
    public AuthTokenNotFoundException() {
        super();
    }

    public AuthTokenNotFoundException(String message) {
        super(message);
    }

    public AuthTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthTokenNotFoundException(Throwable cause) {
        super(cause);
    }

    protected AuthTokenNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
