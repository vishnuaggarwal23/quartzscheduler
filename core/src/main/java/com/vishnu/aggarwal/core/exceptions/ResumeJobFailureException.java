package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 1/3/18 11:39 AM
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeJobFailureException extends Exception {
    public ResumeJobFailureException() {
        super("quartz.job.resume.failure");
    }
}
