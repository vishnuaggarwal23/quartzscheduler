package com.vishnu.aggarwal.admin.interceptor;

/*
Created by vishnu on 14/4/18 5:11 PM
*/

import com.google.gson.Gson;
import com.vishnu.aggarwal.admin.exceptions.CookieNotFoundException;
import com.vishnu.aggarwal.admin.service.AuthenticationService;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.User;
import com.vishnu.aggarwal.core.dto.ErrorResponseDTO;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.exceptions.UserNotAuthenticatedException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.BASE_URI;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndErrorResponseDTO;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndUserAuthenticationDTO;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.web.util.WebUtils.getCookie;

/**
 * The type Login interceptor.
 */
@Component
@CommonsLog
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * The Authentication service.
     */
    private final AuthenticationService authenticationService;

    /**
     * The Base message resolver.
     */
    private final BaseMessageResolver baseMessageResolver;

    private final Gson gson;

    @Autowired
    public LoginInterceptor(
            AuthenticationService authenticationService,
            BaseMessageResolver baseMessageResolver,
            Gson gson) {
        this.authenticationService = authenticationService;
        this.baseMessageResolver = baseMessageResolver;
        this.gson = gson;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");

        try {
            final Cookie cookie = getCookie(request, X_AUTH_TOKEN);
            if (isNull(cookie)) {
                throw new CookieNotFoundException("");
            }
            if (isBlank(cookie.getValue())) {
                throw new CookieNotFoundException("");
            }

            final ResponseEntity<String> responseEntity = authenticationService.isAuthenticatedUser(cookie);
            final String responseEntityBody = responseEntity.getBody();
//            responseEntity.getHeaders().toSingleValueMap().forEach(response::addHeader);

            final HashMap<String, Object> responseEntityObject;
            if (isNotBlank(responseEntityBody)) {
                responseEntityObject = responseEntity.getStatusCode().is2xxSuccessful() ?
                        gson.fromJson(responseEntityBody, getHashMapOfStringAndUserAuthenticationDTO()) :
                        gson.fromJson(responseEntityBody, getHashMapOfStringAndErrorResponseDTO());
            } else {
                throw new RestClientException("");
            }

            if (responseEntityObject.containsKey(HASHMAP_USER_KEY) && responseEntityObject.get(HASHMAP_USER_KEY) instanceof UserAuthenticationDTO) {
                if (((UserAuthenticationDTO) responseEntityObject.get(HASHMAP_USER_KEY)).getIsAuthenticated()) {
                    response.sendRedirect(request.getContextPath() + BASE_URI + User.BASE_URI + User.USER_DASHBOARD);
                }
                throw new UserNotAuthenticatedException(baseMessageResolver.getMessage(""));
            }
            if (responseEntityObject.containsKey(HASHMAP_ERROR_KEY) && responseEntityObject.get(HASHMAP_ERROR_KEY) instanceof ErrorResponseDTO) {
                log.error(responseEntityObject.get(HASHMAP_ERROR_KEY).toString());
                throw new UserNotAuthenticatedException(baseMessageResolver.getMessage(""));
            }
            throw new RuntimeException("");
        } catch (Exception e) {
            log.error("[Request ID " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while authenticating user");
            log.error(getStackTrace(e));
        }
        return true;
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
