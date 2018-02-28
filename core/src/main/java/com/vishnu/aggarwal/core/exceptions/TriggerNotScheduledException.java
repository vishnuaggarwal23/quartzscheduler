package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 4:17 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TriggerNotScheduledException extends Exception {
    public TriggerNotScheduledException() {
        super("quartz.trigger.scheduling.exception");
    }
}
