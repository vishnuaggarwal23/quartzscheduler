package com.vishnu.aggarwal.quartz.core.dto;

import com.vishnu.aggarwal.quartz.core.co.DataTableCO;
import com.vishnu.aggarwal.quartz.core.validation.interfaces.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * The type Key group name dto.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class KeyGroupDescriptionDTO extends DataTableCO {
    @NotNull(
            message = "key name is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    FetchTriggerDetailsByJobKeyNameAndJobGroupName.class,
                    UpdateExistingJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    CreateNewCronTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class,
                    UpdateExistingCronTriggerForJob.class
            }
    )
    @NotEmpty(
            message = "key name is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    FetchTriggerDetailsByJobKeyNameAndJobGroupName.class,
                    UpdateExistingJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    CreateNewCronTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class,
                    UpdateExistingCronTriggerForJob.class
            }
    )
    @NotBlank(
            message = "key name is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    FetchTriggerDetailsByJobKeyNameAndJobGroupName.class,
                    UpdateExistingJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    CreateNewCronTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class,
                    UpdateExistingCronTriggerForJob.class
            }
    )
    private String key;

    /*@NotNull(
            message = "group name is required",
            groups = {
                    CreateNewUnscheduledJob.class,
                    CreateNewScheduledSimpleTriggeredJob.class,
                    CreateNewScheduledCronTriggeredJob.class,
                    DeleteTriggers.class,
                    DeleteJobs.class,
                    PauseTriggers.class,
                    ResumeTriggers.class,
                    PauseJobs.class,
                    ResumeJobs.class,
                    FetchJobsByJobGroupName.class,
                    FetchTriggerDetailsByJobKeyNameAndJobGroupName.class,
                    FetchQuartzDetailsByJobGroupName.class,
                    UpdateExistingJob.class,
                    CreateNewSimpleTriggerForJob.class,
                    CreateNewCronTriggerForJob.class,
                    UpdateExistingSimpleTriggerForJob.class,
                    UpdateExistingCronTriggerForJob.class
            }
    )*/
    private UserDTO group;

    @NotNull(
            message = "description is required",
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
    @NotEmpty(
            message = "description is required",
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
    @NotBlank(
            message = "description is required",
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
    private String description;


    public KeyGroupDescriptionDTO(String key, UserDTO group) {
        this.key = key;
        this.group = group;
        this.description = null;
    }

    public KeyGroupDescriptionDTO(KeyGroupDescriptionDTO keyGroupDescriptionDTO) {
        this.key = keyGroupDescriptionDTO.getKey();
        this.group = keyGroupDescriptionDTO.getGroup();
        this.description = keyGroupDescriptionDTO.getDescription();
    }

    public KeyGroupDescriptionDTO(String key) {
        this.key = key;
        this.group = null;
        this.description = null;
    }
}
