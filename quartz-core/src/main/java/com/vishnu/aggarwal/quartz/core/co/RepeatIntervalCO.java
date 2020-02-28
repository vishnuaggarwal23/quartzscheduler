package com.vishnu.aggarwal.quartz.core.co;

import com.vishnu.aggarwal.quartz.core.validation.interfaces.CreateNewScheduledSimpleTriggeredJob;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.CreateNewSimpleTriggerForJob;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.UpdateExistingSimpleTriggerForJob;
import lombok.*;

import javax.validation.constraints.NotNull;

import static java.lang.Boolean.FALSE;
import static java.util.Objects.isNull;

/**
 * The type Repeat interval co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RepeatIntervalCO {
    @NotNull(
            message = "repeat value is required",
            groups = {
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class
            }
    )
    private Integer repeatValue;

    private Boolean repeatForever = FALSE;
    private Integer repeatCount;

    /**
     * Gets repeat forever.
     *
     * @return the repeat forever
     */
    public Boolean getRepeatForever() {
        return this.repeatForever;
    }

    /**
     * Gets repeat count.
     *
     * @return the repeat count
     */
    public Integer getRepeatCount() {
        return isNull(this.repeatCount) || this.repeatCount < 1 ? 1 : this.repeatCount;
    }
}
