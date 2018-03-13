package com.vishnu.aggarwal.admin.service;

/*
Created by vishnu on 13/3/18 11:40 AM
*/

import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

import static java.lang.Boolean.TRUE;

@Service
@CommonsLog
public class AuthenticationService extends BaseService {

    @Autowired
    RestService restService;

    public Boolean isAuthenticatedUser(Cookie cookie) {
        return TRUE;
    }

    public Boolean isAuthorizedUser(Cookie cookie) {
        return TRUE;
    }

    public UserDTO getCurrentLoggedInUser(Cookie cookie) {
        return new UserDTO();
    }

    public String loginUser(UserDTO userDTO) {
        return "";
    }

    public String logoutUser(Cookie cookie) {
        return "";
    }
}
