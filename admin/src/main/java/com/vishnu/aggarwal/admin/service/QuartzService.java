package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 9/3/18 11:06 AM
*/


import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.enums.RestApiEndPoint.*;


@Service
@CommonsLog
public class QuartzService extends BaseService {

    private final RestService restService;

    @Autowired
    public QuartzService(RestService restService) {
        this.restService = restService;
    }

    public ResponseEntity<String> createJob(final QuartzDTO quartzDTO, final Cookie xAuthToken) {
        return createJob(quartzDTO, xAuthToken.getValue());
    }

    public ResponseEntity<String> createJob(final QuartzDTO quartzDTO, final String xAuthToken) {
        return restService.getResponseFromBackendService(quartzDTO, xAuthToken, CREATE_JOB.getApiEndPoint(), CREATE_JOB.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> updateJob(final QuartzDTO quartzDTO, final Cookie xAuthToken) {
        return updateJob(quartzDTO, xAuthToken.getValue());
    }

    public ResponseEntity<String> updateJob(final QuartzDTO quartzDTO, final String xAuthToken) {
        return restService.getResponseFromBackendService(quartzDTO, xAuthToken, UPDATE_JOB.getApiEndPoint(), UPDATE_JOB.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> deleteJob(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final Cookie xAuthToken) {
        return deleteJob(keyGroupDescriptionDTO, xAuthToken.getValue());
    }

    public ResponseEntity<String> deleteJob(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final String xAuthToken) {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, xAuthToken, DELETE_JOB.getApiEndPoint(), DELETE_JOB.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> deleteJobs(final Cookie xAuthToken) {
        return deleteJobs(xAuthToken.getValue());
    }

    public ResponseEntity<String> deleteJobs(final String xAuthToken) {
        return restService.getResponseFromBackendService(null, xAuthToken, DELETE_JOBS.getApiEndPoint(), DELETE_JOBS.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> resumeJob(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final Cookie xAuthToken) {
        return resumeJob(keyGroupDescriptionDTO, xAuthToken.getValue());
    }

    public ResponseEntity<String> resumeJob(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final String xAuthToken) {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, xAuthToken, RESUME_JOB.getApiEndPoint(), RESUME_JOB.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> resumeJobs(final Cookie xAuthToken) {
        return resumeJobs(xAuthToken.getValue());
    }

    public ResponseEntity<String> resumeJobs(final String xAuthToken) {
        return restService.getResponseFromBackendService(null, xAuthToken, RESUME_JOBS.getApiEndPoint(), RESUME_JOBS.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> pauseJob(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final Cookie xAuthToken) {
        return pauseJob(keyGroupDescriptionDTO, xAuthToken.getValue());
    }

    public ResponseEntity<String> pauseJob(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final String xAuthToken) {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, xAuthToken, PAUSE_JOB.getApiEndPoint(), PAUSE_JOB.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> pauseJobs(final Cookie xAuthToken) {
        return pauseJobs(xAuthToken.getValue());
    }

    public ResponseEntity<String> pauseJobs(final String xAuthToken) {
        return restService.getResponseFromBackendService(null, xAuthToken, PAUSE_JOBS.getApiEndPoint(), PAUSE_JOBS.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> showJob(final String jobKey, final Cookie xAuthToken) {
        return showJob(jobKey, xAuthToken.getValue());
    }

    public ResponseEntity<String> showJob(final String jobKey, final String xAuthToken) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(JOB_KEY, jobKey);
        return restService.getResponseFromBackendService(null, xAuthToken, SHOW_JOB.getApiEndPoint(), SHOW_JOB.getHttpMethod(), params, params);
    }

    public ResponseEntity<String> listJobs(final Cookie xAuthToken) {
        return listJobs(xAuthToken.getValue());
    }

    public ResponseEntity<String> listJobs(final String xAuthToken) {
        return restService.getResponseFromBackendService(null, xAuthToken, LIST_JOBS.getApiEndPoint(), LIST_JOBS.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> createTrigger(final QuartzDTO quartzDTO, final Cookie xAuthToken) {
        return createTrigger(quartzDTO, xAuthToken.getValue());
    }

    public ResponseEntity<String> createTrigger(final QuartzDTO quartzDTO, final String xAuthToken) {
        return restService.getResponseFromBackendService(quartzDTO, xAuthToken, CREATE_TRIGGER.getApiEndPoint(), CREATE_TRIGGER.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> updateTrigger(final QuartzDTO quartzDTO, final Cookie xAuthToken) {
        return updateTrigger(quartzDTO, xAuthToken.getValue());
    }

    public ResponseEntity<String> updateTrigger(final QuartzDTO quartzDTO, final String xAuthToken) {
        return restService.getResponseFromBackendService(quartzDTO, xAuthToken, UPDATE_TRIGGER.getApiEndPoint(), UPDATE_TRIGGER.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> deleteTrigger(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final Cookie xAuthToken) {
        return deleteTrigger(keyGroupDescriptionDTO, xAuthToken.getValue());
    }

    public ResponseEntity<String> deleteTrigger(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final String xAuthToken) {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, xAuthToken, DELETE_TRIGGER.getApiEndPoint(), DELETE_TRIGGER.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> deleteTriggers(final Cookie xAuthToken) {
        return deleteTriggers(xAuthToken.getValue());
    }

    public ResponseEntity<String> deleteTriggers(final String xAuthToken) {
        return restService.getResponseFromBackendService(null, xAuthToken, DELETE_TRIGGERS.getApiEndPoint(), DELETE_TRIGGERS.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> resumeTrigger(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final Cookie xAuthToken) {
        return resumeTrigger(keyGroupDescriptionDTO, xAuthToken.getValue());
    }

    public ResponseEntity<String> resumeTrigger(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final String xAuthToken) {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, xAuthToken, RESUME_TRIGGER.getApiEndPoint(), RESUME_TRIGGER.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> resumeTriggers(final Cookie xAuthToken) {
        return resumeTriggers(xAuthToken.getValue());
    }

    public ResponseEntity<String> resumeTriggers(final String xAuthToken) {
        return restService.getResponseFromBackendService(null, xAuthToken, RESUME_TRIGGERS.getApiEndPoint(), RESUME_TRIGGERS.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> pauseTrigger(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final Cookie xAuthToken) {
        return pauseTrigger(keyGroupDescriptionDTO, xAuthToken.getValue());
    }

    public ResponseEntity<String> pauseTrigger(final KeyGroupDescriptionDTO keyGroupDescriptionDTO, final String xAuthToken) {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, xAuthToken, PAUSE_TRIGGER.getApiEndPoint(), PAUSE_TRIGGER.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> pauseTriggers(final Cookie xAuthToken) {
        return pauseTriggers(xAuthToken.getValue());
    }

    public ResponseEntity<String> pauseTriggers(final String xAuthToken) {
        return restService.getResponseFromBackendService(null, xAuthToken, PAUSE_TRIGGERS.getApiEndPoint(), PAUSE_TRIGGERS.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> showTrigger(final String triggerKey, final Cookie xAuthToken) {
        return showTrigger(triggerKey, xAuthToken.getValue());
    }

    public ResponseEntity<String> showTrigger(final String triggerKey, final String xAuthToken) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(TRIGGER_KEY, triggerKey);
        return restService.getResponseFromBackendService(null, xAuthToken, SHOW_TRIGGER.getApiEndPoint(), SHOW_TRIGGER.getHttpMethod(), params, params);
    }

    public ResponseEntity<String> listTriggers(final String jobKey, final Cookie xAuthToken) {
        return listTriggers(jobKey, xAuthToken.getValue());
    }

    public ResponseEntity<String> listTriggers(final String jobKey, final String xAuthToken) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(JOB_KEY, jobKey);
        return restService.getResponseFromBackendService(null, xAuthToken, LIST_TRIGGERS.getApiEndPoint(), LIST_TRIGGERS.getHttpMethod(), params, params);
    }

    public ResponseEntity<String> listQuartzDetails(final Cookie xAuthToken) {
        return listQuartzDetails(xAuthToken.getValue());
    }

    public ResponseEntity<String> listQuartzDetails(final String xAuthToken) {
        return restService.getResponseFromBackendService(null, xAuthToken, LIST_QUARTZ_DETAILS.getApiEndPoint(), LIST_QUARTZ_DETAILS.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> jobKeysAutocomplete(String searchText, Cookie xAuthToken) {
        return jobKeysAutocomplete(searchText, xAuthToken.getValue());
    }

    public ResponseEntity<String> jobKeysAutocomplete(String searchText, String xAuthToken) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SEARCH_TEXT, searchText);
        return restService.getResponseFromBackendService(null, xAuthToken, JOB_KEYS_AUTOCOMPLETE.getApiEndPoint(), JOB_KEYS_AUTOCOMPLETE.getHttpMethod(), params, params);
    }
}
