package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import com.vishnu.aggarwal.core.validation.interfaces.*;
import lombok.*;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Api job data co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class APIJobDataCO {
    @NotNull(
            message = "job executor class is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    UpdateExistingJob.class
            }
    )
    private JobExecutorClass executorClass;

    @NotNull(
            message = "request type is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    UpdateExistingJob.class
            }
    )
    private HttpMethod requestType;

    @NotNull(
            message = "request url is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    UpdateExistingJob.class
            }
    )
    @NotBlank(
            message = "request url is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    UpdateExistingJob.class
            }
    )
    @NotEmpty(
            message = "request url is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    UpdateExistingJob.class
            }
    )
    private String requestUrl;

    @NotNull(
            message = "request headers is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    UpdateExistingJob.class
            }
    )
    @NotEmpty(
            message = "request headers is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    UpdateExistingJob.class
            }
    )
    private List<APIHeaderCO> requestHeaders;

    @NotNull(
            message = "simple job scheduler is required",
            groups = {
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class
            }
    )
    private SimpleJobSchedulerDataCO simpleJobScheduler;

    @NotNull(
            message = "cron job scheduler is required",
            groups = {
                    CreateNewScheduledCronTriggeredJob.class,
                    CreateNewCronTriggerForJob.class,
                    UpdateExistingCronTriggerForJob.class
            }
    )
    private CronJobSchedulerDataCO cronJobScheduler;
}

