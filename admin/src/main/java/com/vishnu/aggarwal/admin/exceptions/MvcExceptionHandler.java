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

import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice(basePackages = {"com.vishnu.aggarwal.admin.controller.web"})
@Controller
@CommonsLog
public class MvcExceptionHandler extends DefaultHandlerExceptionResolver {
    private final BaseMessageResolver baseMessageResolver;
    private final Gson gson;

    @Autowired
    public MvcExceptionHandler(BaseMessageResolver baseMessageResolver, Gson gson) {
        this.baseMessageResolver = baseMessageResolver;
        this.gson = gson;
    }

    @Override
    protected ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setStatus(SC_NOT_FOUND);

        if (log.isErrorEnabled()) {
            log.error("Exception occurred", getRootCause(ex));
        }

        return new ModelAndView("error/404");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public final ModelAndView handleRuntimeException(RuntimeException exception, WebRequest webRequest) {
        String exceptionMessage = exception.getMessage();
        HttpServletRequest httpServletRequest = httpServletRequest(webRequest).orElse(null);
        if (nonNull(httpServletRequest)) {
            httpServletRequest.setAttribute("javax.servlet.error.exception", exception);
        }

        HttpServletResponse httpServletResponse = httpServletResponse(webRequest).orElse(null);
        if (nonNull(httpServletResponse)) {
            httpServletResponse.setStatus(SC_INTERNAL_SERVER_ERROR);
        }

        if (log.isErrorEnabled()) {
            log.error("Exception occurred", getRootCause(exception));
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
