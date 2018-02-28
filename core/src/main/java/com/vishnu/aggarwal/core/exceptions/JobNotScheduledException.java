package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 4:01 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobNotScheduledException extends Exception {
    public JobNotScheduledException() {
        super("quartz.job.scheduling.exception");
    }
}
