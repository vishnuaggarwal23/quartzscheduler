package com.vishnu.aggarwal.rest.util.quartz.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.dto.JobTriggerResponseDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.rest.entity.JobTriggerResponse;
import com.vishnu.aggarwal.rest.service.repository.jpa.JobTriggerResponseRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.Objects;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.REQUEST_HEADER;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.REQUEST_URL;
import static java.lang.Long.valueOf;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;

@CommonsLog
abstract class ApiRequestUtil implements Job {

    String url;
    RestTemplate restTemplate;
    HttpHeaders httpHeaders;

    JobExecutionException jobExecutionException;

    @Autowired
    JobTriggerResponseRepoService jobTriggerResponseRepoService;

    private ApiRequestUtil() {
        this.restTemplate = new RestTemplate();
        this.httpHeaders = new HttpHeaders();
    }

    ApiRequestUtil(HttpMethod httpMethod) {
        this();
        this.httpHeaders.setAccessControlAllowMethods(singletonList(httpMethod));
        this.httpHeaders.setAccessControlRequestMethod(httpMethod);
    }

    private String convertResponseToString(Object response) {
        try {
            return new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("Exception occurred", getRootCause(e));
            return null;
        }
    }

    private JobTriggerResponseDTO constructJobTriggerResponseDTO(ResponseEntity<Object> responseEntity, JobExecutionContext jobExecutionContext) {
        JobTriggerResponseDTO jobTriggerResponseDTO = new JobTriggerResponseDTO();
        jobTriggerResponseDTO.setResponseCode(responseEntity.getStatusCode().value());
        jobTriggerResponseDTO.setResponseHeader(convertResponseToString(responseEntity.getHeaders()));
        jobTriggerResponseDTO.setResponseBody(responseEntity.getBody());
        jobTriggerResponseDTO.setJobKey(jobExecutionContext.getJobDetail().getKey().getName());
        jobTriggerResponseDTO.setJobGroup(new UserDTO(valueOf(jobExecutionContext.getJobDetail().getKey().getGroup())));
        jobTriggerResponseDTO.setTriggerKey(jobExecutionContext.getTrigger().getKey().getName());
        jobTriggerResponseDTO.setTriggerGroup(new UserDTO(valueOf(jobExecutionContext.getTrigger().getKey().getGroup())));
        jobTriggerResponseDTO.setFireTime(jobExecutionContext.getTrigger().getPreviousFireTime());
        return jobTriggerResponseDTO;
    }

    Map<String, String> getHeaders(final JobDataMap jobDataMap) {
        return jobDataMap
                .keySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(it -> it.contains(REQUEST_HEADER))
                .collect(toMap((String it) -> it.split("_")[1], jobDataMap::getString, (String a, String b) -> b));
    }

    String getRequestUrl(final JobDataMap jobDataMap) {
        return jobDataMap.getString(REQUEST_URL);
    }

    JobTriggerResponse constructAndSaveJobTriggerResponse(
            final JobTriggerResponseRepoService jobTriggerResponseRepoService,
            final RestTemplate restTemplate,
            @NotBlank @NotEmpty final String url,
            final HttpMethod httpMethod,
            final HttpHeaders httpHeaders,
            final JobExecutionContext jobExecutionContext) {
        return jobTriggerResponseRepoService.save(
                constructJobTriggerResponseDTO(
                        restTemplate.exchange(
                                url,
                                httpMethod,
                                new HttpEntity<String>(httpHeaders),
                                Object.class
                        ),
                        jobExecutionContext
                )
        );
    }
}
