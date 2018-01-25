package com.vishnu.aggarwal.core.dto;

import com.vishnu.aggarwal.core.co.APIJobDataCO;
import com.vishnu.aggarwal.core.co.CronJobSchedulerDataCO;
import com.vishnu.aggarwal.core.co.JobCO;
import com.vishnu.aggarwal.core.co.SimpleJobSchedulerDataCO;
import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import com.vishnu.aggarwal.core.enums.JobType;
import com.vishnu.aggarwal.core.enums.ScheduleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartzDTO {
    private JobCO job;
    private ScheduleType scheduleType;
    private JobExecutorClass executorClass;
    private APIJobDataCO apiJobData;
}
