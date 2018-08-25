package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.enums.JobType;
import com.vishnu.aggarwal.core.validation.interfaces.*;
import lombok.*;

import javax.validation.constraints.NotNull;

import static java.lang.Boolean.FALSE;

/**
 * The type Job co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobCO {
    @NotNull(
            message = "job details is required",
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
    private KeyGroupDescriptionDTO details;

    private Boolean durability = FALSE;
    private Boolean recover = FALSE;
    private JobType type;
    private Boolean scheduled = FALSE;
}
