package com.vishnu.aggarwal.core.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.web.util.WebUtils.getCookie;

/*
Created by vishnu on 6/7/19 4:56 PM
*/

@Component
@Data
@EqualsAndHashCode(callSuper = false)
@Order(HIGHEST_PRECEDENCE)
public class MDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String randomId;
            final String token;
            if (isNotEmpty(request.getHeader(X_AUTH_TOKEN))) {
                token = request.getHeader(X_AUTH_TOKEN);
            } else if (nonNull(getCookie(request, X_AUTH_TOKEN)) && nonNull(getCookie(request, X_AUTH_TOKEN).getValue()) && isNotEmpty(getCookie(request, X_AUTH_TOKEN).getValue())) {
                token = getCookie(request, X_AUTH_TOKEN).getValue();
            } else {
                token = UUID.randomUUID().toString();
            }

            if (isNotEmpty(request.getHeader(CUSTOM_REQUEST_ID))) {
                randomId = request.getHeader(X_AUTH_TOKEN);
            } else if (nonNull(getCookie(request, CUSTOM_REQUEST_ID)) && nonNull(getCookie(request, CUSTOM_REQUEST_ID).getValue()) && isNotEmpty(getCookie(request, CUSTOM_REQUEST_ID).getValue())) {
                randomId = getCookie(request, CUSTOM_REQUEST_ID).getValue();
            } else if (request.getParameter(CUSTOM_REQUEST_ID) != null) {
                randomId = request.getParameter(CUSTOM_REQUEST_ID);
            } else if (request.getAttribute(CUSTOM_REQUEST_ID) != null) {
                randomId = request.getAttribute(CUSTOM_REQUEST_ID).toString();
            } else {
                randomId = UUID.randomUUID().toString();
            }

            request.setAttribute(CUSTOM_REQUEST_ID, randomId);
            MDC.put(X_AUTH_TOKEN, token);
            MDC.put(CUSTOM_REQUEST_ID, randomId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(X_AUTH_TOKEN);
            MDC.remove(CUSTOM_REQUEST_ID);
        }
    }
}
