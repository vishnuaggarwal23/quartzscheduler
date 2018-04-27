package com.vishnu.aggarwal.rest.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserToken;
import com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.lang.Boolean.FALSE;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/*
Created by vishnu on 20/4/18 1:21 PM
*/

@CommonsLog
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenAuthenticationService tokenAuthenticationService;
    private final BaseMessageResolver baseMessageResolver;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;

    public LoginFilter(RequestMatcher requestMatcher, TokenAuthenticationService tokenAuthenticationService, BaseMessageResolver baseMessageResolver, ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.baseMessageResolver = baseMessageResolver;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.setRequiresAuthenticationRequestMatcher(requestMatcher);
        this.setAuthenticationManager(this.authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("********************* [Request Interceptor Id "+request.getParameter(CUSTOM_REQUEST_ID)+"] Attempting Authentication **********************");
        try{
            Authentication authentication = tokenAuthenticationService.getAuthenticationForLogin(request, response, authenticationManager);
            if (isFalse(authentication.isAuthenticated())) {
                throw new AuthenticationServiceException(baseMessageResolver.getMessage("user.authentication.failed"));
            }
            return authentication;
        } catch (IOException e){
            log.error("*********** " + e.getClass() + " ***************");
            throw new InternalAuthenticationServiceException(baseMessageResolver.getMessage(""));
        } catch (Exception e){
            log.error("*********** " + e.getClass() + " ***************");
            throw e;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserToken userToken;
        Map<String, Object> responseWriteMap = new HashMap<String, Object>();
        userToken = tokenAuthenticationService.addAuthentication(response, (UsernamePasswordAuthenticationToken) authResult);
        if (nonNull(userToken)) {
            log.info("********************* [Request Interceptor Id "+request.getParameter(CUSTOM_REQUEST_ID)+"] Fetched UserToken " + userToken + " **********************");
            ((UsernamePasswordAuthenticationToken) authResult).setDetails(userToken.getToken());
            getContext().setAuthentication(authResult);
            User user = userToken.getUser();
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setAccountEnabled(user.getAccountEnabled());
            userDTO.setAccountLocked(user.getAccountLocked());
            userDTO.setAccountExpired(user.getAccountExpired());
            userDTO.setCredentialsExpired(user.getCredentialsExpired());
            responseWriteMap.put("isAuthenticated", authResult.isAuthenticated());
            responseWriteMap.put(X_AUTH_TOKEN, ((Token) authResult.getDetails()).getKey());
            responseWriteMap.put("user", userDTO);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(SC_OK);
            log.info("********************* [Request Interceptor Id "+request.getParameter(CUSTOM_REQUEST_ID)+"] Successful Authentication **********************");
        } else {
            responseWriteMap.put("isAuthenticated", FALSE);
            responseWriteMap.put(X_AUTH_TOKEN, null);
            response.setStatus(SC_UNAUTHORIZED);
            log.error("********************* [Request Interceptor Id "+request.getParameter(CUSTOM_REQUEST_ID)+"] Unsuccessful Authentication **********************");
        }
        response.getWriter().write(objectMapper.writeValueAsString(responseWriteMap));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> responseWriteMap = new HashMap<String, Object>();
        response.setStatus(SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        responseWriteMap.put("isAuthenticated", FALSE);
        responseWriteMap.put(X_AUTH_TOKEN, null);
        response.getWriter().write(objectMapper.writeValueAsString(responseWriteMap));

        log.error("********************* [Request Interceptor Id "+request.getParameter(CUSTOM_REQUEST_ID)+"] Unsuccessful Authentication **********************");
    }
}
