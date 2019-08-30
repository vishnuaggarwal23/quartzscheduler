package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 19/4/18 11:38 AM
*/

import com.vishnu.aggarwal.admin.config.FeignClientFactory;
import com.vishnu.aggarwal.admin.config.RestApplicationConfig;
import com.vishnu.aggarwal.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

public class FeignService extends BaseService {

    @Autowired
    RestApplicationConfig restApplicationConfig;

    @Autowired
    FeignClientFactory feignClientFactory;

    <T> T getBean(Class<T> clazz, String restApplicationBaseUri) {
        return feignClientFactory.getInstance(clazz, restApplicationConfig.restApplicationUrl(restApplicationBaseUri));
    }
}
