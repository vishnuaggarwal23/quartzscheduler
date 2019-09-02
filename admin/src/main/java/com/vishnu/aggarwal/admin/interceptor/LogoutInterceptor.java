package com.vishnu.aggarwal.admin.interceptor;

/*
Created by vishnu on 16/4/18 10:19 AM
*/

import com.google.gson.Gson;
import com.vishnu.aggarwal.admin.exceptions.CookieNotFoundException;
import com.vishnu.aggarwal.admin.service.AuthenticationService;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
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

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndErrorResponseDTO;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndUserAuthenticationDTO;
import static java.util.Objects.isNull;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.springframework.web.util.WebUtils.getCookie;

@Component
@CommonsLog
public class LogoutInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    private final BaseMessageResolver baseMessageResolver;

    private final Gson gson;

    @Autowired
    public LogoutInterceptor(
            AuthenticationService authenticationService,
            BaseMessageResolver baseMessageResolver,
            Gson gson) {
        this.authenticationService = authenticationService;
        this.baseMessageResolver = baseMessageResolver;
        this.gson = gson;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
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
            final Object responseEntityObject;
//            responseEntity.getHeaders().toSingleValueMap().forEach(response::addHeader);

            if (isNotBlank(responseEntityBody)) {
                responseEntityObject = responseEntity.getStatusCode().is2xxSuccessful() ? gson.fromJson(responseEntityBody, getHashMapOfStringAndUserAuthenticationDTO()) : gson.fromJson(responseEntityBody, getHashMapOfStringAndErrorResponseDTO());
            } else {
                throw new RestClientException("");
            }

            if (responseEntityObject instanceof UserAuthenticationDTO) {
                if (((UserAuthenticationDTO) responseEntityObject).getIsAuthenticated()) {
                    return true;
                }
                throw new UserNotAuthenticatedException(baseMessageResolver.getMessage(""));
            }

            if (responseEntityObject instanceof ErrorResponseDTO) {
                throw new UserNotAuthenticatedException(baseMessageResolver.getMessage(""));
            }
            throw new RuntimeException("");
        } catch (RestClientException e) {
            return handleException(request, response, e, SC_INTERNAL_SERVER_ERROR, "/500");
        } catch (UserNotAuthenticatedException e) {
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
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 1L);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
    }
}
