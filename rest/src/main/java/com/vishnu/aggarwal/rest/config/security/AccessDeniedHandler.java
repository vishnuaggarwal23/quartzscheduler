package com.vishnu.aggarwal.rest.config.security;

/*
Created by vishnu on 27/4/18 4:25 PM
*/

import org.springframework.security.access.AccessDeniedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * The type Access denied handler.
 */
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(FORBIDDEN.value());
        response.sendError(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase());
    }
}
