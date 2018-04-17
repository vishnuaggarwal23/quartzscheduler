package com.vishnu.aggarwal.rest.util.quartz;

import lombok.extern.apachecommons.CommonsLog;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.MediaType.parseMediaType;

/**
 * The type Api get request util.
 */
@CommonsLog
public class ApiDeleteRequestUtil extends ApiRequestUtil implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setAccessControlAllowMethods(singletonList(DELETE));
        httpHeaders.setAccessControlRequestMethod(DELETE);

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
                httpHeaders.add(X_AUTH_TOKEN, headers.get(it));
            } else if (it.equalsIgnoreCase("contentType")) {
                httpHeaders.setContentType(parseMediaType(headers.get(it)));
            } else {
                httpHeaders.add(it, headers.get(it));
            }
        });

        jobTriggerResponseRepoService.save(constructJobTriggerResponseDTO(restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<String>("parameters", httpHeaders), String.class), context));
    }
}
