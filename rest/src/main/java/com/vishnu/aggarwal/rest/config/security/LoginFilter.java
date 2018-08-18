package com.vishnu.aggarwal.rest.config.security;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.lang.Boolean.FALSE;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.util.Assert.notNull;

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

    /**
     * Instantiates a new Login filter.
     *
     * @param requestMatcher             the request matcher
     * @param tokenAuthenticationService the token authentication service
     * @param baseMessageResolver        the base message resolver
     * @param objectMapper               the object mapper
     * @param authenticationManager      the authentication manager
     * @param userService                the user service
     */
    public LoginFilter(
            RequestMatcher requestMatcher,
            TokenAuthenticationService tokenAuthenticationService,
            BaseMessageResolver baseMessageResolver,
            ObjectMapper objectMapper,
            AuthenticationManager authenticationManager,
            UserService userService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.baseMessageResolver = baseMessageResolver;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.setRequiresAuthenticationRequestMatcher(requestMatcher);
        this.setAuthenticationManager(this.authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Attempting User Authentication");
            return tokenAuthenticationService.getAuthenticationForLogin(request, response, authenticationManager);
        } catch (JsonParseException | JsonMappingException | IllegalArgumentException | HibernateException | LockedException | DisabledException | AccountExpiredException | CredentialsExpiredException | IllegalStateException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] User authentication failed.");
            log.error(getStackTrace(e));
            throw new AuthenticationServiceException(baseMessageResolver.getMessage("user.authentication.failed"));
        } catch (IOException | AuthenticationException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] User authentication failed.");
            log.error(getStackTrace(e));
            throw new AuthenticationServiceException(baseMessageResolver.getMessage("user.authentication.failed"));
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(APPLICATION_JSON_UTF8_VALUE);

            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Fetched UserToken " + authResult.getDetails());

            final Object principal = authResult.getPrincipal();

            notNull(principal, baseMessageResolver.getMessage("principal.not.found.in.authentication"));
            User user = null;
            if (principal instanceof String) {
                user = userService.findByUsername(principal.toString());
            } else if (principal instanceof User) {
                user = (User) principal;
            }

            notNull(user, baseMessageResolver.getMessage("user.not.found.for.principal"));

            getContext().setAuthentication(authResult);
            response.getWriter().write(objectMapper.writeValueAsString(
                    new UserAuthenticationDTO(new UserDTO
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
                            ((Token) authResult.getDetails()).getKey())
                    )
            );
            response.setStatus(SC_OK);
            response.addHeader(X_AUTH_TOKEN, ((Token) authResult.getDetails()).getKey());
            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Successful Authentication");
        } catch (IllegalArgumentException | HibernateException | JsonProcessingException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while Successful Authentication");
            log.error(getStackTrace(e));
            response.setStatus(SC_UNAUTHORIZED);
            throw e;
        } catch (IOException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while Successful Authentication");
            log.error(getStackTrace(e));
            response.setStatus(SC_UNAUTHORIZED);
            throw e;
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        try {
            response.setStatus(SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType(APPLICATION_JSON_UTF8_VALUE);
            HttpSession session = request.getSession(FALSE);
            if (nonNull(session)) {
                session.invalidate();
            }
            getContext().setAuthentication(null);
            clearContext();
            log.info("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Unsuccessful Authentication");
        } catch (IllegalStateException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while Unsuccessful Authentication");
            throw e;
        }
    }
}
