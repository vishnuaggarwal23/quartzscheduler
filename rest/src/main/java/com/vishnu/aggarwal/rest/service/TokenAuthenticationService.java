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
import com.vishnu.aggarwal.rest.exception.AuthTokenNotFoundException;
import com.vishnu.aggarwal.rest.exception.EmptyJwtTokenException;
import com.vishnu.aggarwal.rest.exception.PrincipalNotFoundException;
import com.vishnu.aggarwal.rest.interfaces.TokenHandlerService;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import io.jsonwebtoken.JwtException;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
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
import static java.lang.String.valueOf;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Transactional
@CommonsLog
public class TokenAuthenticationService extends BaseService implements com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService {

    private final UserService userService;

    private final TokenHandlerService tokenHandlerService;

    private final UserTokenService userTokenService;

    private final TokenService tokenService;

    private final ObjectMapper objectMapper;

    private final UserDetailsChecker accountStatusUserDetailsCheck;

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
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationManager authenticationManager) throws AuthenticationException {
        try {
            final UserDTO login = objectMapper.readValue(IOUtils.toString(request.getReader()), UserDTO.class);
            if (isNull(login)) {
                throw new AuthenticationCredentialsNotFoundException("");
            }

            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Attempting Authentication for [Username : " + login.getUsername() + "] and [Password : " + login.getPassword() + "]");

            final UsernamePasswordAuthenticationToken authenticatedUser = (UsernamePasswordAuthenticationToken) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
            if (isNull(authenticatedUser) || !authenticatedUser.isAuthenticated()) {
                throw new InsufficientAuthenticationException("");
            }

            final User user = (User) authenticatedUser.getPrincipal();
            if (isNull(user)) {
                throw new PrincipalNotFoundException("");
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(addAuthentication(request, usernamePasswordAuthenticationToken).getToken());
            return usernamePasswordAuthenticationToken;
        } catch (IOException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] User authentication failed.");
            log.error(getStackTrace(e));
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        } catch (AuthenticationException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] User authentication failed.");
            log.error(getStackTrace(e));
            throw e;
        } catch (Exception e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] User authentication failed.");
            log.error(getStackTrace(e));
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            final String xAuthToken = request.getHeader(X_AUTH_TOKEN);
            if (isBlank(xAuthToken)) {
                throw new AuthTokenNotFoundException("");
            }

            if (!tokenHandlerService.isValidToken(xAuthToken)) {
                throw new JwtException("");
            }

            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] JWT token " + xAuthToken + " is found to be valid.");

            final User user = tokenHandlerService.parseToken(xAuthToken);
            if (isNull(user)) {
                throw new JwtException("");
            }

            accountStatusUserDetailsCheck.check(user);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(tokenService.findByToken(xAuthToken));
            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] User " + user + " authenticated for JWT token " + xAuthToken);
            return usernamePasswordAuthenticationToken;
        } catch (JwtException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while getting authentication");
            log.error(getStackTrace(e));
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        } catch (AuthenticationException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while getting authentication");
            log.error(getStackTrace(e));
            throw e;
        } catch (Exception e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while getting authentication");
            log.error(getStackTrace(e));
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
    }

    private UserToken addAuthentication(HttpServletRequest request, final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        try {
            if (isNull(usernamePasswordAuthenticationToken)) {
                throw new PreAuthenticatedCredentialsNotFoundException("");
            }

            final Object principal = usernamePasswordAuthenticationToken.getPrincipal();
            if (isNull(principal)) {
                throw new PrincipalNotFoundException("");
            }

            final String username = (String) principal;
            if (isBlank(username)) {
                throw new UsernameNotFoundException("");
            }

            final User user = userService.findByUsername(username);
            if (isNull(user)) {
                throw new UsernameNotFoundException("");
            }

            accountStatusUserDetailsCheck.check(user);
            final Map<String, Object> tokenMap = tokenHandlerService.generateToken(user);
            if (isEmpty(tokenMap)) {
                throw new EmptyJwtTokenException("");
            }
            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] JWT Token " + tokenMap.get("key") + " generated for user " + user);

            Token token = tokenService.findByToken(valueOf(tokenMap.get("key")));
            if (isNull(token)) {
                token = Token.getInstance();
                token.setIssueId(valueOf(tokenMap.get("issueId")));
                token.setToken(valueOf(tokenMap.get("key")));
                token.setExpirationDate((Date) tokenMap.get("expirationDate"));
                token.setIssuedDate((Date) tokenMap.get("issuedDate"));
                token = tokenService.save(token);
            }

            UserToken userToken = UserToken.getInstance();
            userToken.setUser(user);
            userToken.setToken(token);
            userToken.setStatus(ACTIVE);

            boolean saveUserToken = true;
            if (!isEmpty(userTokenService.findAllUserTokens(user))) {
                saveUserToken = userTokenService.updateTokenStatus(user);
            }

            if (!saveUserToken) {
                throw new AuthenticationServiceException("");
            }

            userToken = userTokenService.save(userToken);
            if (isNull(userToken)) {
                throw new AuthenticationServiceException("");
            }

            return userToken;
        } catch (JwtException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while adding authentication to user");
            log.error(getStackTrace(e));
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        } catch (AuthenticationException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while adding authentication to user");
            log.error(getStackTrace(e));
            throw e;
        } catch (Exception e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while adding authentication to user");
            log.error(getStackTrace(e));
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean removeAuthentication(final Authentication authentication, HttpServletRequest request) throws AuthenticationException {
        try {
            if (isNull(authentication)) {
                throw new PreAuthenticatedCredentialsNotFoundException("");
            }

            final Object principal = authentication.getPrincipal();
            if (isNull(principal)) {
                throw new PrincipalNotFoundException("");
            }

            final String username = (String) principal;
            if (isBlank(username)) {
                throw new UsernameNotFoundException("");
            }

            final User user = userService.findByUsername(username);
            if (isNull(user)) {
                throw new UsernameNotFoundException("");
            }

            accountStatusUserDetailsCheck.check(user);
            if (!isEmpty(userTokenService.findAllUserTokens(user))) {
                return userTokenService.updateTokenStatus(user);
            }
            return true;
        } catch (AuthenticationException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while invalidating all authentication token objects for user ");
            log.error(getStackTrace(e));
            throw e;
        } catch (Exception e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while invalidating all authentication token objects for user ");
            log.error(getStackTrace(e));
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
    }
}
