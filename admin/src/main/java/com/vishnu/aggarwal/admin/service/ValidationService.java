package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 18/4/18 10:44 AM
*/

import com.vishnu.aggarwal.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.KEY_NAME;
import static com.vishnu.aggarwal.core.enums.RestApiEndPoint.UNIQUE_JOB_KEY_PER_GROUP;
import static com.vishnu.aggarwal.core.enums.RestApiEndPoint.UNIQUE_TRIGGER_KEY_PER_GROUP;

@Service
@CommonsLog
public class ValidationService extends BaseService {

    private final RestService restService;

    @Autowired
    public ValidationService(RestService restService) {
        this.restService = restService;
    }

    public ResponseEntity<String> isJobKeyUnique(final String keyName, final Cookie xAuthToken) {
        return isJobKeyUnique(keyName, xAuthToken.getValue());
    }

    public ResponseEntity<String> isTriggerKeyUnique(final String keyName, final Cookie xAuthToken) {
        return isTriggerKeyUnique(keyName, xAuthToken.getValue());
    }

    public ResponseEntity<String> isJobKeyUnique(String keyName, String xAuthToken) {
        Map<String, Object> urlQueryParams = new HashMap<String, Object>();
        urlQueryParams.put(KEY_NAME, keyName);
        return restService.getResponseFromBackendService(null, xAuthToken, UNIQUE_JOB_KEY_PER_GROUP.getApiEndPoint(), UNIQUE_JOB_KEY_PER_GROUP.getHttpMethod(), urlQueryParams, null);
    }

    public ResponseEntity<String> isTriggerKeyUnique(String keyName, String xAuthToken) {
        Map<String, Object> urlQueryParams = new HashMap<String, Object>();
        urlQueryParams.put(KEY_NAME, keyName);
        return restService.getResponseFromBackendService(null, xAuthToken, UNIQUE_TRIGGER_KEY_PER_GROUP.getApiEndPoint(), UNIQUE_TRIGGER_KEY_PER_GROUP.getHttpMethod(), urlQueryParams, null);
    }
}
