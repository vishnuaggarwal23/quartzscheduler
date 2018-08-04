package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 20/4/18 11:35 AM
*/

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserToken;
import com.vishnu.aggarwal.rest.interfaces.TokenHandlerService;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static com.vishnu.aggarwal.core.enums.Status.ACTIVE;
import static io.jsonwebtoken.lang.Assert.*;
import static java.lang.Boolean.FALSE;
import static java.lang.String.valueOf;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * The type Token authentication service.
 */
@Service
@Transactional
@CommonsLog
public class TokenAuthenticationService extends BaseService implements com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService {

    /**
     * The User service.
     */
    private final UserService userService;

    /**
     * The Token handler service.
     */
    private final TokenHandlerService tokenHandlerService;

    /**
     * The User token service.
     */
    private final UserTokenService userTokenService;

    /**
     * The Token service.
     */
    private final TokenService tokenService;

    /**
     * The Object mapper.
     */
    private final ObjectMapper objectMapper;

    /**
     * The Account status user details check.
     */
    private final UserDetailsChecker accountStatusUserDetailsCheck;

    /**
     * Instantiates a new Token authentication service.
     *
     * @param userService                   the user service
     * @param tokenHandlerService           the token handler service
     * @param userTokenService              the user token service
     * @param tokenService                  the token service
     * @param objectMapper                  the object mapper
     * @param accountStatusUserDetailsCheck the account status user details check
     */
    @Autowired
    public TokenAuthenticationService(UserService userService,
                                      TokenHandlerService tokenHandlerService,
                                      UserTokenService userTokenService,
                                      TokenService tokenService,
                                      ObjectMapper objectMapper,
                                      UserDetailsChecker accountStatusUserDetailsCheck) {
        this.userService = userService;
        this.tokenHandlerService = tokenHandlerService;
        this.userTokenService = userTokenService;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
        this.accountStatusUserDetailsCheck = accountStatusUserDetailsCheck;
    }

