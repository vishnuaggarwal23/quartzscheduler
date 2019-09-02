package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 19/8/18 4:03 PM
*/

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ScheduleType {
    public static final String simple = "simple";
    public static final String SIMPLE = "SIMPLE";
    public static final String cron = "cron";
    public static final String CRON = "CRON";
}
