package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:49 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartzDetailNotFoundException extends RuntimeException {
    public QuartzDetailNotFoundException(String message) {
        super(message);
    }
}
