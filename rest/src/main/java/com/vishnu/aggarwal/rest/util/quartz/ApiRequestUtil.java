package com.vishnu.aggarwal.rest.util.quartz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.dto.JobTriggerResponseDTO;
import com.vishnu.aggarwal.rest.service.repository.jpa.JobTriggerResponseRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * The type Api request util.
 */
@CommonsLog
public abstract class ApiRequestUtil implements Job {

    /**
     * The Url.
     */
    String url;
    /**
     * The Rest template.
     */
    RestTemplate restTemplate;
    /**
     * The Http headers.
     */
    HttpHeaders httpHeaders;

    /**
     * The Job trigger response service.
     */
    @Autowired
    JobTriggerResponseRepoService jobTriggerResponseRepoService;

    private String convertResponseToString(Object response) {
        try {
            return new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Construct job trigger response dto job trigger response dto.
     *
     * @param responseEntity      the response entity
     * @param jobExecutionContext the job execution context
     * @return the job trigger response dto
     */
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
