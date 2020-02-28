package com.vishnu.aggarwal.quartz.rest.config.security;

/*
Created by vishnu on 21/4/18 5:41 PM
*/

import com.vishnu.aggarwal.quartz.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.quartz.rest.interfaces.TokenAuthenticationService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * The type Logout filter.
 */
@CommonsLog
public class LogoutFilter extends org.springframework.security.web.authentication.logout.LogoutFilter {

    private TokenAuthenticationService tokenAuthenticationService;
    private LogoutHandler logoutHandler;
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * Instantiates a new Logout filter.
     *
     * @param logoutUrl                  the logout url
     * @param logoutSuccessHandler       the logout success handler
     * @param requestMatcher             the request matcher
     * @param tokenAuthenticationService the token authentication service
     * @param logoutHandler              the logout handler
     */
    public LogoutFilter(
            String logoutUrl,
            LogoutSuccessHandler logoutSuccessHandler,
            RequestMatcher requestMatcher,
            TokenAuthenticationService tokenAuthenticationService,
            LogoutHandler logoutHandler) {
        super(logoutSuccessHandler, logoutHandler);
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.logoutHandler = logoutHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        super.setFilterProcessesUrl(logoutUrl);
        super.setLogoutRequestMatcher(requestMatcher);
    }

    /**
     * Instantiates a new Logout filter.
     *
     * @param logoutSuccessUrl    the logout success url
     * @param baseMessageResolver the base message resolver
     * @param handlers            the handlers
     */
    @Deprecated
    public LogoutFilter(String logoutSuccessUrl, BaseMessageResolver baseMessageResolver, LogoutHandler... handlers) {
        super(logoutSuccessUrl, handlers);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        try {
            if (super.requiresLogout(request, response)) {
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) ofNullable(getContext().getAuthentication()).orElse((tokenAuthenticationService.getAuthentication(request, response)));
                this.logoutHandler.logout(request, response, usernamePasswordAuthenticationToken);
                new CookieClearingLogoutHandler("JSESSIONID", X_AUTH_TOKEN, "localhost").logout(request, response, usernamePasswordAuthenticationToken);
                this.logoutSuccessHandler.onLogoutSuccess(request, response, usernamePasswordAuthenticationToken);
            }
            chain.doFilter(request, response);
        } catch (AuthenticationException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while logging out");
            log.error(getStackTrace(e));
            throw new IOException(e.getMessage(), e);
        } catch (IOException | ServletException e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while logging out");
            log.error(getStackTrace(e));
            throw e;
        } catch (Exception e) {
            log.error("[Request Interceptor Id " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while logging out");
            log.error(getStackTrace(e));
            throw new IOException(e.getMessage(), e);
        }
    }
}
