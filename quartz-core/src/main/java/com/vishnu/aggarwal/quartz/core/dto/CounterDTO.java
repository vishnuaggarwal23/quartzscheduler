package com.vishnu.aggarwal.quartz.core.dto;

/*
Created by vishnu on 15/9/18 2:40 PM
*/

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CounterDTO {
    private Integer jobs;
    private Integer scheduledJobs;
    private Integer unscheduledJobs;
    private Integer triggers;
    private Integer runningTriggers;
}
