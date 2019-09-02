package com.vishnu.aggarwal.rest.config.security;

/*
Created by vishnu on 21/4/18 5:41 PM
*/

import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService;
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

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@CommonsLog
public class LogoutFilter extends org.springframework.security.web.authentication.logout.LogoutFilter {

    private TokenAuthenticationService tokenAuthenticationService;
    private LogoutHandler logoutHandler;
    private LogoutSuccessHandler logoutSuccessHandler;

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
            log.error("Exception occurred", getRootCause(e));
            throw new IOException(e.getMessage(), e);
        } catch (IOException | ServletException e) {
            log.error("Exception occurred", getRootCause(e));
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred", getRootCause(e));
            throw new IOException(e.getMessage(), e);
        }
    }
}
