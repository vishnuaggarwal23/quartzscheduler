package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:41 PM
*/

import lombok.Getter;
import lombok.Setter;

/**
 * The type Schedule type not found exception.
 */
@Getter
@Setter
public class ScheduleTypeNotFoundException extends Exception {
    /**
     * Instantiates a new Schedule type not found exception.
     */
    public ScheduleTypeNotFoundException() {
        super("no.scheduling.type.found");
    }
}
