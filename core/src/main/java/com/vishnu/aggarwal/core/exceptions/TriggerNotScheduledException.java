package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 4:17 PM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Trigger not scheduled exception.
 */
@Getter
@Setter
public class TriggerNotScheduledException extends Exception {
    /**
     * Instantiates a new Trigger not scheduled exception.
     *
     * @param message the message
     */
    public TriggerNotScheduledException(String message) {
        super(message);
    }
}
