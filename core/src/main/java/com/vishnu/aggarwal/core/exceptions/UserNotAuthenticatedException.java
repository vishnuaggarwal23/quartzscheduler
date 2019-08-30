package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 16/3/18 11:10 AM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
