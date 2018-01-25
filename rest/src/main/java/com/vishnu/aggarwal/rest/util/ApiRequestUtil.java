package com.vishnu.aggarwal.rest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.dto.JobTriggerResponseDTO;
import com.vishnu.aggarwal.rest.service.JobTriggerResponseService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@CommonsLog
abstract class ApiRequestUtil{

    String url;
    RestTemplate restTemplate;
    HttpHeaders httpHeaders;

    @Autowired
    JobTriggerResponseService jobTriggerResponseService;

    private String convertResponseToString(Object response) {
        try {
            return new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    JobTriggerResponseDTO constructJobTriggerResponseDTO(ResponseEntity<String> responseEntity, JobExecutionContext jobExecutionContext) {
        JobTriggerResponseDTO jobTriggerResponseDTO = new JobTriggerResponseDTO();
        jobTriggerResponseDTO.setResponseCode(responseEntity.getStatusCode().value());
        jobTriggerResponseDTO.setResponseHeader(convertResponseToString(responseEntity.getHeaders()));
        jobTriggerResponseDTO.setResponseBody(responseEntity.getBody());
        jobTriggerResponseDTO.setJobName(jobExecutionContext.getJobDetail().getKey().getName());
        jobTriggerResponseDTO.setTriggerName(jobExecutionContext.getTrigger().getKey().getName());
        jobTriggerResponseDTO.setFireTime(jobExecutionContext.getTrigger().getPreviousFireTime());
        return jobTriggerResponseDTO;
    }

    Map<String, String> convertJobDataMapToHeadersMap(String url, JobDataMap jobDataMap){
        Map<String, String> headers = new HashMap<String, String>();
        final String[] requestUrl = new String[1];
        jobDataMap.keySet().forEach(it -> {
            if (it.equalsIgnoreCase("key")) {
                headers.put(it.split("_")[1], jobDataMap.getString(it));
            } else if (it.equalsIgnoreCase("request-url")) {
                requestUrl[0] = jobDataMap.getString(it);
            }
        });
        url = requestUrl[0];
        return headers;
    }

    void convertHeadersMapToHttpHeaders(HttpHeaders httpHeaders, Map<String, String> headers){
        headers.keySet().forEach(it -> {
            if (it.equalsIgnoreCase("auth")) {
                httpHeaders.add("X-AUTH-TOKEN", headers.get(it));
            } else if (it.equalsIgnoreCase("contentType")) {
                httpHeaders.setContentType(MediaType.parseMediaType(headers.get(it)));
            } else {
                httpHeaders.add(it, headers.get(it));
            }
        });
    }
}
