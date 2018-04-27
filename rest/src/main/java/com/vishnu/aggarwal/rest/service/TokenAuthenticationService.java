package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 20/4/18 11:35 AM
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserToken;
import com.vishnu.aggarwal.rest.interfaces.TokenHandlerService;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static com.vishnu.aggarwal.core.enums.Status.ACTIVE;
import static java.lang.Boolean.FALSE;
import static java.lang.String.valueOf;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Transactional
@CommonsLog
public class TokenAuthenticationService extends BaseService implements com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService {

    @Autowired
    UserService userService;

    @Autowired
    TokenHandlerService tokenHandlerService;

    @Autowired
    UserTokenService userTokenService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    TokenService tokenService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserDetailsChecker accountStatusUserDetailsCheck;

    @Override
    public Authentication getAuthenticationForLogin(HttpServletRequest request, HttpServletResponse response, AuthenticationManager authenticationManager) throws AuthenticationException, IOException {
        UserDTO login = objectMapper.readValue(IOUtils.toString(request.getReader()), UserDTO.class);

        Boolean isAuthentic = FALSE;
        User user = null;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
        UsernamePasswordAuthenticationToken authenticatedUser = null;

        if (isNotBlank(login.getUsername()) && isNotBlank(login.getUsername()) && nonNull(userService.findByUsername(login.getUsername()))) {
            log.info("********************* [Request Interceptor Id " + request.getParameter(CUSTOM_REQUEST_ID) + "] Attempting Authentication for [Username : " + login.getUsername() + "] and [Password : " + login.getPassword() + "] **********************");
            authenticatedUser = (UsernamePasswordAuthenticationToken) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        }

        if (nonNull(authenticatedUser)) {
            isAuthentic = authenticatedUser.isAuthenticated();
            user = (User) authenticatedUser.getPrincipal();
        }

        log.debug("********************* [Request Interceptor Id " + request.getParameter(CUSTOM_REQUEST_ID) + "] Authentication Result : " + isAuthentic + " for [Username : " + login.getUsername() + "] and [Password : " + login.getPassword() + "] **********************");

        if (isTrue(isAuthentic) && nonNull(user)) {
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        } else {
            usernamePasswordAuthenticationToken = nonNull(user) ? new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()) : new UsernamePasswordAuthenticationToken(null, null, null);
            usernamePasswordAuthenticationToken.setAuthenticated(FALSE);
            log.error("********************* [Request Interceptor Id " + request.getParameter(CUSTOM_REQUEST_ID) + "] Authentication failed **********************");
        }

        return usernamePasswordAuthenticationToken;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        final String xAuthToken = request.getHeader(X_AUTH_TOKEN);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
        User user;
        log.info("*************** Authenticating user associated for JWT token " + xAuthToken + " *****************");

        if (isNotBlank(xAuthToken)) {
            try {
                user = tokenHandlerService.parseToken(xAuthToken);
            } catch (AuthenticationException e) {
                log.error("******************* Error while fetching user from X-AUTH-TOKEN " + xAuthToken + " ***************** \n");
                log.error("******************* Stacktrace ****************** \n");
                log.error(getStackTrace(e));
                user = null;
            }
            if (nonNull(user)) {
                try {
                    accountStatusUserDetailsCheck.check(user);
                    usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(xAuthToken);
                    log.info("*************** User " + user + " authenticated for JWT token " + xAuthToken + " *****************");
                    return usernamePasswordAuthenticationToken;
                } catch (AuthenticationException e) {
                    log.error("******************* Error while fetching creating authentication token object from user " + user + " ***************** \n");
                    log.error("******************* Stacktrace ****************** \n");
                    log.error(getStackTrace(e));
                }
            }
        }
        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(null, null, null);
        usernamePasswordAuthenticationToken.setAuthenticated(FALSE);
        usernamePasswordAuthenticationToken.setDetails(xAuthToken);
        response.addHeader(X_AUTH_TOKEN, xAuthToken);
        response.setStatus(SC_UNAUTHORIZED);
        log.error("****************** No user found or authenticated for JWT token " + xAuthToken + " ******************");
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public UserToken addAuthentication(HttpServletResponse response, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
        final Object principal = usernamePasswordAuthenticationToken.getPrincipal();
        String username = "";
        User user = null;
        if (principal instanceof String) {
            username = (String) principal;
            log.info("*************** Generating JWT Token for username " + username + " *****************");
            user = isNotBlank(username) ? userService.findByUsername(username) : null;
        } else if (principal instanceof User) {
            user = (User) principal;
            username = user.getUsername();
        }
        if (nonNull(user)) {
            try {
                accountStatusUserDetailsCheck.check(user);
                Map<String, Object> tokenMap = tokenHandlerService.generateToken(user);
                if (!isEmpty(tokenMap)) {
                    log.info("*************** JWT Token " + tokenMap.get("key") + " generated for username " + username + " *****************");
                    Token token = tokenService.findByTokenAndIsDeleted(valueOf(tokenMap.get("key")), FALSE);
                    if (isNull(token)) {
                        token = new Token();
                        token.setIssueId(valueOf(tokenMap.get("issueId")));
                        token.setToken(valueOf(tokenMap.get("key")));
                        token.setExpirationDate((Date) tokenMap.get("expirationDate"));
                        token.setIssuedDate((Date) tokenMap.get("issuedDate"));
                        token = tokenService.save(token);
                    }
                    if (nonNull(token)) {
                        UserToken userToken = new UserToken();
                        userToken.setUser(user);
                        userToken.setToken(token);
                        userToken.setStatus(ACTIVE);
                        userTokenService.updateTokenStatus(user);
                        if (nonNull(userTokenService.save(userToken))) {
                            response.addHeader(X_AUTH_TOKEN, token.getKey());
                            return userToken;
                        }
                    }
                }
            } catch (AuthenticationException e) {
                log.error("******************* Error while fetching generating authentication token object for user " + user + " ***************** \n");
                log.error("******************* Stacktrace ****************** \n");
                log.error(getStackTrace(e));
            }
        }
        response.addHeader(X_AUTH_TOKEN, null);
        log.error("************* No user or JWT token generated for user " + user + "  ***************");
        return null;
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
                log.error("******************* Error while invalidating all authentication token objects for user " + user + " ***************** \n");
                log.error("******************* Stacktrace ****************** \n");
                log.error(getStackTrace(e));
            }
        }
        return FALSE;
    }
}
