package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import lombok.*;
import org.springframework.http.HttpMethod;

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
    private JobExecutorClass executorClass;
    private HttpMethod requestType;
    private String requestUrl;
    private List<APIHeaderCO> requestHeaders;
    private SimpleJobSchedulerDataCO simpleJobScheduler;
    private CronJobSchedulerDataCO cronJobScheduler;
}
