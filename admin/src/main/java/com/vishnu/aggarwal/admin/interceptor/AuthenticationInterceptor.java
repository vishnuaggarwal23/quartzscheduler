package com.vishnu.aggarwal.admin.interceptor;

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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.BASE_URI;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndErrorResponseDTO;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndUserAuthenticationDTO;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.springframework.web.util.WebUtils.getCookie;

/*
Created by vishnu on 28/3/18 11:10 AM
*/

@Component
@CommonsLog
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    private final BaseMessageResolver baseMessageResolver;

    private final Gson gson;

    @Autowired
    public AuthenticationInterceptor(
            AuthenticationService authenticationService,
            BaseMessageResolver baseMessageResolver,
            Gson gson) {
        this.authenticationService = authenticationService;
        this.baseMessageResolver = baseMessageResolver;
        this.gson = gson;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");

        try {
            final Cookie cookie = getCookie(request, X_AUTH_TOKEN);
            if (isNull(cookie) || isBlank(cookie.getValue())) {
                throw new CookieNotFoundException("");
            }

            final ResponseEntity<String> responseEntity = authenticationService.isAuthenticatedUser(cookie);
            final String responseEntityBody = responseEntity.getBody();
            if (isBlank(responseEntityBody)) {
                throw new RestClientException("");
            }

//            responseEntity.getHeaders().toSingleValueMap().forEach(response::addHeader);

            final HashMap<String, Object> responseEntityObject = responseEntity.getStatusCode().is2xxSuccessful() ? gson.fromJson(responseEntityBody, getHashMapOfStringAndUserAuthenticationDTO()) : gson.fromJson(responseEntityBody, getHashMapOfStringAndErrorResponseDTO());

            final List<String> loginPageUris = asList(format("%s%s%s%s", request.getContextPath(), BASE_URI, User.BASE_URI, User.USER_LOGIN_1), format("%s%s%s%s", request.getContextPath(), BASE_URI, User.BASE_URI, User.USER_LOGIN_2));
            if (responseEntityObject.containsKey(HASHMAP_USER_KEY) && responseEntityObject.get(HASHMAP_USER_KEY) instanceof UserAuthenticationDTO) {
                if (((UserAuthenticationDTO) responseEntityObject.get(HASHMAP_USER_KEY)).getIsAuthenticated()) {
                    if (loginPageUris.contains(request.getRequestURI())) {
                        response.sendRedirect(request.getContextPath() + "/web/user/dashboard");
                    }
                    return true;
                }
                throw new UserNotAuthenticatedException(baseMessageResolver.getMessage(""));
            }
            if (responseEntityObject.containsKey(HASHMAP_ERROR_KEY) && responseEntityObject.get(HASHMAP_ERROR_KEY) instanceof ErrorResponseDTO) {
                if (loginPageUris.contains(request.getRequestURI())) {
                    log.error(responseEntityObject.get(HASHMAP_ERROR_KEY).toString());
                    response.sendRedirect(format("%s%s%s%s", request.getContextPath(), BASE_URI, User.BASE_URI, User.USER_LOGIN_1));
                    return true;
                }
                throw new UserNotAuthenticatedException(baseMessageResolver.getMessage(""));
            }
            throw new RuntimeException("");
        } catch (RestClientException e) {
            return handleException(request, response, e, SC_INTERNAL_SERVER_ERROR, "/500");
        } catch (CookieNotFoundException | UserNotAuthenticatedException e) {
            return handleException(request, response, e, SC_UNAUTHORIZED, "/401");
        } catch (Exception e) {
            return handleException(request, response, e, SC_INTERNAL_SERVER_ERROR, "/500");
        }
    }

    private boolean handleException(final HttpServletRequest request, HttpServletResponse response, final Exception e, final Integer httpStatueCode, final String redirectUri) throws IOException {
        log.error("Exception occurred", getRootCause(e));
        response.setStatus(httpStatueCode);
        response.sendRedirect(request.getContextPath() + redirectUri);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
        if (nonNull(modelAndView)) {
            modelAndView.getModelMap().addAttribute(USER_LOGGED_IN, response.getStatus() != SC_UNAUTHORIZED);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
    }
}
