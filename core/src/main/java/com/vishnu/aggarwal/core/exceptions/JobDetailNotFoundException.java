package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:46 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDetailNotFoundException extends Exception {
    public JobDetailNotFoundException() {
        super("no.job.details.found");
    }
}
