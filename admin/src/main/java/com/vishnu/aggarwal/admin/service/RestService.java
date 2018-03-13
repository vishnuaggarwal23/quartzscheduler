package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 9/3/18 10:36 AM
*/

import com.google.gson.Gson;
import com.vishnu.aggarwal.admin.config.RestApplicationConfig;
import com.vishnu.aggarwal.core.exceptions.RestServiceCallException;
import com.vishnu.aggarwal.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * The type Rest service.
 */
@CommonsLog
@Component
public class RestService extends BaseService {

    /**
     * The Rest application config.
     */
    @Autowired
    RestApplicationConfig restApplicationConfig;

    /**
     * Gets response from backend service.
     *
     * @param requestObject     the request object
     * @param xAuthToken        the x auth token
     * @param apiEndPoint       the api end point
     * @param httpMethod        the http method
     * @param responseTypeClass the response type class
     * @return the response from backend service
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public Object getResponseFromBackendService(Object requestObject, String xAuthToken, String apiEndPoint, HttpMethod httpMethod, Class<?> responseTypeClass) throws RestServiceCallException, Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<?> responseEntity = null;
        httpHeaders.add("x-auth-token", xAuthToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Gson gson = new Gson();
        HttpEntity<?> httpEntity = new HttpEntity<Object>(isNull(requestObject) ? null : gson.toJson(requestObject), httpHeaders);

        String apiUrl = format("%s%s", restApplicationConfig.restApplicationUrl(), apiEndPoint);
        try {
            responseEntity = restTemplate.exchange(apiUrl, httpMethod, httpEntity, responseTypeClass);
            if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
                throw new RestServiceCallException(formatMessage(getMessage("error.while.accessing.rest.service.call"), apiUrl));
            }
            return responseEntity.getBody();
        } catch (RestServiceCallException e) {
            log.error("**************** Error in " + httpMethod + " for " + apiUrl);
            log.error("Response Body \n " + responseEntity.getBody());
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error("**************** Error in " + httpMethod + " for " + apiUrl);
            log.error("Response Body \n " + (nonNull(responseEntity) ? responseEntity.getBody() : null));
            log.error(e);
            throw e;
        }
    }
}

