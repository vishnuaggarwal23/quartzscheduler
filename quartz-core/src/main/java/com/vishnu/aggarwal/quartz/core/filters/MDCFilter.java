package com.vishnu.aggarwal.quartz.core.filters;

import lombok.Data;
import lombok.EqualsAndHashCode;
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

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.MDC_TOKEN;
import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.web.util.WebUtils.getCookie;

/*
Created by vishnu on 6/7/19 4:56 PM
*/

@Component
@Data
@EqualsAndHashCode(callSuper = false)
@Order(2)
public class MDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token;
            if (isNotEmpty(X_AUTH_TOKEN) && isNotEmpty(request.getHeader(X_AUTH_TOKEN))) {
                token = request.getHeader(X_AUTH_TOKEN);
            } else if (nonNull(getCookie(request, X_AUTH_TOKEN)) && nonNull(getCookie(request, X_AUTH_TOKEN).getValue()) && isNotEmpty(getCookie(request, X_AUTH_TOKEN).getValue())) {
                token = getCookie(request, X_AUTH_TOKEN).getValue();
            } else {
                token = UUID.randomUUID().toString();
            }
            MDC.put(MDC_TOKEN, token);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_TOKEN);
        }
    }
}
