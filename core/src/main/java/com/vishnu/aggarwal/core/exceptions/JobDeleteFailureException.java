package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:50 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDeleteFailureException extends Exception {
    public JobDeleteFailureException() {
        super("quartz.jobs.delete.failure");
    }
}
