package com.vishnu.aggarwal.rest.config.security;

/*
Created by vishnu on 21/4/18 5:47 PM
*/

import com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static java.lang.Boolean.FALSE;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * The type Logout handler.
 */
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    /**
     * The Token authentication service.
     */
    @Autowired
    TokenAuthenticationService tokenAuthenticationService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (tokenAuthenticationService.removeAuthentication(authentication)) {
            HttpSession session = request.getSession(FALSE);
            if (nonNull(session)) {
                session.invalidate();
            }
            getContext().setAuthentication(null);
            clearContext();
            response.setStatus(SC_OK);
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }
    }
}
