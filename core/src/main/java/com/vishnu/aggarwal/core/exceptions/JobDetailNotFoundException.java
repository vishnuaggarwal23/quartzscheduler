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
public class JobDetailNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Job detail not found exception.
     *
     * @param exceptionMessage the exception message
     */
    public JobDetailNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
