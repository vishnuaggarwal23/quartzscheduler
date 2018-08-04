package com.vishnu.aggarwal.rest.exception;

/*
Created by vishnu on 27/7/18 12:09 PM
*/
public class UserRuntimeException extends RuntimeException {
    public UserRuntimeException() {
    }

    public UserRuntimeException(String message) {
        super(message);
    }

    public UserRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
