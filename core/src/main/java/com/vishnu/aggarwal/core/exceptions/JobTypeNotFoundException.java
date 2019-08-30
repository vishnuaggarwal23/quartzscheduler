package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:43 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobTypeNotFoundException extends RuntimeException {
    public JobTypeNotFoundException(String message) {
        super(message);
    }
}
