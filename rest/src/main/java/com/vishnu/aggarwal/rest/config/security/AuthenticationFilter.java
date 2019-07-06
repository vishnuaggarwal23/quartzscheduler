package com.vishnu.aggarwal.rest.config.security;

import com.google.gson.Gson;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.dto.ErrorResponseDTO;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.exceptions.UserNotAuthenticatedException;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndErrorResponseDTO;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndUserAuthenticationDTO;
import static com.vishnu.aggarwal.rest.util.DTOConversion.convertFromUser;
import static java.lang.Boolean.FALSE;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/*
Created by vishnu on 19/4/18 4:13 PM
*/

/**
 * The type Authentication filter.
 */
@CommonsLog
public class AuthenticationFilter extends GenericFilterBean {

    private TokenAuthenticationService tokenAuthenticationService;
    private BaseMessageResolver baseMessageResolver;
    private UserService userService;
    private RequestMatcher[] ignoredRequestMatcher;
    private RequestMatcher loginRequestMatcher;
    private RequestMatcher authenticationRequestMatcher;
    private Gson gson;

    /**
     * Instantiates a new Authentication filter.
     *
     * @param tokenAuthenticationService   the token authentication service
     * @param baseMessageResolver          the base message resolver
     * @param userService                  the user service
     * @param ignoredRequestMatcher        the ignored request matcher
     * @param loginRequestMatcher          the login request matcher
     * @param authenticationRequestMatcher the authentication request matcher
     * @param gson                         the gson
     */
    public AuthenticationFilter(
            TokenAuthenticationService tokenAuthenticationService,
            BaseMessageResolver baseMessageResolver,
            UserService userService,
            RequestMatcher loginRequestMatcher,
            RequestMatcher authenticationRequestMatcher,
            Gson gson,
            RequestMatcher... ignoredRequestMatcher
    ) {
        super();
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.baseMessageResolver = baseMessageResolver;
        this.userService = userService;
        this.ignoredRequestMatcher = ignoredRequestMatcher;
        this.loginRequestMatcher = loginRequestMatcher;
        this.authenticationRequestMatcher = authenticationRequestMatcher;
        this.gson = gson;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(APPLICATION_JSON_UTF8_VALUE);

            if (Arrays.stream(ignoredRequestMatcher).filter(it -> it.matches(httpServletRequest)).toArray().length <= 0) {
                if (loginRequestMatcher.matches(httpServletRequest)) {
                    chain.doFilter(httpServletRequest, response);
                } else {
                    Authentication authentication = tokenAuthenticationService.getAuthentication(httpServletRequest, (HttpServletResponse) response);
                    if (authentication.isAuthenticated()) {
                        getContext().setAuthentication(authentication);
                        if (authenticationRequestMatcher.matches(httpServletRequest)) {
                            final User user = userService.findByUsername(authentication.getPrincipal().toString());
                            HashMap<String, UserAuthenticationDTO> responseMap = new HashMap<String, UserAuthenticationDTO>();
                            responseMap.put(HASHMAP_USER_KEY, new UserAuthenticationDTO(
                                    convertFromUser(user),
                                    authentication.isAuthenticated(),
                                    ((Token) authentication.getDetails()).getKey()
                            ));

                            ((HttpServletResponse) response).setStatus(SC_OK);
                            response.getWriter().write(gson.toJson(responseMap, getHashMapOfStringAndUserAuthenticationDTO()));
                        } else {
                            chain.doFilter(httpServletRequest, response);
                        }
                    } else {
                        throw new UserNotAuthenticatedException("");
                    }
                }
            }
        } catch (AuthenticationException e) {
            handleException(request, (HttpServletResponse) response, httpServletRequest, e);
//            throw new IOException(e.getMessage(), e);
        } catch (IOException | ServletException e) {
            handleException(request, (HttpServletResponse) response, httpServletRequest, e);
//            throw e;
        } catch (Exception e) {
            handleException(request, (HttpServletResponse) response, httpServletRequest, e);
//            throw new IOException(e.getMessage(), e);
        }
    }

    private void handleException(ServletRequest request, HttpServletResponse response, HttpServletRequest httpServletRequest, Exception e) throws IOException {
        log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while checking authentication.");
        log.error(getStackTrace(e));
        HttpSession session = httpServletRequest.getSession(FALSE);
        if (nonNull(session)) {
            session.invalidate();
        }
        getContext().setAuthentication(null);
        clearContext();

        HashMap<String, ErrorResponseDTO> responseMap = new HashMap<String, ErrorResponseDTO>();
        responseMap.put(HASHMAP_ERROR_KEY, new ErrorResponseDTO(
                request.getAttribute(CUSTOM_REQUEST_ID),
                new Date(),
                baseMessageResolver.getMessage(e.getMessage(), e.getMessage()),
                null
        ));
        response.getWriter().write(gson.toJson(responseMap, getHashMapOfStringAndErrorResponseDTO()));
        response.setCharacterEncoding("UTF-8");
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(SC_UNAUTHORIZED);
//        response.sendError(SC_UNAUTHORIZED, baseMessageResolver.getMessage(e.getMessage(), e.getMessage()));
    }
}
