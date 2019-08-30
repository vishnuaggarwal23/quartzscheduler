package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 28/2/18 3:51 PM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TriggerDeleteFailureException extends RuntimeException {
    public TriggerDeleteFailureException(String message) {
        super(message);
    }
}
