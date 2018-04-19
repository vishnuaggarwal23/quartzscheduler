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

@Service
@CommonsLog
public class ValidationService extends BaseService {

    @Autowired
    RestApplicationConfig restApplicationConfig;

    @Bean
    @Primary
    ValidationApiService validationApiService() {
        return new FeignClientFactory().getInstance(ValidationApiService.class, restApplicationConfig.restApplicationUrl(BASE_URI));
    }

    public RestResponseVO<Boolean> isJobKeyUnique(String keyName, Cookie xAuthToken) {
        return validationApiService().isJobKeyUnique(keyName, xAuthToken.getValue());
    }

    public RestResponseVO<Boolean> isTriggerKeyUnique(String keyName, Cookie xAuthToken) {
        return validationApiService().isTriggerKeyUnique(keyName, xAuthToken.getValue());
    }
}
