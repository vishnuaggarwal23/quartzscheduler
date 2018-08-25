package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.validation.interfaces.CreateNewScheduledCronTriggeredJob;
import com.vishnu.aggarwal.core.validation.interfaces.CreateNewScheduledSimpleTriggeredJob;
import com.vishnu.aggarwal.core.validation.interfaces.CreateNewUnscheduledJob;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * The type Api header co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class APIHeaderCO {
    @NotNull(
            message = "request header key is required",
            groups = {CreateNewUnscheduledJob.class, CreateNewScheduledSimpleTriggeredJob.class, CreateNewScheduledCronTriggeredJob.class}
    )
    @NotBlank(
            message = "request header key is required",
            groups = {CreateNewUnscheduledJob.class, CreateNewScheduledSimpleTriggeredJob.class, CreateNewScheduledCronTriggeredJob.class}
    )
    @NotEmpty(
            message = "request header key is required",
            groups = {CreateNewUnscheduledJob.class, CreateNewScheduledSimpleTriggeredJob.class, CreateNewScheduledCronTriggeredJob.class}
    )
    private String key;
    @NotNull(
            message = "request header value is required",
            groups = {CreateNewUnscheduledJob.class, CreateNewScheduledSimpleTriggeredJob.class, CreateNewScheduledCronTriggeredJob.class}
    )
    @NotBlank(
            message = "request header value is required",
            groups = {CreateNewUnscheduledJob.class, CreateNewScheduledSimpleTriggeredJob.class, CreateNewScheduledCronTriggeredJob.class}
    )
    @NotEmpty(
            message = "request header value is required",
            groups = {CreateNewUnscheduledJob.class, CreateNewScheduledSimpleTriggeredJob.class, CreateNewScheduledCronTriggeredJob.class}
    )
    private String value;
}
