package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 1/3/18 11:37 AM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Pause job failure exception.
 */
@Getter
@Setter
public class PauseJobFailureException extends RuntimeException {
    /**
     * Instantiates a new Pause job failure exception.
     *
     * @param message the message
     */
    public PauseJobFailureException(String message) {
        super(message);
    }
}
