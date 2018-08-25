package com.vishnu.aggarwal.rest.config.security;

/*
Created by vishnu on 27/4/18 4:25 PM
*/

import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * The type Access denied handler.
 */
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    private final BaseMessageResolver baseMessageResolver;

    public AccessDeniedHandler(BaseMessageResolver baseMessageResolver) {
        this.baseMessageResolver = baseMessageResolver;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(FORBIDDEN.value());
        response.sendError(FORBIDDEN.value(), baseMessageResolver.getMessage(accessDeniedException.getMessage(), accessDeniedException.getMessage()));
    }
}
