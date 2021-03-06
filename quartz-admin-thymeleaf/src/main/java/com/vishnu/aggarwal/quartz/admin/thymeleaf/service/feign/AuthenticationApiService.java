package com.vishnu.aggarwal.quartz.admin.thymeleaf.service.feign;

/*
Created by vishnu on 19/4/18 11:35 AM
*/

import com.vishnu.aggarwal.quartz.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.quartz.core.dto.UserDTO;
import com.vishnu.aggarwal.quartz.core.vo.RestResponseVO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * The interface Authentication api service.
 */
@Headers(value = {"Content-Type: " + APPLICATION_JSON_UTF8_VALUE})
public interface AuthenticationApiService {

    /**
     * Is authenticated user rest response vo.
     *
     * @param xAuthToken the x auth token
     * @return the rest response vo
     */
    @Headers(value = {X_AUTH_TOKEN + ": {xAuthToken}"})
    @RequestLine("GET /authenticate")
    RestResponseVO<UserAuthenticationDTO> isAuthenticatedUser(@Param("xAuthToken") String xAuthToken);

    /**
     * Login rest response vo.
     *
     * @param login the login
     * @return the rest response vo
     */
    @RequestLine("POST /login")
    RestResponseVO<UserAuthenticationDTO> login(@RequestBody UserDTO login);

    @Headers(value = {X_AUTH_TOKEN + ": {xAuthToken}"})
    @RequestLine("POST /logout")
    void logout(@Param("xAuthToken") String xAuthToken);
}
