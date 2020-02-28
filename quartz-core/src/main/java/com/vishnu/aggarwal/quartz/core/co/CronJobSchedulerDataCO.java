package com.vishnu.aggarwal.quartz.core.co;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.CreateNewCronTriggerForJob;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.CreateNewScheduledCronTriggeredJob;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.UpdateExistingCronTriggerForJob;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.beans.Transient;

import static java.lang.String.format;

/**
 * The type Cron job scheduler data co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CronJobSchedulerDataCO {
    @NotNull(
            message = "trigger is required",
            groups = {
                    CreateNewScheduledCronTriggeredJob.class,
                    CreateNewCronTriggerForJob.class,
                    UpdateExistingCronTriggerForJob.class
            }
    )
    private TriggerCO trigger;
    private String second;
    private String minute;
    private String hour;
    private String dayOfMonth;
    private String month;
    private String dayOfWeek;
    private String year;

    /**
     * Gets cron expression.
     *
     * @return the cron expression
     */
    @NotNull
    @NotBlank
    @Transient
    @JsonIgnore
    public String getCronExpression() {
        return format("%s %s %s %s %s %s %s", second, minute, hour, dayOfMonth, month, dayOfWeek, year);
    }
}
