package com.vishnu.aggarwal.quartz.rest.util.quartz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.quartz.core.dto.JobTriggerResponseDTO;
import com.vishnu.aggarwal.quartz.core.dto.UserDTO;
import com.vishnu.aggarwal.quartz.rest.service.repository.jpa.JobTriggerResponseRepoService;
import com.vishnu.aggarwal.quartz.rest.entity.JobTriggerResponse;
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

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.REQUEST_HEADER;
import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.REQUEST_URL;
import static java.lang.Long.valueOf;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * The type Api request util.
 */
@CommonsLog
abstract class ApiRequestUtil implements Job {

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
     * The Job execution exception.
     */
    JobExecutionException jobExecutionException;

    /**
     * The Job trigger response service.
     */
    @Autowired
    JobTriggerResponseRepoService jobTriggerResponseRepoService;

    private ApiRequestUtil() {
        this.restTemplate = new RestTemplate();
        this.httpHeaders = new HttpHeaders();
    }

    /**
     * Instantiates a new Api request util.
     *
     * @param httpMethod the http method
     */
    ApiRequestUtil(HttpMethod httpMethod) {
        this();
        this.httpHeaders.setAccessControlAllowMethods(singletonList(httpMethod));
        this.httpHeaders.setAccessControlRequestMethod(httpMethod);
    }

    private String convertResponseToString(Object response) {
        try {
            return new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error(getStackTrace(e));
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

    /**
     * Gets headers.
     *
     * @param jobDataMap the job data map
     * @return the headers
     */
    Map<String, String> getHeaders(final JobDataMap jobDataMap) {
        return jobDataMap
                .keySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(it -> it.contains(REQUEST_HEADER))
                .collect(toMap((String it) -> it.split("_")[1], jobDataMap::getString, (String a, String b) -> b));
    }

    /**
     * Gets request url.
     *
     * @param jobDataMap the job data map
     * @return the request url
     */
    String getRequestUrl(final JobDataMap jobDataMap) {
        return jobDataMap.getString(REQUEST_URL);
    }

    /**
     * Construct and save job trigger response.
     *
     * @param jobTriggerResponseRepoService the job trigger response repo service
     * @param restTemplate                  the rest template
     * @param url                           the url
     * @param httpMethod                    the http method
     * @param httpHeaders                   the http headers
     * @param jobExecutionContext           the job execution context
     */
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
