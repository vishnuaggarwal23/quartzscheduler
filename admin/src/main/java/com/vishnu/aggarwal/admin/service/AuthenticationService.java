package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 13/3/18 11:40 AM
*/

import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.exceptions.RestServiceCallException;
import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

import static com.vishnu.aggarwal.core.enums.RestApiEndPoint.*;

@Service
@CommonsLog
@SuppressWarnings("unchecked")
public class AuthenticationService extends BaseService {

    @Autowired
    RestService restService;

    public RestResponseVO<Boolean> isAuthenticatedUser(Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<Boolean>) restService.getResponseFromBackendService(null, cookie.getValue(), USER_AUTHENTICATION.getApiEndPoint(), (HttpMethod) USER_AUTHENTICATION.getHttpMethods(), USER_AUTHENTICATION.getResponseTypeClass());
    }

    public RestResponseVO<UserDTO> getCurrentLoggedInUser(Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<UserDTO>) restService.getResponseFromBackendService(null, cookie.getValue(), CURRENT_LOGGED_IN_USER.getApiEndPoint(), (HttpMethod) CURRENT_LOGGED_IN_USER.getHttpMethods(), CURRENT_LOGGED_IN_USER.getResponseTypeClass());
    }

    public RestResponseVO<String> loginUser(UserDTO userDTO) throws RestServiceCallException, Exception {
        return (RestResponseVO<String>) restService.getResponseFromBackendService(userDTO, null, LOGIN_USER.getApiEndPoint(), (HttpMethod) LOGIN_USER.getHttpMethods(), LOGIN_USER.getResponseTypeClass());
    }

    public RestResponseVO<String> logoutUser(Cookie cookie) throws RestServiceCallException, Exception {
        return (RestResponseVO<String>) restService.getResponseFromBackendService(null, cookie.getValue(), LOGOUT_USER.getApiEndPoint(), (HttpMethod) LOGOUT_USER.getHttpMethods(), LOGOUT_USER.getResponseTypeClass());
    }
}
