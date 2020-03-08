package com.vishnu.aggarwal.quartz.admin.thymeleaf.service;

/*
Created by vishnu on 9/3/18 11:06 AM
*/


import com.vishnu.aggarwal.quartz.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.quartz.core.dto.QuartzDTO;
import com.vishnu.aggarwal.quartz.core.service.BaseService;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.Cookie;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.JOB_KEY;
import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.SEARCH_TEXT;
import static com.vishnu.aggarwal.quartz.core.enums.RestApiEndPoint.*;
import static java.util.Collections.singletonMap;


/**
 * The type Quartz service.
 */
@Service
@CommonsLog
@SuppressWarnings("unchecked")
public class QuartzService extends BaseService {


    /**
     * The Rest service.
     */
    private final RestService restService;


    /**
     * Instantiates a new Quartz service.
     *
     * @param restService the rest service
     */
    @Autowired
    public QuartzService(@NonNull final RestService restService) {
        this.restService = restService;
    }


    /**
     * Create new job rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> createUnscheduledApiJob(@NonNull final QuartzDTO quartzDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), CREATE_API_JOB.getApiEndPoint(), CREATE_API_JOB.getHttpMethod(), null, null);
    }

    /**
     * Create scheduled api simple triggered job response entity.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the response entity
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> createScheduledApiSimpleTriggeredJob(@NonNull final QuartzDTO quartzDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), CREATE_API_JOB_SCHEDULED_SIMPLE.getApiEndPoint(), CREATE_API_JOB_SCHEDULED_SIMPLE.getHttpMethod(), null, null);
    }

    /**
     * Create scheduled api cron triggered job response entity.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the response entity
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> createScheduledApiCronTriggeredJob(@NonNull final QuartzDTO quartzDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), CREATE_API_JOB_SCHEDULED_CRON.getApiEndPoint(), CREATE_API_JOB_SCHEDULED_CRON.getHttpMethod(), null, null);
    }

    /**
     * Update existing job rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> updateExistingJob(@NonNull final QuartzDTO quartzDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), UPDATE_API_JOB.getApiEndPoint(), UPDATE_API_JOB.getHttpMethod(), null, null);
    }

    /**
     * Create new trigger rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> createNewSimpleTriggerForJob(@NonNull final QuartzDTO quartzDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), CREATE_SIMPLE_TRIGGER.getApiEndPoint(), CREATE_SIMPLE_TRIGGER.getHttpMethod(), null, null);
    }

    /**
     * Create new cron trigger for job response entity.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the response entity
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> createNewCronTriggerForJob(@NonNull final QuartzDTO quartzDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), CREATE_CRON_TRIGGER.getApiEndPoint(), CREATE_CRON_TRIGGER.getHttpMethod(), null, null);
    }

    /**
     * Update existing trigger rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> updateExistingSimpleTrigger(@NonNull final QuartzDTO quartzDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), UPDATE_SIMPLE_TRIGGER.getApiEndPoint(), UPDATE_SIMPLE_TRIGGER.getHttpMethod(), null, null);
    }

    /**
     * Update existing cron trigger response entity.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the response entity
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> updateExistingCronTrigger(@NonNull final QuartzDTO quartzDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), UPDATE_CRON_TRIGGER.getApiEndPoint(), UPDATE_CRON_TRIGGER.getHttpMethod(), null, null);
    }

    /**
     * Fetch jobs by group name data table vo.
     *
     * @param cookie the cookie
     * @return the data table vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> fetchJobsByJobGroupName(@NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(null, cookie.getValue(), FETCH_JOBS_OF_CURRENT_USER_GROUP.getApiEndPoint(), FETCH_JOBS_OF_CURRENT_USER_GROUP.getHttpMethod(), null, null);
    }


    /**
     * Fetch triggers by job key name and group name data table vo.
     *
     * @param jobKeyName the key group description dto
     * @param cookie     the cookie
     * @return the data table vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> fetchTriggersByJobKeyNameAndJobGroupName(final String jobKeyName, @NonNull final Cookie cookie) throws RestClientException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(JOB_KEY, jobKeyName);
        return restService.getResponseFromBackendService(null, cookie.getValue(), FETCH_TRIGGERS_BY_JOB_KEY_AND_CURRENT_USER_GROUP.getApiEndPoint(), FETCH_TRIGGERS_BY_JOB_KEY_AND_CURRENT_USER_GROUP.getHttpMethod(), params, params);
    }


    /**
     * Fetch triggers by job key name and group name data table vo.
     *
     * @param cookie the cookie
     * @return the data table vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> fetchQuartzDetailsForJobGroupName(@NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(null, cookie.getValue(), FETCH_QUARTZ_DETAILS_BY_CURRENT_USER_GROUP.getApiEndPoint(), FETCH_QUARTZ_DETAILS_BY_CURRENT_USER_GROUP.getHttpMethod(), null, null);
    }


    /**
     * Resume jobs rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie                 the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> resumeJobs(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), RESUME_JOBS.getApiEndPoint(), RESUME_JOBS.getHttpMethod(), null, null);
    }


    /**
     * Pause jobs rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie                 the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> pauseJobs(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), PAUSE_JOBS.getApiEndPoint(), PAUSE_JOBS.getHttpMethod(), null, null);
    }


    /**
     * Resume triggers rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie                 the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> resumeTriggers(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), RESUME_TRIGGERS.getApiEndPoint(), RESUME_TRIGGERS.getHttpMethod(), null, null);
    }


    /**
     * Pause triggers rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie                 the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> pauseTriggers(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), PAUSE_TRIGGERS.getApiEndPoint(), PAUSE_TRIGGERS.getHttpMethod(), null, null);
    }


    /**
     * Delete jobs rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie                 the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> deleteJobs(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), DELETE_JOBS.getApiEndPoint(), DELETE_JOBS.getHttpMethod(), null, null);
    }


    /**
     * Delete triggers rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie                 the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    final public ResponseEntity<String> deleteTriggers(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @NonNull final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), DELETE_TRIGGERS.getApiEndPoint(), DELETE_TRIGGERS.getHttpMethod(), null, null);
    }

    final public ResponseEntity<String> jobKeysAutocomplete(@NonNull @NotEmpty @NotBlank final String searchText, @NonNull final Cookie cookie) {
        Map<String, Object> params = singletonMap(SEARCH_TEXT, searchText);
        return restService.getResponseFromBackendService(null, cookie.getValue(), JOB_KEYS_AUTOCOMPLETE.getApiEndPoint(), JOB_KEYS_AUTOCOMPLETE.getHttpMethod(), params, params);
    }
}
