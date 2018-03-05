package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 1/3/18 11:39 AM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Resume trigger failure exception.
 */
@Getter
@Setter
public class ResumeTriggerFailureException extends Exception {
    /**
     * Instantiates a new Resume trigger failure exception.
     *
     * @param message the message
     */
    public ResumeTriggerFailureException(String message) {
        super(message);
    }
}
