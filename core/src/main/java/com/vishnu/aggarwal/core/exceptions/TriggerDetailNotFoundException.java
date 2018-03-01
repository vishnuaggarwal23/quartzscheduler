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
public class TriggerDetailNotFoundException extends Exception {
    /**
     * Instantiates a new Trigger detail not found exception.
     */
    public TriggerDetailNotFoundException() {
        super("no.triggers.details.found");
    }
}
