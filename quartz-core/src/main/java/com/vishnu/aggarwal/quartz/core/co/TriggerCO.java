package com.vishnu.aggarwal.quartz.core.co;

import com.vishnu.aggarwal.quartz.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.*;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static java.lang.Boolean.FALSE;

/**
 * The type Trigger co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TriggerCO {
    @NotNull(
            message = "trigger details is required",
            groups = {
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    CreateNewCronTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class,
                    UpdateExistingCronTriggerForJob.class
            }
    )
    private KeyGroupDescriptionDTO details;

    private Boolean startNow = FALSE;
    private Date startTime;
    private Date endTime;
}
