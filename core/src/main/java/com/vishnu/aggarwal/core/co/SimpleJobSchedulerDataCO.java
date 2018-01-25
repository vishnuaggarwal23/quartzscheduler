package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.RepeatType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SimpleJobSchedulerDataCO {
    private String triggerKeyName;
    private String triggerGroupName;
    private String triggerDescription;
    private Date startTime;
    private RepeatType repeatType;
    private RepeatIntervalCO repeatInterval;
}
