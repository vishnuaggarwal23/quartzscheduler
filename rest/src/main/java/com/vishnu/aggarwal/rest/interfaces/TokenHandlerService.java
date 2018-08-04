package com.vishnu.aggarwal.rest.interfaces;

/*
Created by vishnu on 20/4/18 12:11 PM
*/

import com.vishnu.aggarwal.rest.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import javax.persistence.NoResultException;
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
    public User parseToken(String xAuthToken) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, LockedException, DisabledException, AccountExpiredException, CredentialsExpiredException, NoResultException;

    /**
     * Generate token map.
     *
     * @param user the user
     * @return the map
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalStateException    the illegal state exception
     */
    public Map<String, Object> generateToken(User user) throws IllegalArgumentException, IllegalStateException;

    /**
     * Is valid token boolean.
     *
     * @param token the token
     * @return the boolean
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
    public Boolean isValidToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, LockedException, DisabledException, AccountExpiredException, CredentialsExpiredException, NoResultException;
}