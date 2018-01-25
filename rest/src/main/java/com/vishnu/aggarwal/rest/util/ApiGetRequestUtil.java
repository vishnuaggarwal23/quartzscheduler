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
public class ApiGetRequestUtil extends ApiRequestUtil implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setAccessControlAllowMethods(Collections.singletonList(HttpMethod.GET));
        httpHeaders.setAccessControlRequestMethod(HttpMethod.GET);
        convertHeadersMapToHttpHeaders(httpHeaders, convertJobDataMapToHeadersMap(url, context.getJobDetail().getJobDataMap()));
        jobTriggerResponseService.save(constructJobTriggerResponseDTO(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>("parameters", httpHeaders), String.class), context));
    }
}
