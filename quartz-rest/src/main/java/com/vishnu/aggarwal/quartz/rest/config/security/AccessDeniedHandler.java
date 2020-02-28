package com.vishnu.aggarwal.quartz.rest.config.security;

/*
Created by vishnu on 27/4/18 4:25 PM
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vishnu.aggarwal.quartz.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.quartz.core.dto.ErrorResponseDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.HASHMAP_ERROR_KEY;
import static com.vishnu.aggarwal.quartz.core.util.TypeTokenUtils.getHashMapOfStringAndErrorResponseDTO;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * The type Access denied handler.
 */
@CommonsLog
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    private final BaseMessageResolver baseMessageResolver;
    private final Gson gson;
    private final ObjectMapper objectMapper;

    public AccessDeniedHandler(
            BaseMessageResolver baseMessageResolver,
            Gson gson,
            ObjectMapper objectMapper) {
        this.baseMessageResolver = baseMessageResolver;
        this.gson = gson;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        HashMap<String, ErrorResponseDTO> responseMap = new HashMap<String, ErrorResponseDTO>();
        responseMap.put(HASHMAP_ERROR_KEY, new ErrorResponseDTO(request.getAttribute(CUSTOM_REQUEST_ID), new Date(), baseMessageResolver.getMessage(accessDeniedException.getMessage()), null));
        response.getWriter().write(gson.toJson(responseMap, getHashMapOfStringAndErrorResponseDTO()));
        response.setCharacterEncoding("UTF-8");
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(FORBIDDEN.value());
        log.error("[Request Interceptor Id : " + request.getAttribute(CUSTOM_REQUEST_ID) + "] " + accessDeniedException.getMessage());
        log.error(getStackTrace(accessDeniedException));
//        response.sendError(FORBIDDEN.value(), baseMessageResolver.getMessage(accessDeniedException.getMessage(), accessDeniedException.getMessage()));
    }
}
