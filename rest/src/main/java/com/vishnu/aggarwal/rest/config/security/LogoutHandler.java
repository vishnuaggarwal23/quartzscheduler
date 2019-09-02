package com.vishnu.aggarwal.rest.config.security;

/*
Created by vishnu on 21/4/18 5:47 PM
*/

import com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static java.lang.Boolean.FALSE;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@CommonsLog
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    @Autowired
    TokenAuthenticationService tokenAuthenticationService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            if (tokenAuthenticationService.removeAuthentication(authentication, request)) {
                HttpSession session = request.getSession(FALSE);
                if (nonNull(session)) {
                    session.invalidate();
                }
                getContext().setAuthentication(null);
                clearContext();
                response.setStatus(SC_OK);
            } else {
                throw new RuntimeException("");
            }
        } catch (AuthenticationException e) {
            log.error("Exception occurred", getRootCause(e));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Exception occurred", getRootCause(e));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
