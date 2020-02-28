package com.vishnu.aggarwal.quartz.rest.interfaces;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The interface Token authentication service.
 */
/*
Created by vishnu on 20/4/18 11:27 AM
*/
@Transactional
public interface TokenAuthenticationService {

    /**
     * Gets authentication for login.
     *
     * @param request               the request
     * @param response              the response
     * @param authenticationManager the authentication manager
     * @return the authentication for login
     * @throws AuthenticationException the authentication exception
     */
    Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationManager authenticationManager) throws AuthenticationException;

    /**
     * Gets authentication.
     *
     * @param request  the request
     * @param response the response
     * @return the authentication
     * @throws AuthenticationException the authentication exception
     */
    Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException;

    /**
     * Remove authentication boolean.
     *
     * @param authentication the authentication
     * @param request        the request
     * @return the boolean
     * @throws AuthenticationException the authentication exception
     */
    Boolean removeAuthentication(Authentication authentication, HttpServletRequest request) throws AuthenticationException;
}
