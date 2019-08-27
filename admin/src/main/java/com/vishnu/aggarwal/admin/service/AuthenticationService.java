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

@Service
@CommonsLog
@SuppressWarnings("unchecked")
public class AuthenticationService {

    private final RestService restService;

    @Autowired
    public AuthenticationService(RestService restService) {
        this.restService = restService;
    }

    public ResponseEntity<String> isAuthenticatedUser(final Cookie xAuthToken) throws RestClientException {
        return isAuthenticatedUser(xAuthToken.getValue());
    }

    public ResponseEntity<String> isAuthenticatedUser(final String xAuthToken) throws RestClientException {
        return restService.getResponseFromBackendService(null, xAuthToken, AUTHENTICATE.getApiEndPoint(), AUTHENTICATE.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> getCurrentLoggedInUser(final Cookie xAuthToken) throws RestClientException {
        return getCurrentLoggedInUser(xAuthToken.getValue());
    }

    public ResponseEntity<String> getCurrentLoggedInUser(final String xAuthToken) throws RestClientException {
        return restService.getResponseFromBackendService(null, xAuthToken, CURRENT_LOGGED_IN_USER.getApiEndPoint(), CURRENT_LOGGED_IN_USER.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> loginUser(final UserDTO login) throws RestClientException {
        return restService.getResponseFromBackendService(login, null, LOGIN.getApiEndPoint(), LOGIN.getHttpMethod(), null, null);
    }

    public ResponseEntity<String> logoutUser(final Cookie xAuthToken) {
        return logoutUser(xAuthToken.getValue());
    }

    public ResponseEntity<String> logoutUser(final String xAuthToken) {
        return restService.getResponseFromBackendService(null, xAuthToken, LOGOUT.getApiEndPoint(), LOGOUT.getHttpMethod(), null, null);
    }
}
