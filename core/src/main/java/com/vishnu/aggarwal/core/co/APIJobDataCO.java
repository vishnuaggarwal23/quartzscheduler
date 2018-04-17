package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * The type Api job data co.
 */
@Getter
@Setter
public class APIJobDataCO {
    private JobExecutorClass executorClass;
    private HttpMethod requestType;
    private String requestUrl;
    private List<APIHeaderCO> requestHeaders;
    private SimpleJobSchedulerDataCO simpleJobScheduler;
    private CronJobSchedulerDataCO cronJobScheduler;
}
