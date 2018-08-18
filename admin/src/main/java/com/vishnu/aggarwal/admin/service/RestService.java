package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 9/3/18 10:36 AM
*/

import com.google.gson.Gson;
import com.vishnu.aggarwal.admin.config.RestApplicationConfig;
import com.vishnu.aggarwal.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * The type Rest service.
 */
@CommonsLog
@Component
public class RestService extends BaseService {

    /**
     * The Rest application config.
     */
    private final RestApplicationConfig restApplicationConfig;
    private final Gson gson;

    /**
     * Instantiates a new Rest service.
     *
     * @param restApplicationConfig the rest application config
     * @param gson                  the gson
     */
    @Autowired
    public RestService(
            RestApplicationConfig restApplicationConfig,
            Gson gson) {
        this.restApplicationConfig = restApplicationConfig;
        this.gson = gson;
    }


    @Autowired
    private RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /**
     * Gets response from backend service.
     *
     * @param <T>               the type parameter
     * @param requestObject     the request object
     * @param xAuthToken        the x auth token
     * @param apiEndPoint       the api end point
     * @param httpMethod        the http method
     * @param responseTypeClass the response type class
     * @return the response from backend service
     * @throws RestClientException the rest client exception
     */
    public <T> ResponseEntity<T> getResponseFromBackendService(Object requestObject, String xAuthToken, String apiEndPoint, HttpMethod httpMethod, Class<T> responseTypeClass) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (isNotEmpty(xAuthToken)) {
            httpHeaders.add(X_AUTH_TOKEN, xAuthToken);
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return restTemplate().exchange(
                format("%s%s", restApplicationConfig.restApplicationUrl(), apiEndPoint),
                httpMethod,
                new HttpEntity<Object>(isNull(requestObject) ? null : gson.toJson(requestObject), httpHeaders),
                responseTypeClass);
    }
}

