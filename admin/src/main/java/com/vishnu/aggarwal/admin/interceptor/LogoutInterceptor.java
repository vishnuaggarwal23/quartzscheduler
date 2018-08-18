package com.vishnu.aggarwal.admin.interceptor;

/*
Created by vishnu on 16/4/18 10:19 AM
*/

import com.vishnu.aggarwal.admin.service.AuthenticationService;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.web.util.WebUtils.getCookie;

/**
 * The type Logout interceptor.
 */
@Component
@CommonsLog
public class LogoutInterceptor implements HandlerInterceptor {

    /**
     * The Authentication service.
     */
    private final AuthenticationService authenticationService;

    private final BaseMessageResolver baseMessageResolver;

    @Autowired
    public LogoutInterceptor(
            AuthenticationService authenticationService,
            BaseMessageResolver baseMessageResolver) {
        this.authenticationService = authenticationService;
        this.baseMessageResolver = baseMessageResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");

        Cookie cookie = getCookie(request, X_AUTH_TOKEN);

        ResponseEntity<UserAuthenticationDTO> userAuthenticationDTOResponseEntity = nonNull(cookie) && isNotBlank(cookie.getValue()) ? authenticationService.isAuthenticatedUser(cookie) : null;
        return nonNull(userAuthenticationDTOResponseEntity) && isTrue(userAuthenticationDTOResponseEntity.hasBody()) && isTrue(userAuthenticationDTOResponseEntity.getBody().getIsAuthenticated());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
    }
}
