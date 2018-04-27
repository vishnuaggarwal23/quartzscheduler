package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 13/3/18 11:40 AM
*/

import com.vishnu.aggarwal.admin.service.feign.AuthenticationApiService;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.exceptions.RestServiceCallException;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User.BASE_URI;
import static com.vishnu.aggarwal.core.enums.RestApiEndPoint.CURRENT_LOGGED_IN_USER;
import static com.vishnu.aggarwal.core.enums.RestApiEndPoint.LOGOUT_USER;

/**
 * The type Authentication service.
 */
@Service
@CommonsLog
@SuppressWarnings("unchecked")
public class AuthenticationService extends FeignService {

    /**
     * The Rest service.
     */
    @Autowired
    RestService restService;

    /**
     * Authentication api service authentication api service.
     *
     * @return the authentication api service
     */
    @Bean
    @Primary
    AuthenticationApiService authenticationApiService() {
        return getBean(AuthenticationApiService.class, BASE_URI);
    }

    /**
     * Is authenticated user rest response vo.
     *
     * @param cookie the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<Boolean> isAuthenticatedUser(Cookie cookie) throws RestServiceCallException, Exception {
//        return (RestResponseVO<Boolean>) restService.getResponseFromBackendService(null, cookie.getValue(), USER_AUTHENTICATION.getApiEndPoint(), (HttpMethod) USER_AUTHENTICATION.getHttpMethods(), USER_AUTHENTICATION.getResponseTypeClass());
        return authenticationApiService().isAuthenticatedUser(cookie.getValue());
    }

    /**
     * Gets current logged in user.
     *
     * @param cookie the cookie
     * @return the current logged in user
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<UserDTO> getCurrentLoggedInUser(Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<UserDTO>) restService.getResponseFromBackendService(null, cookie.getValue(), CURRENT_LOGGED_IN_USER.getApiEndPoint(), (HttpMethod) CURRENT_LOGGED_IN_USER.getHttpMethods(), CURRENT_LOGGED_IN_USER.getResponseTypeClass());
    }

    /**
     * Login user rest response vo.
     *
     * @param login the login
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<String> loginUser(UserDTO login) throws RestServiceCallException, Exception {
//        return (RestResponseVO<String>) restService.getResponseFromBackendService(userDTO, null, LOGIN_USER.getApiEndPoint(), (HttpMethod) LOGIN_USER.getHttpMethods(), LOGIN_USER.getResponseTypeClass());
        return authenticationApiService().login(login);
    }

    /**
     * Logout user rest response vo.
     *
     * @param cookie the cookie
     * @return the rest response vo
     * @throws RestServiceCallException the rest service call exception
     * @throws Exception                the exception
     */
    public RestResponseVO<String> logoutUser(Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<String>) restService.getResponseFromBackendService(null, cookie.getValue(), LOGOUT_USER.getApiEndPoint(), (HttpMethod) LOGOUT_USER.getHttpMethods(), LOGOUT_USER.getResponseTypeClass());
    }
}
