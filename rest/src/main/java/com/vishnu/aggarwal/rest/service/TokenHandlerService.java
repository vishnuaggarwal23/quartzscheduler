package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 19/4/18 3:43 PM
*/

import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserToken;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import io.jsonwebtoken.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.*;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.System.currentTimeMillis;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.util.Assert.*;

/**
 * The type Token handler service.
 */
@Service
@CommonsLog
public class TokenHandlerService extends BaseService implements com.vishnu.aggarwal.rest.interfaces.TokenHandlerService {

    /**
     * The User service.
     */
    private final UserService userService;
    /**
     * The User token service.
     */
    private final UserTokenService userTokenService;
    /**
     * The Account status user details check.
     */
    private final UserDetailsChecker accountStatusUserDetailsCheck;
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.expirationTime:''}")
    private String EXPIRATION_TIME;

    /**
     * Instantiates a new Token handler service.
     *
     * @param userService                   the user service
     * @param userTokenService              the user token service
     * @param accountStatusUserDetailsCheck the account status user details check
     */
    @Autowired
    public TokenHandlerService(
            UserService userService,
            UserTokenService userTokenService,
            UserDetailsChecker accountStatusUserDetailsCheck) {
        this.userService = userService;
        this.userTokenService = userTokenService;
        this.accountStatusUserDetailsCheck = accountStatusUserDetailsCheck;
    }

    public User parseToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, LockedException, DisabledException, AccountExpiredException, CredentialsExpiredException, NoResultException {
        log.info("Parsing X-AUTH-TOKEN " + token);
        hasText(token, formatMessage(getMessage("jwt.token.empty")));
        try {
            UserToken userToken = userTokenService.findByToken(token);
            notNull(userToken, formatMessage(getMessage("jwt.token.empty")));

            User userFromUserToken = userToken.getUser();
            notNull(userFromUserToken, formatMessage(getMessage("jwt.token.empty")));

            String username = getClaim(token).getSubject();
            hasText(username, formatMessage(getMessage("jwt.token.empty")));

            User userFromUsername = userService.findByUsername(username);
            notNull(userFromUsername, formatMessage(getMessage("jwt.token.empty")));

            state(userFromUserToken.equals(userFromUsername), formatMessage(getMessage("jwt.token.empty")));

            accountStatusUserDetailsCheck.check(userFromUsername);
            return userFromUsername;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | LockedException | DisabledException | AccountExpiredException | CredentialsExpiredException | NoResultException e) {
            log.error("Error while fetching user name from X-AUTH-TOKEN " + token);
            log.error(getStackTrace(e));
            throw e;
        }
    }

    private Claims getClaim(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        notNull(claims, formatMessage(getMessage("jwt.token.empty")));
        return claims;
    }

    public Map<String, Object> generateToken(User user) throws IllegalArgumentException, IllegalStateException {

        notNull(user, formatMessage(getMessage("jwt.token.empty")));

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

    public Boolean isValidToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, LockedException, DisabledException, AccountExpiredException, CredentialsExpiredException, NoResultException {
        try {
            accountStatusUserDetailsCheck.check(parseToken(token));
            return getClaim(token).getExpiration().after(new Date(currentTimeMillis()));
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | LockedException | DisabledException | AccountExpiredException | CredentialsExpiredException | NoResultException e) {
            log.error("Error while checking validity of JWT Token " + token);
            log.error(getStackTrace(e));
            throw e;
        }
    }
}
