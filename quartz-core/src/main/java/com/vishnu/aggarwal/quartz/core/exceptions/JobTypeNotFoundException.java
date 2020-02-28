package com.vishnu.aggarwal.quartz.core.exceptions;

/*
Created by vishnu on 28/2/18 3:43 PM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Job type not found exception.
 */
@Getter
@Setter
public class JobTypeNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Job type not found exception.
     *
     * @param message the message
     */
    public JobTypeNotFoundException(String message) {
        super(message);
    }
}
