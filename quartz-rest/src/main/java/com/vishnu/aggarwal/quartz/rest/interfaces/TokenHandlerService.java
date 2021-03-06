package com.vishnu.aggarwal.quartz.rest.interfaces;

/*
Created by vishnu on 20/4/18 12:11 PM
*/

import com.vishnu.aggarwal.quartz.rest.entity.User;
import io.jsonwebtoken.JwtException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * The interface Token handler service.
 */
@Transactional
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