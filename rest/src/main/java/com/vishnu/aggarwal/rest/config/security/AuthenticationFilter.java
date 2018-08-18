package com.vishnu.aggarwal.rest.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.persistence.NoResultException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
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
    private ObjectMapper objectMapper;
    private BaseMessageResolver baseMessageResolver;
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private RequestMatcher ignoredRequestMatcher;
    private RequestMatcher loginRequestMatcher;
    private RequestMatcher authenticationRequestMatcher;

    /**
     * Instantiates a new Authentication filter.
     *
     * @param tokenAuthenticationService   the token authentication service
     * @param objectMapper                 the object mapper
     * @param baseMessageResolver          the base message resolver
     * @param authenticationManager        the authentication manager
     * @param userService
     * @param ignoredRequestMatcher
     * @param loginRequestMatcher
     * @param authenticationRequestMatcher
     */
    public AuthenticationFilter(TokenAuthenticationService tokenAuthenticationService, ObjectMapper objectMapper, BaseMessageResolver baseMessageResolver, AuthenticationManager authenticationManager, UserService userService, RequestMatcher ignoredRequestMatcher, RequestMatcher loginRequestMatcher, RequestMatcher authenticationRequestMatcher) {
        super();
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.objectMapper = objectMapper;
        this.baseMessageResolver = baseMessageResolver;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.ignoredRequestMatcher = ignoredRequestMatcher;
        this.loginRequestMatcher = loginRequestMatcher;
        this.authenticationRequestMatcher = authenticationRequestMatcher;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(APPLICATION_JSON_UTF8_VALUE);

            if (!ignoredRequestMatcher.matches(httpServletRequest)) {
                if (loginRequestMatcher.matches(httpServletRequest)) {
                    chain.doFilter(httpServletRequest, response);
                } else {
                    Authentication authentication = tokenAuthenticationService.getAuthentication(httpServletRequest, (HttpServletResponse) response);
                    getContext().setAuthentication(authentication);

                    if (authenticationRequestMatcher.matches(httpServletRequest)) {
                        Assert.isTrue(authentication.isAuthenticated(), baseMessageResolver.getMessage("user.authentication.failed"));
                        final Object principal = authentication.getPrincipal();

                        Assert.notNull(principal, baseMessageResolver.getMessage("principal.not.found.in.authentication"));
                        User user = null;
                        if (principal instanceof String) {
                            user = userService.findByUsername(principal.toString());
                        } else if (principal instanceof User) {
                            user = (User) principal;
                        }

                        Assert.notNull(user, baseMessageResolver.getMessage("user.not.found.for.principal"));

                        ((HttpServletResponse) response).setStatus(SC_OK);
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
                                        authentication.isAuthenticated(),
                                        ((Token) authentication.getDetails()).getKey())
                                )
                        );
                    } else {
                        Assert.isTrue(authentication.isAuthenticated(), baseMessageResolver.getMessage("user.authentication.failed"));
                        chain.doFilter(httpServletRequest, response);
                    }
                }
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | LockedException | DisabledException | AccountExpiredException | CredentialsExpiredException | JsonProcessingException | NoResultException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while checking authentication.");
            log.error(getStackTrace(e));
            HttpSession session = httpServletRequest.getSession(FALSE);
            if (nonNull(session)) {
                session.invalidate();
            }
            getContext().setAuthentication(null);
            clearContext();
            ((HttpServletResponse) response).setStatus(SC_UNAUTHORIZED);
//            throw e;
        }
    }
}
