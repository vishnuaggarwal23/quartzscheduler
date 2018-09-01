package com.vishnu.aggarwal.rest.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.dto.ErrorResponseDTO;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndErrorResponseDTO;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndUserAuthenticationDTO;
import static java.lang.Boolean.FALSE;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/*
Created by vishnu on 20/4/18 1:21 PM
*/

/**
 * The type Login filter.
 */
@CommonsLog
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenAuthenticationService tokenAuthenticationService;
    private final BaseMessageResolver baseMessageResolver;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final Gson gson;

    /**
     * Instantiates a new Login filter.
     *
     * @param requestMatcher             the request matcher
     * @param tokenAuthenticationService the token authentication service
     * @param baseMessageResolver        the base message resolver
     * @param objectMapper               the object mapper
     * @param authenticationManager      the authentication manager
     * @param userService                the user service
     * @param gson
     */
    public LoginFilter(
            RequestMatcher requestMatcher,
            TokenAuthenticationService tokenAuthenticationService,
            BaseMessageResolver baseMessageResolver,
            ObjectMapper objectMapper,
            AuthenticationManager authenticationManager,
            UserService userService,
            Gson gson) {
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.baseMessageResolver = baseMessageResolver;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.gson = gson;
        this.setRequiresAuthenticationRequestMatcher(requestMatcher);
        this.setAuthenticationManager(this.authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Attempting User Authentication");
        return tokenAuthenticationService.getAuthenticationForLogin(request, response, authenticationManager);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        final Object principal = authResult.getPrincipal();
        User user = userService.findByUsername(principal.toString());
        final String xAuthToken = ((Token) authResult.getDetails()).getKey();
        getContext().setAuthentication(authResult);
        HashMap<String, UserAuthenticationDTO> responseMap = new HashMap<String, UserAuthenticationDTO>();
        responseMap.put(HASHMAP_USER_KEY, new UserAuthenticationDTO(
                new UserDTO
                        (
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                null,
                                user.getAccountExpired(),
                                user.getAccountLocked(),
                                user.getCredentialsExpired(),
                                user.getAccountEnabled(),
                                user.getIsDeleted(),
                                user.getFirstName(),
                                user.getLastName()
                        ),
                authResult.isAuthenticated(),
                xAuthToken
        ));
        response.getWriter().write(gson.toJson(responseMap, getHashMapOfStringAndUserAuthenticationDTO()));
        response.setCharacterEncoding("UTF-8");
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(SC_OK);
        response.addHeader(X_AUTH_TOKEN, xAuthToken);
        log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Successful Authentication");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        HttpSession session = request.getSession(FALSE);
        if (nonNull(session)) {
            session.invalidate();
        }
        getContext().setAuthentication(null);
        clearContext();
        response.setStatus(SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        HashMap<String, ErrorResponseDTO> responseMap = new HashMap<String, ErrorResponseDTO>();
        responseMap.put(HASHMAP_ERROR_KEY, new ErrorResponseDTO(
                request.getAttribute(CUSTOM_REQUEST_ID),
                new Date(),
                baseMessageResolver.getMessage(failed.getMessage(), failed.getMessage()),
                null
        ));
        response.getWriter().write(gson.toJson(responseMap, getHashMapOfStringAndErrorResponseDTO()));
        log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Unsuccessful Authentication");
    }
}
