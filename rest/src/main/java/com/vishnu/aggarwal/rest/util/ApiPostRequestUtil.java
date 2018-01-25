package com.vishnu.aggarwal.rest.util;

import lombok.extern.apachecommons.CommonsLog;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@CommonsLog
public class ApiPostRequestUtil extends ApiRequestUtil implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setAccessControlAllowMethods(Collections.singletonList(HttpMethod.POST));
        httpHeaders.setAccessControlRequestMethod(HttpMethod.POST);
        convertHeadersMapToHttpHeaders(httpHeaders,convertJobDataMapToHeadersMap(url,context.getJobDetail().getJobDataMap()));
        jobTriggerResponseService.save(constructJobTriggerResponseDTO(restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<String>("parameters", httpHeaders), String.class), context));
    }
}
