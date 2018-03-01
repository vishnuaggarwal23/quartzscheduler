package com.vishnu.aggarwal.core.exceptions;

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
public class JobTypeNotFoundException extends Exception {
    /**
     * Instantiates a new Job type not found exception.
     */
    public JobTypeNotFoundException() {
        super("no.job.type.found");
    }
}
