package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:51 PM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Trigger delete failure exception.
 */
@Getter
@Setter
public class TriggerDeleteFailureException extends Exception {
    /**
     * Instantiates a new Trigger delete failure exception.
     */
    public TriggerDeleteFailureException() {
        super("quartz.triggers.delete.failure");
    }
}
