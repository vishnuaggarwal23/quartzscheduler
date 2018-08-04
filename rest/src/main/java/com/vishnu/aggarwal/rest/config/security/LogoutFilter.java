package com.vishnu.aggarwal.rest.config.security;

/*
Created by vishnu on 21/4/18 5:41 PM
*/

import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.authentication.*;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.persistence.NoResultException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.util.Optional.ofNullable;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.util.Assert.isTrue;

/**
 * The type Logout filter.
 */
@CommonsLog
public class LogoutFilter extends org.springframework.security.web.authentication.logout.LogoutFilter {

    private TokenAuthenticationService tokenAuthenticationService;
    private LogoutHandler logoutHandler;
    private LogoutSuccessHandler logoutSuccessHandler;
    private String logoutUrl;
    private BaseMessageResolver baseMessageResolver;

    /**
     * Instantiates a new Logout filter.
     *
     * @param logoutUrl                  the logout url
     * @param logoutSuccessHandler       the logout success handler
     * @param requestMatcher             the request matcher
     * @param tokenAuthenticationService the token authentication service
     * @param logoutHandler              the logout handler
     * @param baseMessageResolver
     */
    public LogoutFilter(String logoutUrl, LogoutSuccessHandler logoutSuccessHandler, RequestMatcher requestMatcher, TokenAuthenticationService tokenAuthenticationService, LogoutHandler logoutHandler, BaseMessageResolver baseMessageResolver) {
        super(logoutSuccessHandler, logoutHandler);
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.logoutHandler = logoutHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.baseMessageResolver = baseMessageResolver;
        super.setFilterProcessesUrl(logoutUrl);
        super.setLogoutRequestMatcher(requestMatcher);
        this.logoutUrl = logoutUrl;
    }

    /**
     * Instantiates a new Logout filter.
     *
     * @param logoutSuccessUrl    the logout success url
     * @param baseMessageResolver
     * @param handlers            the handlers
     */
    @Deprecated
    public LogoutFilter(String logoutSuccessUrl, BaseMessageResolver baseMessageResolver, LogoutHandler... handlers) {
        super(logoutSuccessUrl, handlers);
        this.baseMessageResolver = baseMessageResolver;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            if (super.requiresLogout(request, response)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) ofNullable(getContext().getAuthentication()).orElse((tokenAuthenticationService.getAuthentication(request, response)));
                isTrue(usernamePasswordAuthenticationToken.isAuthenticated(), baseMessageResolver.getMessage(""));

                this.logoutHandler.logout(request, response, usernamePasswordAuthenticationToken);
                isTrue(response.getStatus() == SC_OK, baseMessageResolver.getMessage(""));

                new CookieClearingLogoutHandler("JSESSIONID", X_AUTH_TOKEN, "localhost").logout(request, response, usernamePasswordAuthenticationToken);
                this.logoutSuccessHandler.onLogoutSuccess(request, response, usernamePasswordAuthenticationToken);
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | LockedException | DisabledException | AccountExpiredException | CredentialsExpiredException | NoResultException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while logging out");
            log.error(getStackTrace(e));
            throw e;
        } catch (IOException | ServletException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while logging out");
            log.error(getStackTrace(e));
            throw e;
        } finally {
            chain.doFilter(request, response);
        }
    }
}
