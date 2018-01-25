package com.vishnu.aggarwal.core.co;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.List;

@Getter
@Setter
public class APIJobDataCO {
    private HttpMethod requestType;
    private String requestUrl;
    private List<APIHeaderCO> requestHeaders;
    private SimpleJobSchedulerDataCO simpleJobScheduler;
    private CronJobSchedulerDataCO cronJobScheduler;
}
