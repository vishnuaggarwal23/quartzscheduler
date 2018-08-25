package com.vishnu.aggarwal.core.dto;

import com.vishnu.aggarwal.core.co.APIJobDataCO;
import com.vishnu.aggarwal.core.co.JobCO;
import com.vishnu.aggarwal.core.enums.ScheduleType;
import com.vishnu.aggarwal.core.validation.interfaces.*;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * The type Quartz dto.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuartzDTO {
    @NotNull(
            message = "job is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    UpdateExistingJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    CreateNewCronTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class,
                    UpdateExistingCronTriggerForJob.class
            }
    )
    private JobCO job;

    @NotNull(
            message = "schedule type is required",
            groups = {
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    CreateNewCronTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class,
                    UpdateExistingCronTriggerForJob.class
            }
    )
    private ScheduleType scheduleType;

    @NotNull(
            message = "api data is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    UpdateExistingJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    CreateNewCronTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class,
                    UpdateExistingCronTriggerForJob.class
            }
    )
    private APIJobDataCO apiJobData;
}
