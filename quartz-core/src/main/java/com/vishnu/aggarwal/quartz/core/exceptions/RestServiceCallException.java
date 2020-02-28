package com.vishnu.aggarwal.quartz.core.exceptions;

/*
Created by vishnu on 9/3/18 11:42 AM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Rest service call exception.
 */
@Getter
@Setter
public class RestServiceCallException extends Exception {
    /**
     * Instantiates a new Rest service call exception.
     *
     * @param message the message
     */
    public RestServiceCallException(String message) {
        super(message);
    }
}
