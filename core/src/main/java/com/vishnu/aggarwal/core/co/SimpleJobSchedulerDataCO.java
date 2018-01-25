package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.RepeatType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SimpleJobSchedulerDataCO {
    private TriggerCO trigger;
    private RepeatType repeatType;
    private RepeatIntervalCO repeatInterval;
}
