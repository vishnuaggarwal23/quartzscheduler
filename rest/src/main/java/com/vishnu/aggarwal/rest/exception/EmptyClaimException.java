package com.vishnu.aggarwal.rest.exception;

import io.jsonwebtoken.JwtException;

/*
Created by vishnu on 27/7/18 12:00 PM
*/public class EmptyClaimException extends JwtException {
    public EmptyClaimException(String message) {
        super(message);
    }

    public EmptyClaimException(String message, Throwable cause) {
        super(message, cause);
    }
}
