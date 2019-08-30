package com.vishnu.aggarwal.admin.service.feign;

/*
Created by vishnu on 18/4/18 10:45 AM
*/

import com.vishnu.aggarwal.core.vo.RestResponseVO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Headers(value = {
        "Content-Type: " + APPLICATION_JSON_UTF8_VALUE,
        X_AUTH_TOKEN + ": {xAuthToken}"
})
public interface ValidationApiService {

    @RequestLine("GET /uniqueJobKey?keyName={keyName}")
    RestResponseVO<Boolean> isJobKeyUnique(@Param("keyName") String keyName, @Param("xAuthToken") String xAuthToken);

    @RequestLine("GET /uniqueTriggerKey?keyName={keyName}")
    RestResponseVO<Boolean> isTriggerKeyUnique(@Param("keyName") String keyName, @Param("xAuthToken") String value);
}
