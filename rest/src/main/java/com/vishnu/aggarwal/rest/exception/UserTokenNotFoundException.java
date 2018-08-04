package com.vishnu.aggarwal.rest.exception;

/*
Created by vishnu on 27/7/18 12:05 PM
*/

public class UserTokenNotFoundException extends RuntimeException {
    public UserTokenNotFoundException() {
    }

    public UserTokenNotFoundException(String message) {
        super(message);
    }

    public UserTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
