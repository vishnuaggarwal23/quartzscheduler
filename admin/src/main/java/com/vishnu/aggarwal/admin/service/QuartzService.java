
package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 9/3/18 11:06 AM
*/


import com.vishnu.aggarwal.admin.config.FeignClientFactory;
import com.vishnu.aggarwal.admin.config.RestApplicationConfig;
import com.vishnu.aggarwal.admin.service.feign.QuartzFeignApiService;
import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.constants.UrlMapping;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.Cookie;
import java.util.Map;

import static com.vishnu.aggarwal.core.enums.RestApiEndPoint.*;


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
     * The Rest application config.
     */
    private final RestApplicationConfig restApplicationConfig;

    /**
     * Instantiates a new Quartz service.
     *
     * @param restService           the rest service
     * @param restApplicationConfig the rest application config
     */
    @Autowired
    public QuartzService(
            RestService restService,
            RestApplicationConfig restApplicationConfig) {
        this.restService = restService;
        this.restApplicationConfig = restApplicationConfig;
    }


    /**
     * Quartz feign api service quartz feign api service.
     *
     * @return the quartz feign api service
     */
    @Bean
    @Primary
    QuartzFeignApiService quartzFeignApiService() {
        return new FeignClientFactory().getInstance(QuartzFeignApiService.class, restApplicationConfig.restApplicationUrl(UrlMapping.Rest.Quartz.BASE_URI));
    }


    /**
     * Create new job rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<String> createNewJob(QuartzDTO quartzDTO, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), CREATE_NEW_JOB.getApiEndPoint(), CREATE_NEW_JOB.getHttpMethod(), CREATE_NEW_JOB.getResponseTypeClass());
//        return quartzFeignApiService().createJob(quartzDTO, cookie.getValue());
    }


    /**
     * Create new trigger rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<String> createNewTrigger(QuartzDTO quartzDTO, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), CREATE_NEW_TRIGGER.getApiEndPoint(), CREATE_NEW_TRIGGER.getHttpMethod(), CREATE_NEW_TRIGGER.getResponseTypeClass());
    }


    /**
     * Update existing job rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<String> updateExistingJob(QuartzDTO quartzDTO, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), UPDATE_EXISTING_JOB.getApiEndPoint(), UPDATE_EXISTING_JOB.getHttpMethod(), UPDATE_EXISTING_JOB.getResponseTypeClass());
    }


    /**
     * Update existing trigger rest response vo.
     *
     * @param quartzDTO the quartz dto
     * @param cookie    the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<String> updateExistingTrigger(QuartzDTO quartzDTO, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(quartzDTO, cookie.getValue(), UPDATE_EXISTING_TRIGGER.getApiEndPoint(), UPDATE_EXISTING_TRIGGER.getHttpMethod(), UPDATE_EXISTING_TRIGGER.getResponseTypeClass());
    }


    /**
     * Fetch jobs by group name data table vo.
     *
     * @param groupName the group name
     * @param cookie    the cookie
     * @return the data table vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<DataTableVO<JobDetailsCO>> fetchJobsByGroupName(String groupName, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(null, cookie.getValue(), FETCH_JOBS_BY_GROUP_NAME.getApiEndPoint().replace("{groupName}", groupName), FETCH_JOBS_BY_GROUP_NAME.getHttpMethod(), FETCH_JOBS_BY_GROUP_NAME.getResponseTypeClass());
    }


    /**
     * Fetch triggers by job key name and group name data table vo.
     *
     * @param jobKeyName the job key name
     * @param groupName  the group name
     * @param cookie     the cookie
     * @return the data table vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<DataTableVO<TriggerDetailsCO>> fetchTriggersByJobKeyNameAndGroupName(String jobKeyName, String groupName, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(null, cookie.getValue(), FETCH_TRIGGERS_BY_JOB_KEY_NAME_AND_GROUP_NAME.getApiEndPoint().replace("{jobKeyName}", jobKeyName).replace("{groupName}", groupName), FETCH_TRIGGERS_BY_JOB_KEY_NAME_AND_GROUP_NAME.getHttpMethod(), FETCH_TRIGGERS_BY_JOB_KEY_NAME_AND_GROUP_NAME.getResponseTypeClass());
    }


    /**
     * Fetch triggers by job key name and group name data table vo.
     *
     * @param groupName the group name
     * @param cookie    the cookie
     * @return the data table vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<DataTableVO<QuartzDetailsCO>> fetchQuartzDetailsForGroupName(String groupName, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(null, cookie.getValue(), FETCH_QUARTZ_DETAILS_BY_GROUP_NAME.getApiEndPoint().replace("{groupName}", groupName), FETCH_QUARTZ_DETAILS_BY_GROUP_NAME.getHttpMethod(), FETCH_QUARTZ_DETAILS_BY_GROUP_NAME.getResponseTypeClass());
    }


    /**
     * Resume jobs rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<Map> resumeJobs(KeyGroupDescriptionDTO keyGroupDescriptionDTO, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), RESUME_JOBS.getApiEndPoint(), RESUME_JOBS.getHttpMethod(), RESUME_JOBS.getResponseTypeClass());
    }


    /**
     * Pause jobs rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<Map> pauseJobs(KeyGroupDescriptionDTO keyGroupDescriptionDTO, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), PAUSE_JOBS.getApiEndPoint(), PAUSE_JOBS.getHttpMethod(), PAUSE_JOBS.getResponseTypeClass());
    }


    /**
     * Resume triggers rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<Map> resumeTriggers(KeyGroupDescriptionDTO keyGroupDescriptionDTO, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), RESUME_TRIGGERS.getApiEndPoint(), RESUME_TRIGGERS.getHttpMethod(), RESUME_TRIGGERS.getResponseTypeClass());
    }


    /**
     * Pause triggers rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<Map> pauseTriggers(KeyGroupDescriptionDTO keyGroupDescriptionDTO, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), PAUSE_TRIGGERS.getApiEndPoint(), PAUSE_TRIGGERS.getHttpMethod(), PAUSE_TRIGGERS.getResponseTypeClass());
    }


    /**
     * Delete jobs rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<Map> deleteJobs(KeyGroupDescriptionDTO keyGroupDescriptionDTO, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), DELETE_JOBS.getApiEndPoint(), DELETE_JOBS.getHttpMethod(), DELETE_JOBS.getResponseTypeClass());
    }


    /**
     * Delete triggers rest response vo.
     *
     * @param keyGroupDescriptionDTO the key group name dto
     * @param cookie          the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<Map> deleteTriggers(KeyGroupDescriptionDTO keyGroupDescriptionDTO, Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(keyGroupDescriptionDTO, cookie.getValue(), DELETE_TRIGGERS.getApiEndPoint(), DELETE_TRIGGERS.getHttpMethod(), DELETE_TRIGGERS.getResponseTypeClass());
    }
}
