package com.vishnu.aggarwal.admin.interceptor;

import com.vishnu.aggarwal.admin.service.AuthenticationService;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.exceptions.RestServiceCallException;
import com.vishnu.aggarwal.core.exceptions.UserNotAuthenticatedException;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.USER_LOGGED_IN;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.springframework.web.util.WebUtils.getCookie;

/*
Created by vishnu on 28/3/18 11:10 AM
*/

/**
 * The type Authentication interceptor.
 */
@Component
@CommonsLog
public class AuthenticationInterceptor implements HandlerInterceptor {

    /**
     * The Authentication service.
     */
    @Autowired
    AuthenticationService authenticationService;

    /**
     * The Base message resolver.
     */
    @Autowired
    BaseMessageResolver baseMessageResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie = getCookie(request, X_AUTH_TOKEN);

        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");

        try {
            RestResponseVO<Boolean> restResponseVO = authenticationService.isAuthenticatedUser(cookie);
            List<String> loginPageUris = Arrays.asList(request.getContextPath() + "/web", request.getContextPath() + "/web/");

            if (nonNull(cookie) && nonNull(restResponseVO) && isTrue(restResponseVO.getData())) {
                if (loginPageUris.contains(request.getRequestURI())) {
                    response.sendRedirect(request.getContextPath() + "/web/user/dashboard");
                    return true;
                } else {
                    return true;
                }

            } else {
                if (loginPageUris.contains(request.getRequestURI())) {
                    response.sendRedirect(request.getContextPath() + "/web");
                    return true;
                } else {
                    throw new UserNotAuthenticatedException(baseMessageResolver.getMessage(""));
                }
            }
        } catch (RestServiceCallException | UserNotAuthenticatedException e) {
            log.error("************** Error while authenticating user *************** \n", e);
            response.setStatus(SC_UNAUTHORIZED);
            response.sendRedirect(request.getContextPath() + "/web");
            return false;
        } catch (Exception e) {
            log.error("************** Error while authenticating user *************** \n", e);
            response.setStatus(SC_UNAUTHORIZED);
            response.sendRedirect(request.getContextPath() + "/web");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
        modelAndView.getModelMap().addAttribute(USER_LOGGED_IN, response.getStatus() != SC_UNAUTHORIZED);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
    }
}