    @Override
    public Authentication getAuthenticationForLogin(HttpServletRequest request, HttpServletResponse response, AuthenticationManager authenticationManager) throws JsonParseException, JsonMappingException, HibernateException, LockedException, DisabledException, AccountExpiredException, CredentialsExpiredException, IllegalStateException, AuthenticationException, IllegalArgumentException, IOException {
        try {
            UserDTO login = objectMapper.readValue(IOUtils.toString(request.getReader()), UserDTO.class);

            hasLength(login.getUsername(), formatMessage(getMessage("")));
            hasLength(login.getPassword(), formatMessage(getMessage("")));
            notNull(userService.findByUsername(login.getUsername()), formatMessage(getMessage("")));

            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Attempting Authentication for [Username : " + login.getUsername() + "] and [Password : " + login.getPassword() + "]");
            UsernamePasswordAuthenticationToken authenticatedUser = (UsernamePasswordAuthenticationToken) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

            notNull(authenticatedUser, formatMessage(getMessage("")));

            Boolean isAuthentic = authenticatedUser.isAuthenticated();
            User user = (User) authenticatedUser.getPrincipal();

            log.debug("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Authentication Result : " + isAuthentic + " for [Username : " + login.getUsername() + "] and [Password : " + login.getPassword() + "]");

            notNull(user, formatMessage(getMessage("")));
            isTrue(isAuthentic, formatMessage(getMessage("")));

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(addAuthentication(request, usernamePasswordAuthenticationToken).getToken());
            return usernamePasswordAuthenticationToken;
        } catch (JsonParseException | JsonMappingException | IllegalArgumentException | HibernateException | LockedException | DisabledException | AccountExpiredException | CredentialsExpiredException | IllegalStateException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] User authentication failed.");
            log.error(getStackTrace(e));
            throw e;
        } catch (IOException | AuthenticationException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] User authentication failed.");
            log.error(getStackTrace(e));
            throw e;
        }
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, LockedException, DisabledException, AccountExpiredException, CredentialsExpiredException, NoResultException {
        try {
            final String xAuthToken = request.getHeader(X_AUTH_TOKEN);
            hasText(xAuthToken, formatMessage(getMessage("")));
            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Authenticating user associated for JWT token " + xAuthToken);

            Boolean isValidToken = tokenHandlerService.isValidToken(xAuthToken);

            isTrue(isValidToken, formatMessage(getMessage("")));

            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] JWT token " + xAuthToken + " is found to be valid.");

            User user = tokenHandlerService.parseToken(xAuthToken);
            notNull(user, formatMessage(getMessage("")));

            accountStatusUserDetailsCheck.check(user);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(tokenService.findByTokenAndIsDeleted(xAuthToken, FALSE));
            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] User " + user + " authenticated for JWT token " + xAuthToken);
            return usernamePasswordAuthenticationToken;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | LockedException | DisabledException | AccountExpiredException | CredentialsExpiredException | NoResultException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while getting authentication");
            log.error(getStackTrace(e));
            throw e;
        }
    }

    @Override
    public UserToken addAuthentication(HttpServletRequest request, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws IllegalArgumentException, HibernateException, LockedException, DisabledException, AccountExpiredException, CredentialsExpiredException, IllegalStateException {
        try {
            final Object principal = usernamePasswordAuthenticationToken.getPrincipal();
            String username = "";
            User user = null;

            notNull(principal, formatMessage(getMessage("")));

            if (principal instanceof String) {
                username = (String) principal;
                log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Generating JWT Token for username " + username);
                hasText(username, formatMessage(getMessage("")));
                user = userService.findByUsername(username);
            } else if (principal instanceof User) {
                user = (User) principal;
                username = user.getUsername();
            }

            notNull(user, formatMessage(getMessage("")));
            hasText(username, formatMessage(getMessage("")));
            accountStatusUserDetailsCheck.check(user);

            Map<String, Object> tokenMap = tokenHandlerService.generateToken(user);
            notEmpty(tokenMap, formatMessage(getMessage("")));

            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] JWT Token " + tokenMap.get("key") + " generated for user " + user);

            Token token = tokenService.findByTokenAndIsDeleted(valueOf(tokenMap.get("key")), FALSE);
            if (isNull(token)) {
                token = new Token();
                token.setIssueId(valueOf(tokenMap.get("issueId")));
                token.setToken(valueOf(tokenMap.get("key")));
                token.setExpirationDate((Date) tokenMap.get("expirationDate"));
                token.setIssuedDate((Date) tokenMap.get("issuedDate"));
                token = tokenService.save(token);
            }
            notNull(token, formatMessage(getMessage("")));

            UserToken userToken = new UserToken();
            userToken.setUser(user);
            userToken.setToken(token);
            userToken.setStatus(ACTIVE);
            userTokenService.updateTokenStatus(user);
            userToken = userTokenService.save(userToken);
            notNull(userToken, formatMessage(getMessage("")));

            return userToken;
        } catch (IllegalArgumentException | HibernateException | LockedException | DisabledException | AccountExpiredException | CredentialsExpiredException | IllegalStateException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while adding authentication to user");
            log.error(getStackTrace(e));
            throw e;
        }
    }

    @Override
    public Boolean removeAuthentication(Authentication authentication) {
        final Object principal = authentication.getPrincipal();
        String username = "";
        User user = null;
        if (principal instanceof String) {
            username = (String) principal;
            user = isNotBlank(username) ? userService.findByUsername(username) : null;
        } else if (principal instanceof User) {
            user = (User) principal;
        }
        if (nonNull(user)) {
            try {
                accountStatusUserDetailsCheck.check(user);
                if (!isEmpty(userTokenService.findAllUserTokens(user))) {
                    return userTokenService.updateTokenStatus(user);
                }
            } catch (Exception e) {
                log.error("****Error while invalidating all authentication token objects for user " + user + " **\n");
                log.error("****Stacktrace ***\n");
                log.error(getStackTrace(e));
            }
        }
        return FALSE;
    }
}
