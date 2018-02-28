package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:47 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TriggerDetailNotFoundException extends Exception {
    public TriggerDetailNotFoundException() {
        super("no.triggers.details.found");
    }
}
