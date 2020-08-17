package wrapper.quartz.scheduler.util.quartz.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import wrapper.quartz.scheduler.constants.ApplicationConstants;
import wrapper.quartz.scheduler.dto.quartz.JobTriggerResponseDTO;
import wrapper.quartz.scheduler.dto.quartz.KeyGroupDescriptionDTO;
import wrapper.quartz.scheduler.util.LoggerUtility;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Request utility.
 */
@Getter
@Setter
@Slf4j
abstract class RequestUtility implements Job {
    protected RestTemplate restTemplate;
    protected HttpHeaders httpHeaders;

    private RequestUtility() {
        this.restTemplate = new RestTemplate();
        this.httpHeaders = new HttpHeaders();
    }

    /**
     * Instantiates a new Request utility.
     *
     * @param httpMethod the http method
     */
    RequestUtility(HttpMethod httpMethod) {
        this();
        this.httpHeaders.setAccessControlAllowMethods(Collections.singletonList(httpMethod));
        this.httpHeaders.setAccessControlRequestMethod(httpMethod);
    }

    private String convertResponseToString(Object response) {
        try {
            return new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            LoggerUtility.error(log, e);
            return null;
        }
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
                .filter(it -> StringUtils.isNotBlank(it) && ApplicationConstants.QuartzConstants.isValidRequestHeader(it))
                .collect(Collectors.toMap(ApplicationConstants.QuartzConstants::getHeaderKeyFromRequestHeader, jobDataMap::getString, (String a, String b) -> b));
    }

    private JobTriggerResponseDTO constructJobTriggerResponseDTO(ResponseEntity<Object> responseEntity, JobExecutionContext jobExecutionContext) {
        JobTriggerResponseDTO jobTriggerResponseDTO = new JobTriggerResponseDTO();
        jobTriggerResponseDTO.setResponseCode(responseEntity.getStatusCode());
        jobTriggerResponseDTO.setResponseHeader(convertResponseToString(responseEntity.getHeaders()));
        jobTriggerResponseDTO.setResponseBody(responseEntity.getBody());
        jobTriggerResponseDTO.setJob(new KeyGroupDescriptionDTO(jobExecutionContext.getJobDetail()));
        jobTriggerResponseDTO.setTrigger(new KeyGroupDescriptionDTO(jobExecutionContext.getTrigger()));
        jobTriggerResponseDTO.setFireTime(jobExecutionContext.getTrigger().getPreviousFireTime());
        return jobTriggerResponseDTO;
    }

    /**
     * Gets request url.
     *
     * @param jobDataMap the job data map
     * @return the request url
     * @throws ClassCastException the class cast exception
     */
    String getRequestUrl(JobDataMap jobDataMap) throws ClassCastException {
        return jobDataMap.getString(ApplicationConstants.QuartzConstants.REQUEST_URL);
    }
}
