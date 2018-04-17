package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:50 PM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Job delete failure exception.
 */
@Getter
@Setter
public class JobDeleteFailureException extends RuntimeException {
    /**
     * Instantiates a new Job delete failure exception.
     *
     * @param message the message
     */
    public JobDeleteFailureException(String message) {
        super(message);
    }
}
