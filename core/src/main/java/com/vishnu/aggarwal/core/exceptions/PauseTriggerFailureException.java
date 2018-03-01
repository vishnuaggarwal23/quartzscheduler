package com.vishnu.aggarwal.core.exceptions;

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
public class PauseTriggerFailureException extends Exception {
    /**
     * Instantiates a new Pause trigger failure exception.
     */
    public PauseTriggerFailureException() {
        super("quartz.trigger.pause.failure");
    }
}
