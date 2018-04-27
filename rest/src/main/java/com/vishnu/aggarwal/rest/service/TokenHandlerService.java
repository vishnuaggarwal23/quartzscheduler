package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 19/4/18 3:43 PM
*/

import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserToken;
import com.vishnu.aggarwal.rest.exception.JwtException;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.*;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.Boolean.FALSE;
import static java.lang.System.currentTimeMillis;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * The type Token handler service.
 */
@Service
@CommonsLog
public class TokenHandlerService extends BaseService implements com.vishnu.aggarwal.rest.interfaces.TokenHandlerService {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expirationTime}")
    private String EXPIRATION_TIME;

    /**
     * The User service.
     */
    @Autowired
    UserService userService;

    /**
     * The User token service.
     */
    @Autowired
    UserTokenService userTokenService;

    /**
     * The Account status user details check.
     */
    @Autowired
    UserDetailsChecker accountStatusUserDetailsCheck;

    public User parseToken(String token) throws AuthenticationException {
        log.info("***************** Parsing X-AUTH-TOKEN " + token + " ********************\n");
        try {
            if (isEmpty(token)) {
                throw new JwtException(formatMessage(getMessage("")));
            }

            Claims claims = this.getClaim(token);
            if (nonNull(claims)) {
                String username = claims.getSubject();
                UserToken userToken = userTokenService.findByToken(token);
                if (nonNull(userToken)) {
                    User userFromUserToken = userToken.getUser();
                    User userFromUsername = userService.findByUsername(username);
                    if (nonNull(userFromUsername) && nonNull(userFromUserToken) && userFromUserToken.equals(userFromUsername)) {
                        accountStatusUserDetailsCheck.check(userFromUsername);
                        return userFromUsername;
                    } else {
                        throw new JwtException(formatMessage(getMessage("")));
                    }
                } else {
                    throw new JwtException(formatMessage(getMessage("")));
                }
            } else {
                throw new JwtException(formatMessage(getMessage("")));
            }
        } catch (AuthenticationException e) {
            log.error("******************* Error while fetching user name from X-AUTH-TOKEN " + token + " ***************** \n");
            log.error("******************* Stacktrace ****************** \n");
            log.error(getStackTrace(e));
            throw e;
        }
    }

    private Claims getClaim(String token) {
        return parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public Map<String, Object> generateToken(User user) {

        Map<String, Object> tokenMap = new HashMap<>();
        final Date now = new Date(currentTimeMillis());
        Date expiration = null;
        if (isNotBlank(EXPIRATION_TIME)) {
            expiration = new Date(now.getTime() + Long.parseLong(EXPIRATION_TIME));
        }
        final String issueId = user.getId().toString();

        Claims claims = claims()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setId(issueId);
        if (nonNull(expiration)) {
            claims.setExpiration(expiration);
        }

        JwtBuilder jwtBuilder = builder()
                .setClaims(claims)
                .signWith(HS512, SECRET)
                .setIssuedAt(now)
                .setId(issueId);
        if (nonNull(expiration)) {
            jwtBuilder.setExpiration(expiration);
        }

        tokenMap.put("issueId", issueId);
        tokenMap.put("issuedDate", now);
        tokenMap.put("expirationDate", expiration);
        tokenMap.put("key", jwtBuilder.compact());
        return tokenMap;
    }

    public Boolean isTokenExpired(String token) {
        Claims claims = this.getClaim(token);
        if (nonNull(claims)) {
            return claims.getExpiration().before(new Date(currentTimeMillis()));
        }
        return FALSE;
    }

    public Boolean isValidToken(String token) {
        final User user = this.parseToken(token);
        if (nonNull(user)) {
            try {
                accountStatusUserDetailsCheck.check(user);
                return !this.isTokenExpired(token);
            } catch (AuthenticationException e) {
                log.error("******************* Error while checking validity of JWT Token " + token + " ***************** \n");
                log.error("******************* Stacktrace ****************** \n");
                log.error(getStackTrace(e));
            }
        }
        return FALSE;
    }
}
