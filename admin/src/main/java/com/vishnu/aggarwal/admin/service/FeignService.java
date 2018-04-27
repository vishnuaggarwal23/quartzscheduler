package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 19/4/18 11:38 AM
*/

import com.vishnu.aggarwal.admin.config.FeignClientFactory;
import com.vishnu.aggarwal.admin.config.RestApplicationConfig;
import com.vishnu.aggarwal.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Feign service.
 */
public class FeignService extends BaseService {

    /**
     * The Rest application config.
     */
    @Autowired
    RestApplicationConfig restApplicationConfig;

    /**
     * The Feign client factory.
     */
    @Autowired
    FeignClientFactory feignClientFactory;

    /**
     * Gets bean.
     *
     * @param <T>                    the type parameter
     * @param clazz                  the clazz
     * @param restApplicationBaseUri the rest application base uri
     * @return the bean
     */
    <T> T getBean(Class<T> clazz, String restApplicationBaseUri) {
        return feignClientFactory.getInstance(clazz, restApplicationConfig.restApplicationUrl(restApplicationBaseUri));
    }
}
