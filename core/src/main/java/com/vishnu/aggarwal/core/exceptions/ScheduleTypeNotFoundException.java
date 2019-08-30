package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:41 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleTypeNotFoundException extends Exception {
    public ScheduleTypeNotFoundException(String message) {
        super(message);
    }
}
