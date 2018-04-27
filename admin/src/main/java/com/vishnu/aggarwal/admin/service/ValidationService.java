package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 18/4/18 10:44 AM
*/

import com.vishnu.aggarwal.admin.config.FeignClientFactory;
import com.vishnu.aggarwal.admin.config.RestApplicationConfig;
import com.vishnu.aggarwal.admin.service.feign.ValidationApiService;
import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Validation.BASE_URI;

/**
 * The type Validation service.
 */
@Service
@CommonsLog
public class ValidationService extends BaseService {

    /**
     * The Rest application config.
     */
    @Autowired
    RestApplicationConfig restApplicationConfig;

    /**
     * Validation api service validation api service.
     *
     * @return the validation api service
     */
    @Bean
    @Primary
    ValidationApiService validationApiService() {
        return new FeignClientFactory().getInstance(ValidationApiService.class, restApplicationConfig.restApplicationUrl(BASE_URI));
    }

    /**
     * Is job key unique rest response vo.
     *
     * @param keyName    the key name
     * @param xAuthToken the x auth token
     * @return the rest response vo
     */
    public RestResponseVO<Boolean> isJobKeyUnique(String keyName, Cookie xAuthToken) {
        return validationApiService().isJobKeyUnique(keyName, xAuthToken.getValue());
    }

    /**
     * Is trigger key unique rest response vo.
     *
     * @param keyName    the key name
     * @param xAuthToken the x auth token
     * @return the rest response vo
     */
    public RestResponseVO<Boolean> isTriggerKeyUnique(String keyName, Cookie xAuthToken) {
        return validationApiService().isTriggerKeyUnique(keyName, xAuthToken.getValue());
    }
}
