package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:47 PM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Trigger detail not found exception.
 */
@Getter
@Setter
public class TriggerDetailNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Trigger detail not found exception.
     *
     * @param message the message
     */
    public TriggerDetailNotFoundException(String message) {
        super(message);
    }
}
