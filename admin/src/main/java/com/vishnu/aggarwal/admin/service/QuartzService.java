package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 9/3/18 11:06 AM
*/

import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.dto.KeyGroupNameDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.exceptions.RestServiceCallException;
import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.List;

import static com.vishnu.aggarwal.core.enums.RestApiEndPoint.*;
import static org.springframework.http.HttpMethod.PUT;

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
    @Autowired
    RestService restService;

    /**
     * Create new job rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<String> createNewJob(QuartzDTO quartzDTO, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<String>) restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), CREATE_NEW_JOB.getApiEndPoint(), (HttpMethod) CREATE_NEW_JOB.getHttpMethods(), CREATE_NEW_JOB.getResponseTypeClass());
    }

    /**
     * Create new trigger rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<String> createNewTrigger(QuartzDTO quartzDTO, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<String>) restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), CREATE_NEW_TRIGGER.getApiEndPoint(), (HttpMethod) CREATE_NEW_TRIGGER.getHttpMethods(), CREATE_NEW_TRIGGER.getResponseTypeClass());
    }

    /**
     * Update existing job rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<String> updateExistingJob(QuartzDTO quartzDTO, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<String>) restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), UPDATE_EXISTING_JOB.getApiEndPoint(), ((List<HttpMethod>) UPDATE_EXISTING_JOB.getHttpMethods()).stream().findAny().orElse(PUT), UPDATE_EXISTING_JOB.getResponseTypeClass());
    }

    /**
     * Update existing trigger rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<String> updateExistingTrigger(QuartzDTO quartzDTO, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<String>) restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), UPDATE_EXISTING_TRIGGER.getApiEndPoint(), ((List<HttpMethod>) UPDATE_EXISTING_TRIGGER.getHttpMethods()).stream().findAny().orElse(PUT), UPDATE_EXISTING_TRIGGER.getResponseTypeClass());
    }

    /**
     * Fetch jobs by group name data table vo.
     *
     * @param groupName the group name
     * @param cookie    the cookie
     * @return the data table vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<DataTableVO<JobDetailsCO>> fetchJobsByGroupName(String groupName, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<DataTableVO<JobDetailsCO>>) restService.getResponseFromBackendService(null, cookie.getValue(), FETCH_JOBS_BY_GROUP_NAME.getApiEndPoint().replace("{groupName}", groupName), (HttpMethod) FETCH_JOBS_BY_GROUP_NAME.getHttpMethods(), FETCH_JOBS_BY_GROUP_NAME.getResponseTypeClass());
    }

    /**
     * Fetch triggers by job key name and group name data table vo.
     *
     * @param jobKeyName the job key name
     * @param groupName  the group name
     * @param cookie     the cookie
     * @return the data table vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<DataTableVO<TriggerDetailsCO>> fetchTriggersByJobKeyNameAndGroupName(String jobKeyName, String groupName, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<DataTableVO<TriggerDetailsCO>>) restService.getResponseFromBackendService(null, cookie.getValue(), FETCH_TRIGGERS_BY_JOB_KEY_NAME_AND_GROUP_NAME.getApiEndPoint().replace("{jobKeyName}", jobKeyName).replace("{groupName}", groupName), (HttpMethod) FETCH_TRIGGERS_BY_JOB_KEY_NAME_AND_GROUP_NAME.getHttpMethods(), FETCH_TRIGGERS_BY_JOB_KEY_NAME_AND_GROUP_NAME.getResponseTypeClass());
    }

    /**
     * Fetch triggers by job key name and group name data table vo.
     *
     * @param groupName the group name
     * @param cookie    the cookie
     * @return the data table vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<DataTableVO<QuartzDetailsCO>> fetchQuartzDetailsForGroupName(String groupName, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<DataTableVO<QuartzDetailsCO>>) restService.getResponseFromBackendService(null, cookie.getValue(), FETCH_QUARTZ_DETAILS_BY_GROUP_NAME.getApiEndPoint().replace("{groupName}", groupName), (HttpMethod) FETCH_QUARTZ_DETAILS_BY_GROUP_NAME.getHttpMethods(), FETCH_QUARTZ_DETAILS_BY_GROUP_NAME.getResponseTypeClass());
    }

    /**
     * Resume jobs rest response vo.
     *
     * @param keyGroupNameDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<Boolean> resumeJobs(KeyGroupNameDTO keyGroupNameDTO, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<Boolean>) restService.getResponseFromBackendService(keyGroupNameDTO, cookie.getValue(), RESUME_JOBS.getApiEndPoint(), ((List<HttpMethod>) RESUME_JOBS.getHttpMethods()).stream().findAny().orElse(PUT), RESUME_JOBS.getResponseTypeClass());
    }

    /**
     * Pause jobs rest response vo.
     *
     * @param keyGroupNameDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<Boolean> pauseJobs(KeyGroupNameDTO keyGroupNameDTO, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<Boolean>) restService.getResponseFromBackendService(keyGroupNameDTO, cookie.getValue(), PAUSE_JOBS.getApiEndPoint(), ((List<HttpMethod>) PAUSE_JOBS.getHttpMethods()).stream().findAny().orElse(PUT), PAUSE_JOBS.getResponseTypeClass());
    }

    /**
     * Resume triggers rest response vo.
     *
     * @param keyGroupNameDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<Boolean> resumeTriggers(KeyGroupNameDTO keyGroupNameDTO, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<Boolean>) restService.getResponseFromBackendService(keyGroupNameDTO, cookie.getValue(), RESUME_TRIGGERS.getApiEndPoint(), ((List<HttpMethod>) RESUME_TRIGGERS.getHttpMethods()).stream().findAny().orElse(PUT), RESUME_TRIGGERS.getResponseTypeClass());
    }

    /**
     * Pause triggers rest response vo.
     *
     * @param keyGroupNameDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<Boolean> pauseTriggers(KeyGroupNameDTO keyGroupNameDTO, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<Boolean>) restService.getResponseFromBackendService(keyGroupNameDTO, cookie.getValue(), PAUSE_TRIGGERS.getApiEndPoint(), ((List<HttpMethod>) PAUSE_TRIGGERS.getHttpMethods()).stream().findAny().orElse(PUT), PAUSE_TRIGGERS.getResponseTypeClass());
    }

    /**
     * Delete jobs rest response vo.
     *
     * @param keyGroupNameDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<Boolean> deleteJobs(KeyGroupNameDTO keyGroupNameDTO, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<Boolean>) restService.getResponseFromBackendService(keyGroupNameDTO, cookie.getValue(), DELETE_JOBS.getApiEndPoint(), (HttpMethod) DELETE_JOBS.getHttpMethods(), DELETE_JOBS.getResponseTypeClass());
    }

    /**
     * Delete triggers rest response vo.
     *
     * @param keyGroupNameDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<Boolean> deleteTriggers(KeyGroupNameDTO keyGroupNameDTO, Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<Boolean>) restService.getResponseFromBackendService(keyGroupNameDTO, cookie.getValue(), DELETE_TRIGGERS.getApiEndPoint(), (HttpMethod) DELETE_TRIGGERS.getHttpMethods(), DELETE_TRIGGERS.getResponseTypeClass());
    }
}
