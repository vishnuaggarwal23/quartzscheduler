package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.enums.ScheduleType;
import lombok.*;

import java.util.Date;

import static java.lang.Boolean.FALSE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TriggerCO {
    private KeyGroupDescriptionDTO details;
    private boolean startNow = FALSE;
    private Date startTime;
    private Date endTime;
    private ScheduleType scheduleType;

    private SimpleJobSchedulerDataCO simpleTrigger;
    private CronJobSchedulerDataCO cronTrigger;

    public Boolean getStartNow() {
        return startNow;
    }
}
