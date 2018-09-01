package com.vishnu.aggarwal.rest.interfaces;

/*
Created by vishnu on 20/4/18 12:11 PM
*/

import com.vishnu.aggarwal.rest.entity.User;
import io.jsonwebtoken.JwtException;

import java.util.Map;

/**
 * The interface Token handler service.
 */
public interface TokenHandlerService {
    /**
     * Parse token user.
     *
     * @param token the token
     * @return the user
     * @throws JwtException the jwt exception
     */
    User parseToken(final String token) throws JwtException;

    /**
     * Generate token map.
     *
     * @param user the user
     * @return the map
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalStateException    the illegal state exception
     */
    Map<String, Object> generateToken(final User user);

    /**
     * Is valid token boolean.
     *
     * @param token the token
     * @return the boolean
     * @throws JwtException the jwt exception
     */
    Boolean isValidToken(final String token) throws JwtException;
}