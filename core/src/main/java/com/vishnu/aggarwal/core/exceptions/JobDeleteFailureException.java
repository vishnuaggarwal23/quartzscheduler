package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:50 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDeleteFailureException extends RuntimeException {
    public JobDeleteFailureException(String message) {
        super(message);
    }
}
