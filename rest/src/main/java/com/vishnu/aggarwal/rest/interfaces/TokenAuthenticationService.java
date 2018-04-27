package com.vishnu.aggarwal.rest.interfaces;

import com.vishnu.aggarwal.rest.entity.UserToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The interface Token authentication service.
 */
/*
Created by vishnu on 20/4/18 11:27 AM
*/
public interface TokenAuthenticationService {
    /**
     * Gets authentication for login.
     *
     * @param request               the request
     * @param response              the response
     * @param authenticationManager the authentication manager
     * @return the authentication for login
     * @throws AuthenticationException the authentication exception
     * @throws IOException             the io exception
     */
    public Authentication getAuthenticationForLogin(HttpServletRequest request, HttpServletResponse response, AuthenticationManager authenticationManager) throws AuthenticationException, IOException;

    /**
     * Gets authentication.
     *
     * @param request  the request
     * @param response the response
     * @return the authentication
     */
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response);

    /**
     * Add authentication user token.
     *
     * @param response                            the response
     * @param usernamePasswordAuthenticationToken the username password authentication token
     * @return the user token
     */
    public UserToken addAuthentication(HttpServletResponse response, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken);

    /**
     * Remove authentication boolean.
     *
     * @param authentication the authentication
     * @return the boolean
     */
    Boolean removeAuthentication(Authentication authentication);
}
