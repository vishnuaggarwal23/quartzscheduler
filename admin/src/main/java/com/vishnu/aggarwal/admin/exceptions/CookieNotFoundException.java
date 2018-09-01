package com.vishnu.aggarwal.admin.exceptions;

/*
Created by vishnu on 1/9/18 1:05 AM
*/
public class CookieNotFoundException extends RuntimeException {
    public CookieNotFoundException() {
        super();
    }

    public CookieNotFoundException(String message) {
        super(message);
    }

    public CookieNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CookieNotFoundException(Throwable cause) {
        super(cause);
    }

    protected CookieNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
