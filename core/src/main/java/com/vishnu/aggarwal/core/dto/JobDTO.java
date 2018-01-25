package com.vishnu.aggarwal.core.dto;

import com.vishnu.aggarwal.core.co.APIJobDataCO;
import com.vishnu.aggarwal.core.co.CronJobSchedulerDataCO;
import com.vishnu.aggarwal.core.co.SimpleJobSchedulerDataCO;
import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import com.vishnu.aggarwal.core.enums.JobType;
import com.vishnu.aggarwal.core.enums.ScheduleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDTO {
    private String keyName;
    private String groupName;
    private String description;
    private Boolean durability;
    private Boolean shouldRecover;
    private JobType jobType;
    private ScheduleType scheduleType;
    private JobExecutorClass executorClass;
    private SimpleJobSchedulerDataCO simpleJobScheduler;
    private CronJobSchedulerDataCO cronJobScheduler;
    private APIJobDataCO apiJobData;
    private Boolean scheduled;
}
