package com.vishnu.aggarwal.admin.interceptor;

/*
Created by vishnu on 16/4/18 10:19 AM
*/

import com.vishnu.aggarwal.admin.service.AuthenticationService;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.springframework.web.util.WebUtils.getCookie;

@Component
@CommonsLog
public class LogoutInterceptor implements HandlerInterceptor {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    BaseMessageResolver baseMessageResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");

        Cookie xAuthTokenCookie = getCookie(request, X_AUTH_TOKEN);
        if (nonNull(xAuthTokenCookie)) {
            RestResponseVO<Boolean> restResponseVO = authenticationService.isAuthenticatedUser(xAuthTokenCookie);
            return nonNull(restResponseVO) && isTrue(restResponseVO.getData());
        }
        response.setStatus(SC_UNAUTHORIZED);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
    }
}
