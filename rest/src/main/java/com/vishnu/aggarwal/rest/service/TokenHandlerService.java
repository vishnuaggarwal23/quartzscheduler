package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 19/4/18 3:43 PM
*/

import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.*;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.System.currentTimeMillis;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;

@Service
@CommonsLog
@Transactional
public class TokenHandlerService extends BaseService implements com.vishnu.aggarwal.rest.interfaces.TokenHandlerService {

    private final UserService userService;
    private final UserTokenService userTokenService;
    private final UserDetailsChecker accountStatusUserDetailsCheck;
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.expirationTime:''}")
    private String EXPIRATION_TIME;

    @Autowired
    public TokenHandlerService(
            UserService userService,
            UserTokenService userTokenService,
            UserDetailsChecker accountStatusUserDetailsCheck) {
        this.userService = userService;
        this.userTokenService = userTokenService;
        this.accountStatusUserDetailsCheck = accountStatusUserDetailsCheck;
    }

    @Cacheable(value = "parseTokenToGetUser", key = "#token", unless = "#result == null")
    public User parseToken(final String token) throws JwtException {
        try {
            log.info("Parsing X-AUTH-TOKEN " + token);
            final User userFromUsername = userService.findByUsername(getClaim(token).getSubject());
            if (!userTokenService.findByToken(token).getUser().equals(userFromUsername)) {
                throw new JwtException("");
            }
            accountStatusUserDetailsCheck.check(userFromUsername);
            return userFromUsername;
        } catch (AuthenticationException e) {
            log.error("Exception occurred", getRootCause(e));
            throw new JwtException(e.getMessage(), e);
        } catch (JwtException e) {
            log.error("Exception occurred", getRootCause(e));
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred", getRootCause(e));
            throw new JwtException(e.getMessage(), e);
        }
    }

    private Claims getClaim(final String token) throws JwtException {
        Claims claims = parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        if (isNull(claims)) {
            throw new JwtException("");
        }
        return claims;
    }

    public Map<String, Object> generateToken(final User user) {

        Map<String, Object> tokenMap = new HashMap<>();
        final Date now = new Date(currentTimeMillis());
        final Date expiration = isNotBlank(EXPIRATION_TIME) ? new Date(now.getTime() + Long.parseLong(EXPIRATION_TIME)) : null;
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

    public Boolean isValidToken(final String token) throws JwtException {
        try {
            accountStatusUserDetailsCheck.check(parseToken(token));
            final Date tokenExpirationDate = getClaim(token).getExpiration();
            return !nonNull(tokenExpirationDate) || tokenExpirationDate.after(new Date(currentTimeMillis()));
        } catch (AuthenticationException e) {
            log.error("Exception occurred", getRootCause(e));
            throw new JwtException(e.getMessage(), e);
        } catch (JwtException e) {
            log.error("Exception occurred", getRootCause(e));
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred", getRootCause(e));
            throw new JwtException(e.getMessage(), e);
        }
    }
}
