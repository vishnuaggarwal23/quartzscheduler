package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 9/3/18 10:36 AM
*/

import com.google.gson.Gson;
import com.vishnu.aggarwal.admin.config.RestApplicationConfig;
import com.vishnu.aggarwal.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@CommonsLog
@Component
public class RestService extends BaseService {

    private final RestApplicationConfig restApplicationConfig;
    private final Gson gson;

    @Autowired
    public RestService(
            RestApplicationConfig restApplicationConfig,
            Gson gson) {
        this.restApplicationConfig = restApplicationConfig;
        this.gson = gson;
    }

    private RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public ResponseEntity<String> getResponseFromBackendService(@Nullable final Object requestObject, @Nullable final String xAuthToken, final String apiEndPoint, final HttpMethod httpMethod, @Nullable final Map<String, Object> urlQueryParams, @Nullable final Map<String, Object> urlParams) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (isNotEmpty(xAuthToken)) {
            httpHeaders.add(X_AUTH_TOKEN, xAuthToken);
        }
        httpHeaders.setContentType(APPLICATION_JSON_UTF8);

        UriComponentsBuilder uriComponentsBuilder = fromUriString(format("%s%s", restApplicationConfig.restApplicationUrl(), apiEndPoint));

        if (!isEmpty(urlQueryParams)) {
            urlQueryParams.forEach(uriComponentsBuilder::queryParam);
        }

        return restTemplate().exchange(
                (isEmpty(urlParams) ? uriComponentsBuilder.build() : uriComponentsBuilder.buildAndExpand(urlParams)).toUri(),
                httpMethod,
                new HttpEntity<Object>(isNull(requestObject) ? null : gson.toJson(requestObject), httpHeaders),
                String.class);
    }
}

