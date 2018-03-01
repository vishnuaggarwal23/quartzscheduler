package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:46 PM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Job detail not found exception.
 */
@Getter
@Setter
public class JobDetailNotFoundException extends Exception {
    /**
     * Instantiates a new Job detail not found exception.
     */
    public JobDetailNotFoundException() {
        super("no.job.details.found");
    }
}
