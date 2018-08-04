package com.vishnu.aggarwal.rest.interfaces;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vishnu.aggarwal.rest.entity.UserToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.hibernate.HibernateException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.persistence.NoResultException;
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
     * @throws JsonParseException          the json parse exception
     * @throws JsonMappingException        the json mapping exception
     * @throws HibernateException          the hibernate exception
     * @throws LockedException             the locked exception
     * @throws DisabledException           the disabled exception
     * @throws AccountExpiredException     the account expired exception
     * @throws CredentialsExpiredException the credentials expired exception
     * @throws IllegalStateException       the illegal state exception
     * @throws AuthenticationException     the authentication exception
     * @throws IllegalArgumentException    the illegal argument exception
     * @throws IOException                 the io exception
     */
    public Authentication getAuthenticationForLogin(HttpServletRequest request, HttpServletResponse response, AuthenticationManager authenticationManager) throws JsonParseException, JsonMappingException, HibernateException, LockedException, DisabledException, AccountExpiredException, CredentialsExpiredException, IllegalStateException, AuthenticationException, IllegalArgumentException, IOException;

    /**
     * Gets authentication.
     *
     * @param request  the request
     * @param response the response
     * @return the authentication
     * @throws ExpiredJwtException         the expired jwt exception
     * @throws UnsupportedJwtException     the unsupported jwt exception
     * @throws MalformedJwtException       the malformed jwt exception
     * @throws SignatureException          the signature exception
     * @throws IllegalArgumentException    the illegal argument exception
     * @throws LockedException             the locked exception
     * @throws DisabledException           the disabled exception
     * @throws AccountExpiredException     the account expired exception
     * @throws CredentialsExpiredException the credentials expired exception
     */
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, LockedException, DisabledException, AccountExpiredException, CredentialsExpiredException, NoResultException;

    /**
     * Add authentication user token.
     *
     * @param request                             the request
     * @param usernamePasswordAuthenticationToken the username password authentication token
     * @return the user token
     * @throws IllegalArgumentException    the illegal argument exception
     * @throws HibernateException          the hibernate exception
     * @throws LockedException             the locked exception
     * @throws DisabledException           the disabled exception
     * @throws AccountExpiredException     the account expired exception
     * @throws CredentialsExpiredException the credentials expired exception
     * @throws IllegalStateException       the illegal state exception
     */
    public UserToken addAuthentication(HttpServletRequest request, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws IllegalArgumentException, HibernateException, LockedException, DisabledException, AccountExpiredException, CredentialsExpiredException, IllegalStateException;

    /**
     * Remove authentication boolean.
     *
     * @param authentication the authentication
     * @return the boolean
     */
    Boolean removeAuthentication(Authentication authentication);
}
