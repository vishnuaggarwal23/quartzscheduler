package com.vishnu.aggarwal.quartz.rest.exception;

import com.google.gson.Gson;
import com.vishnu.aggarwal.quartz.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.quartz.core.dto.ErrorResponseDTO;
import com.vishnu.aggarwal.quartz.core.exceptions.InvalidRequestException;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.SchedulerException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.HASHMAP_ERROR_KEY;
import static com.vishnu.aggarwal.quartz.core.util.TypeTokenUtils.getHashMapOfStringAndErrorResponseDTO;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.util.CollectionUtils.isEmpty;

/*
Created by vishnu on 18/8/18 10:44 PM
*/

@RestController
@RestControllerAdvice
@CommonsLog
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final BaseMessageResolver baseMessageResolver;
    private final Gson gson;

    @Autowired
    public RestExceptionHandler(
            BaseMessageResolver baseMessageResolver,
            Gson gson) {
        this.baseMessageResolver = baseMessageResolver;
        this.gson = gson;
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
        }

        return getObjectResponseEntity(ex, headers, status, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }

        return getObjectResponseEntity(ex, headers, status, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex, headers, status, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parameter", ex.getParameter());
        map.put("variableName", ex.getVariableName());
        return getObjectResponseEntity(ex, headers, status, request, singletonList(map));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parameterName", ex.getParameterName());
        map.put("parameterType", ex.getParameterType());
        return getObjectResponseEntity(ex, headers, status, request, singletonList(map));
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex, headers, status, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex, headers, status, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", ex.getErrorCode());
        map.put("propertyName", ex.getPropertyName());
        map.put("requiredType", ex.getRequiredType());
        map.put("value", ex.getValue());
        return getObjectResponseEntity(ex, headers, status, request, singletonList(map));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex, headers, status, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex, headers, status, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex, headers, status, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requestPartName", ex.getRequestPartName());
        return getObjectResponseEntity(ex, headers, status, request, singletonList(map));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Object> details = ex.getFieldErrors().stream().filter(Objects::nonNull).map(fieldError -> {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("field", fieldError.getField());
            map.put("rejectedValue", fieldError.getRejectedValue());
            map.put("defaultMessage", fieldError.getDefaultMessage());
            return map;
        }).collect(Collectors.toList());

        return getObjectResponseEntity(ex, headers, status, request, details);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("headers", ex.getHeaders());
        map.put("httpMethod", ex.getHttpMethod());
        map.put("requestURL", ex.getRequestURL());
        return getObjectResponseEntity(ex, headers, status, request, singletonList(map));
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        return getObjectResponseEntity(ex, headers, status, webRequest, null);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return getObjectResponseEntity(ex, headers, status, request, null);
    }

    @ResponseStatus(value = BAD_REQUEST)
    @ExceptionHandler(InvalidRequestException.class)
    public final ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException exception, WebRequest webRequest) {
        return getObjectResponseEntity(exception, new HttpHeaders(), BAD_REQUEST, webRequest, null);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleNullPointerException(NullPointerException exception, WebRequest webRequest) {
        return getObjectResponseEntity(exception, new HttpHeaders(), INTERNAL_SERVER_ERROR, webRequest, null);
    }

    @ExceptionHandler(ClassNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public final ResponseEntity<Object> handleClassNotFoundException(ClassNotFoundException exception, WebRequest webRequest) {
        return getObjectResponseEntity(exception, new HttpHeaders(), NOT_FOUND, webRequest, null);
    }

    @ExceptionHandler(SchedulerException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleClassNotFoundException(SchedulerException exception, WebRequest webRequest) {
        return getObjectResponseEntity(exception, new HttpHeaders(), INTERNAL_SERVER_ERROR, webRequest, null);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleRuntimeException(RuntimeException exception, WebRequest webRequest) {
        return getObjectResponseEntity(exception, new HttpHeaders(), INTERNAL_SERVER_ERROR, webRequest, null);
    }

    private Optional<HttpServletRequest> httpServletRequest(WebRequest webRequest) {
        HttpServletRequest httpServletRequest = null;
        if (webRequest instanceof ServletWebRequest) {
            httpServletRequest = ((ServletWebRequest) webRequest).getRequest();
        }
        return ofNullable(httpServletRequest);
    }

    private ResponseEntity<Object> getObjectResponseEntity(final Exception ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request, @Nullable final List<Object> details) {
        HttpServletRequest httpServletRequest = httpServletRequest(request).orElse(null);
        Object customInterceptorId = nonNull(httpServletRequest) ? httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) : null;
        String exceptionMessage = ex.getMessage();

        if (log.isErrorEnabled()) {
            log.error("[Request Interceptor Id : " + customInterceptorId + "] " + exceptionMessage);
            log.error(getStackTrace(ex));
        }

        HashMap<String, ErrorResponseDTO> response = new HashMap<String, ErrorResponseDTO>();
        response.put(HASHMAP_ERROR_KEY, new ErrorResponseDTO(customInterceptorId, new Date(), baseMessageResolver.getMessage(exceptionMessage, exceptionMessage), details));
        return new ResponseEntity<Object>(gson.toJson(response, getHashMapOfStringAndErrorResponseDTO()), headers, status);
    }
}
