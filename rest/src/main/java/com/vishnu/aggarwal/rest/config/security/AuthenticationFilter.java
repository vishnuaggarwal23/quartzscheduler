package com.vishnu.aggarwal.rest.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.rest.service.interfaces.TokenAuthenticationService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.lang.Boolean.FALSE;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/*
Created by vishnu on 19/4/18 4:13 PM
*/

@CommonsLog
public class AuthenticationFilter extends GenericFilterBean {

    private TokenAuthenticationService tokenAuthenticationService;
    private ObjectMapper objectMapper;
    private BaseMessageResolver baseMessageResolver;
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(TokenAuthenticationService tokenAuthenticationService, ObjectMapper objectMapper, BaseMessageResolver baseMessageResolver, AuthenticationManager authenticationManager) {
        super();
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.objectMapper = objectMapper;
        this.baseMessageResolver = baseMessageResolver;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setCharacterEncoding("UTF-8");

        if(httpServletRequest.getRequestURI().contains("/user/login")){
            chain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            Authentication authentication = tokenAuthenticationService.getAuthentication(httpServletRequest, httpServletResponse);
            getContext().setAuthentication(authentication);

            if (authentication.isAuthenticated()) {
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                Map<String, Object> responseWriteMap = new HashMap<String, Object>();
                responseWriteMap.put("code", httpServletResponse.getStatus());
                responseWriteMap.put("message", baseMessageResolver.getMessage("user.authentication.failed"));
                responseWriteMap.put("authenticated", FALSE);
                responseWriteMap.put("path", httpServletRequest.getRequestURI());
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(responseWriteMap));
            }
        }
    }
}
