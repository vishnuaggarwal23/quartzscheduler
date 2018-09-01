package com.vishnu.aggarwal.admin.exceptions;

/*
Created by vishnu on 26/8/18 3:02 PM
*/

import com.google.gson.Gson;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * The type Mvc exception handler.
 */
@ControllerAdvice(basePackages = {"com.vishnu.aggarwal.admin.controller.web"})
@Controller
@CommonsLog
public class MvcExceptionHandler extends DefaultHandlerExceptionResolver {
    private final BaseMessageResolver baseMessageResolver;
    private final Gson gson;

    /**
     * Instantiates a new Mvc exception handler.
     *
     * @param baseMessageResolver the base message resolver
     * @param gson
     */
    @Autowired
    public MvcExceptionHandler(BaseMessageResolver baseMessageResolver, Gson gson) {
        this.baseMessageResolver = baseMessageResolver;
        this.gson = gson;
    }

    @Override
    protected ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setStatus(SC_NOT_FOUND);

        if (log.isErrorEnabled()) {
            log.error("[Request Interceptor Id : " + request.getAttribute(CUSTOM_REQUEST_ID) + "] " + ex.getMessage());
            log.error(getStackTrace(ex));
        }

        return new ModelAndView("error/404");
    }

    /**
     * Handle runtime exception model and view.
     *
     * @param exception  the exception
     * @param webRequest the web request
     * @return the model and view
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public final ModelAndView handleRuntimeException(RuntimeException exception, WebRequest webRequest) {
        HttpServletRequest httpServletRequest = httpServletRequest(webRequest).orElse(null);
        Object customInterceptorId = nonNull(httpServletRequest) ? httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) : null;
        String exceptionMessage = exception.getMessage();
        if (nonNull(httpServletRequest)) {
            httpServletRequest.setAttribute("javax.servlet.error.exception", exception);
        }

        HttpServletResponse httpServletResponse = httpServletResponse(webRequest).orElse(null);
        if (nonNull(httpServletResponse)) {
            httpServletResponse.setStatus(SC_INTERNAL_SERVER_ERROR);
        }

        if (log.isErrorEnabled()) {
            log.error("[Request Interceptor Id : " + customInterceptorId + "] " + exceptionMessage);
            log.error(getStackTrace(exception));
        }

        return new ModelAndView("error/500");
    }

    private Optional<HttpServletRequest> httpServletRequest(WebRequest webRequest) {
        HttpServletRequest httpServletRequest = null;
        if (webRequest instanceof ServletWebRequest) {
            httpServletRequest = ((ServletWebRequest) webRequest).getRequest();
        }
        return Optional.ofNullable(httpServletRequest);
    }

    private Optional<HttpServletResponse> httpServletResponse(WebRequest webRequest) {
        HttpServletResponse httpServletResponse = null;
        if (webRequest instanceof ServletWebRequest) {
            httpServletResponse = ((ServletWebRequest) webRequest).getResponse();
        }
        return Optional.ofNullable(httpServletResponse);
    }
}
