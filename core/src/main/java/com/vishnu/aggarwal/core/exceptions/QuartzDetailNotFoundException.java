package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:49 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartzDetailNotFoundException extends Exception {
    public QuartzDetailNotFoundException() {
        super("no.quartz.details.found");
    }
}
