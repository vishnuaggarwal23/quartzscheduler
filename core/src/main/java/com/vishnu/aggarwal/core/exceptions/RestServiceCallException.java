package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 9/3/18 11:42 AM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestServiceCallException extends Exception {
    public RestServiceCallException(String message) {
        super(message);
    }
}
