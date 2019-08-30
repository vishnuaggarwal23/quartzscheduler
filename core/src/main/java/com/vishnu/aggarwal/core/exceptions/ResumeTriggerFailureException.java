package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 1/3/18 11:39 AM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeTriggerFailureException extends Exception {
    public ResumeTriggerFailureException(String message) {
        super(message);
    }
}
