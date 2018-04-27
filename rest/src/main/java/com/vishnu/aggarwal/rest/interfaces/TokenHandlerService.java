package com.vishnu.aggarwal.rest.interfaces;

/*
Created by vishnu on 20/4/18 12:11 PM
*/

import com.vishnu.aggarwal.rest.entity.User;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

/**
 * The interface Token handler service.
 */
public interface TokenHandlerService {
    /**
     * Parse token user.
     *
     * @param xAuthToken the x auth token
     * @return the user
     * @throws AuthenticationException the authentication exception
     */
    public User parseToken(String xAuthToken) throws AuthenticationException;

    /**
     * Generate token map.
     *
     * @param user the user
     * @return the map
     */
    public Map<String, Object> generateToken(User user);

    /**
     * Is token expired boolean.
     *
     * @param token the token
     * @return the boolean
     */
    public Boolean isTokenExpired(String token);

    /**
     * Is valid token boolean.
     *
     * @param token the token
     * @return the boolean
     */
    public Boolean isValidToken(String token);
}