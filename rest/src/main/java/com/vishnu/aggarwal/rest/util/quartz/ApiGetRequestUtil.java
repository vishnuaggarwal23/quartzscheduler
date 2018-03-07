package com.vishnu.aggarwal.rest.util.quartz;

import lombok.extern.apachecommons.CommonsLog;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Api get request util.
 */
@CommonsLog
public class ApiGetRequestUtil extends ApiRequestUtil implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setAccessControlAllowMethods(Collections.singletonList(HttpMethod.GET));
        httpHeaders.setAccessControlRequestMethod(HttpMethod.GET);

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Map<String, String> headers = new HashMap<String, String>();
        jobDataMap.keySet().forEach(it -> {
            if (it.equalsIgnoreCase("key")) {
                headers.put(it.split("_")[1], jobDataMap.getString(it));
            } else if (it.equalsIgnoreCase("request-url")) {
                url = jobDataMap.getString(it);
            }
        });
        headers.keySet().forEach(it -> {
            if (it.equalsIgnoreCase("auth")) {
                httpHeaders.add("X-AUTH-TOKEN", headers.get(it));
            } else if (it.equalsIgnoreCase("contentType")) {
                httpHeaders.setContentType(MediaType.parseMediaType(headers.get(it)));
            } else {
                httpHeaders.add(it, headers.get(it));
            }
        });

        jobTriggerResponseRepoService.save(constructJobTriggerResponseDTO(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>("parameters", httpHeaders), String.class), context));
    }
}
