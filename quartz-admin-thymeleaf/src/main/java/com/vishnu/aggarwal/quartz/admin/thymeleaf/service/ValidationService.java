package com.vishnu.aggarwal.quartz.admin.thymeleaf.service;

/*
Created by vishnu on 18/4/18 10:44 AM
*/

import com.vishnu.aggarwal.quartz.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.KEY_NAME;
import static com.vishnu.aggarwal.quartz.core.enums.RestApiEndPoint.UNIQUE_JOB_KEY_PER_GROUP;
import static com.vishnu.aggarwal.quartz.core.enums.RestApiEndPoint.UNIQUE_TRIGGER_KEY_PER_GROUP;

/**
 * The type Validation service.
 */
@Service
@CommonsLog
public class ValidationService extends BaseService {

    private final RestService restService;

    /**
     * Instantiates a new Validation service.
     *
     * @param restService the rest service
     */
    @Autowired
    public ValidationService(RestService restService) {
        this.restService = restService;
    }

    /**
     * Is job key unique rest response vo.
     *
     * @param keyName    the key name
     * @param xAuthToken the x auth token
     * @return the rest response vo
     */
    public ResponseEntity<String> isJobKeyUnique(String keyName, Cookie xAuthToken) {
        Map<String, Object> urlQueryParams = new HashMap<String, Object>();
        urlQueryParams.put(KEY_NAME, keyName);
        return restService.getResponseFromBackendService(null, xAuthToken.getValue(), UNIQUE_JOB_KEY_PER_GROUP.getApiEndPoint(), UNIQUE_JOB_KEY_PER_GROUP.getHttpMethod(), urlQueryParams, null);
    }

    /**
     * Is trigger key unique rest response vo.
     *
     * @param keyName    the key name
     * @param xAuthToken the x auth token
     * @return the rest response vo
     */
    public ResponseEntity<String> isTriggerKeyUnique(String keyName, Cookie xAuthToken) {
        Map<String, Object> urlQueryParams = new HashMap<String, Object>();
        urlQueryParams.put(KEY_NAME, keyName);
        return restService.getResponseFromBackendService(null, xAuthToken.getValue(), UNIQUE_TRIGGER_KEY_PER_GROUP.getApiEndPoint(), UNIQUE_TRIGGER_KEY_PER_GROUP.getHttpMethod(), urlQueryParams, null);
    }
}
