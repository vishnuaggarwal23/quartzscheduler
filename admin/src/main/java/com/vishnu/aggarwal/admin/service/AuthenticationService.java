package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 13/3/18 11:40 AM
*/

import com.vishnu.aggarwal.admin.service.feign.AuthenticationApiService;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.Cookie;

import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User.BASE_URI;
import static com.vishnu.aggarwal.core.enums.RestApiEndPoint.*;

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
    private final RestService restService;

    /**
     * Instantiates a new Authentication service.
     *
     * @param restService the rest service
     */
    @Autowired
    public AuthenticationService(RestService restService) {
        this.restService = restService;
    }

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
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<UserAuthenticationDTO> isAuthenticatedUser(Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(null, cookie.getValue(), USER_AUTHENTICATION.getApiEndPoint(), USER_AUTHENTICATION.getHttpMethod(), UserAuthenticationDTO.class);
    }

    /**
     * Gets current logged in user.
     *
     * @param cookie the cookie
     * @return the current logged in user
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<UserDTO> getCurrentLoggedInUser(Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(null, cookie.getValue(), CURRENT_LOGGED_IN_USER.getApiEndPoint(), CURRENT_LOGGED_IN_USER.getHttpMethod(), UserDTO.class);
    }

    /**
     * Login user rest response vo.
     *
     * @param login the login
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<UserAuthenticationDTO> loginUser(UserDTO login) throws RestClientException {
        return restService.getResponseFromBackendService(login, null, LOGIN_USER.getApiEndPoint(), LOGIN_USER.getHttpMethod(), UserAuthenticationDTO.class);
    }

    /**
     * Logout user rest response vo.
     *
     * @param cookie the cookie
     * @return the rest response vo
     */
    public void logoutUser(Cookie cookie) {
        authenticationApiService().logout(cookie.getValue());
    }
}
