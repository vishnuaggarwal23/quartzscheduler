package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 1/3/18 11:37 AM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PauseTriggerFailureException extends Exception {
    public PauseTriggerFailureException() {
        super("quartz.trigger.pause.failure");
    }
}
