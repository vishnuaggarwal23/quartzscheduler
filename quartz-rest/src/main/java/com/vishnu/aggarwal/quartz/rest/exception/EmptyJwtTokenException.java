package com.vishnu.aggarwal.quartz.rest.exception;

/*
Created by vishnu on 27/7/18 11:57 AM
*/

import io.jsonwebtoken.JwtException;

public class EmptyJwtTokenException extends JwtException {
    public EmptyJwtTokenException(String message) {
        super(message);
    }

    public EmptyJwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
