package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 16/3/18 11:10 AM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type User not authenticated exception.
 */
@Getter
@Setter
public class UserNotAuthenticatedException extends Exception {
    /**
     * Instantiates a new User not authenticated exception.
     *
     * @param message the message
     */
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
