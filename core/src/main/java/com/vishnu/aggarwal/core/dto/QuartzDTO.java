package com.vishnu.aggarwal.core.dto;

import com.vishnu.aggarwal.core.co.APIJobDataCO;
import com.vishnu.aggarwal.core.co.JobCO;
import com.vishnu.aggarwal.core.enums.ScheduleType;
import lombok.*;

/**
 * The type Quartz dto.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuartzDTO {
    private JobCO job;
    private ScheduleType scheduleType;
    private APIJobDataCO apiJobData;
}
