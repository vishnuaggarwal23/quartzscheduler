package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:49 PM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Quartz detail not found exception.
 */
@Getter
@Setter
public class QuartzDetailNotFoundException extends Exception {
    /**
     * Instantiates a new Quartz detail not found exception.
     */
    public QuartzDetailNotFoundException() {
        super("no.quartz.details.found");
    }
}
