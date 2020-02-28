package com.vishnu.aggarwal.quartz.core.exceptions;

/*
Created by vishnu on 1/3/18 11:37 AM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Pause trigger failure exception.
 */
@Getter
@Setter
public class PauseTriggerFailureException extends RuntimeException {
    /**
     * Instantiates a new Pause trigger failure exception.
     *
     * @param message the message
     */
    public PauseTriggerFailureException(String message) {
        super(message);
    }
}
