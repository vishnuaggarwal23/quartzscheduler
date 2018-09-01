package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 13/3/18 11:40 AM
*/

import com.vishnu.aggarwal.core.dto.UserDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.Cookie;

import static com.vishnu.aggarwal.core.enums.RestApiEndPoint.*;

/**
 * The type Authentication service.
 */
@Service
@CommonsLog
@SuppressWarnings("unchecked")
public class AuthenticationService {

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
     * Is authenticated user rest response vo.
     *
     * @param cookie the cookie
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<String> isAuthenticatedUser(final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(null, cookie.getValue(), AUTHENTICATE.getApiEndPoint(), AUTHENTICATE.getHttpMethod(), null, null);
    }

    /**
     * Gets current logged in user.
     *
     * @param cookie the cookie
     * @return the current logged in user
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<String> getCurrentLoggedInUser(final Cookie cookie) throws RestClientException {
        return restService.getResponseFromBackendService(null, cookie.getValue(), CURRENT_LOGGED_IN_USER.getApiEndPoint(), CURRENT_LOGGED_IN_USER.getHttpMethod(), null, null);
    }

    /**
     * Login user rest response vo.
     *
     * @param login the login
     * @return the rest response vo
     * @throws RestClientException the rest client exception
     */
    public ResponseEntity<String> loginUser(final UserDTO login) throws RestClientException {
        return restService.getResponseFromBackendService(login, null, LOGIN.getApiEndPoint(), LOGIN.getHttpMethod(), null, null);
    }

    /**
     * Logout user rest response vo.
     *
     * @param cookie the cookie
     * @return the rest response vo
     */
    public void logoutUser(final Cookie cookie) {
        restService.getResponseFromBackendService(null, cookie.getValue(), LOGOUT.getApiEndPoint(), LOGOUT.getHttpMethod(), null, null);
    }
}
