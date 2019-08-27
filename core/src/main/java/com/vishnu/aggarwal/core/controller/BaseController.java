package com.vishnu.aggarwal.core.controller;

/*
Created by vishnu on 28/2/18 1:26 PM
*/

import com.google.gson.Gson;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Type;
import java.util.List;

import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpStatus.ACCEPTED;

@CommonsLog
public class BaseController {

    @Autowired
    BaseMessageResolver baseMessageResolver;

    @Autowired
    Gson gson;

    @SuppressWarnings("unchecked")
    protected static void setRestResponseVO(RestResponseVO restResponseVO, Object data, HttpStatus httpStatus, String message) {
        restResponseVO.setData(data);
        restResponseVO.setCode(nonNull(httpStatus) ? httpStatus.value() : null);
        restResponseVO.setMessage(message);
    }

    @SuppressWarnings("unchecked")
    protected static void setDataTableVO(DataTableVO dataTableVO, Integer count, Integer recordsTotal, Integer recordsFiltered, List data) {
        dataTableVO.setCount(count);
        dataTableVO.setRecordsTotal(recordsTotal);
        dataTableVO.setRecordsFiltered(recordsFiltered);
        dataTableVO.setData(data);
    }

    protected Gson gson() {
        return gson;
    }

    public String getMessage(String messageCode) {
        return isNotBlank(messageCode) ? baseMessageResolver.getMessage(messageCode) : EMPTY;
    }

    public String getMessage(String messageCode, Object... messageArgs) {
        if (isNotBlank(messageCode)) {
            if (allNotNull(messageArgs)) {
                return baseMessageResolver.getMessage(messageCode, messageArgs);
            }
            return baseMessageResolver.getMessage(messageCode);
        }
        return EMPTY;
    }

    public String formatMessage(String messagePattern, Object... messageArguments) {
        if (isNotBlank(messagePattern)) {
            if (allNotNull(messageArguments)) {
                return format(messagePattern, messageArguments);
            }
            return format(messagePattern);
        }
        return EMPTY;
    }

    protected void checkBindingException(BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }

    protected ResponseEntity<String> createResponseEntity(Object response, Type typeSrc) {
        return new ResponseEntity<String>(gson().toJson(response, typeSrc), ACCEPTED);
    }
}