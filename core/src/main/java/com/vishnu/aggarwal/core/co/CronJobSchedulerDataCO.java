package com.vishnu.aggarwal.core.co;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CronJobSchedulerDataCO {
    private String triggerKeyName;
    private String triggerGroupName;
    private String triggerDescription;

    private Integer second;

    private Integer minute;

    private Integer hour;

    private Integer dayOfMonth;

    private Integer month;

    private Integer dayOfWeek;

    private Integer year;

    public String getCronExpression() {
        return second + " " + minute + " " + hour + " " + dayOfMonth + " " + month + " " + dayOfWeek + " " + year;
    }
}
