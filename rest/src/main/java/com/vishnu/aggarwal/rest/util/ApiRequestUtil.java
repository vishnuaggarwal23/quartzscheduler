package com.vishnu.aggarwal.rest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.dto.JobTriggerResponseDTO;
import com.vishnu.aggarwal.rest.service.JobTriggerResponseService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
        jobTriggerResponseDTO.setJobKeyName(jobExecutionContext.getJobDetail().getKey().getName());
        jobTriggerResponseDTO.setJobGroupName(jobExecutionContext.getJobDetail().getKey().getGroup());
        jobTriggerResponseDTO.setTriggerKeyName(jobExecutionContext.getTrigger().getKey().getName());
        jobTriggerResponseDTO.setTriggerGroupName(jobExecutionContext.getTrigger().getKey().getGroup());
        jobTriggerResponseDTO.setFireTime(jobExecutionContext.getTrigger().getPreviousFireTime());
        return jobTriggerResponseDTO;
    }
}
