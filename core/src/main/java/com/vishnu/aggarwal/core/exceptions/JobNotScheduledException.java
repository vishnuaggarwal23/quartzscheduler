package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 4:01 PM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Job not scheduled exception.
 */
@Getter
@Setter
public class JobNotScheduledException extends RuntimeException {
    /**
     * Instantiates a new Job not scheduled exception.
     *
     * @param message the message
     */
    public JobNotScheduledException(String message) {
        super(message);
    }
}
