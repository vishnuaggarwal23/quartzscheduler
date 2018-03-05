package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 1/3/18 11:39 AM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Resume job failure exception.
 */
@Getter
@Setter
public class ResumeJobFailureException extends Exception {
    /**
     * Instantiates a new Resume job failure exception.
     *
     * @param message the message
     */
    public ResumeJobFailureException(String message) {
        super(message);
    }
}
