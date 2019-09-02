package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 19/8/18 4:02 PM
*/

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class JobType {
    public static final String api = "api";
    public static final String API = "API";
    public static final String FRAGMENT_API = "JOB_TYPE_API";
    public static final String shell_script = "shell_script";
    public static final String SHELL_SCRIPT = "SHELL_SCRIPT";
    public static final String FRAGMENT_SCRIPT = "JOB_TYPE_SHELL_SCRIPT";
}
