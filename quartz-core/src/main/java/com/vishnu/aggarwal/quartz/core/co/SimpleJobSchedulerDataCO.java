package com.vishnu.aggarwal.quartz.core.co;

import com.vishnu.aggarwal.quartz.core.enums.RepeatType;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.CreateNewScheduledSimpleTriggeredJob;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.CreateNewSimpleTriggerForJob;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.UpdateExistingSimpleTriggerForJob;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * The type Simple job scheduler data co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SimpleJobSchedulerDataCO {
    @NotNull(
            message = "trigger is required",
            groups = {
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class
            }
    )
    private TriggerCO trigger;

    @NotNull(
            message = "repeat type is required",
            groups = {
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class
            }
    )
    private RepeatType repeatType;

    @NotNull(
            message = "repeat interval is required",
            groups = {
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class
            }
    )
    private RepeatIntervalCO repeatInterval;
}