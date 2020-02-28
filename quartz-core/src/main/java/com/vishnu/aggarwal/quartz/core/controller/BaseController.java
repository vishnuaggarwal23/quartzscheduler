package com.vishnu.aggarwal.quartz.core.controller;

/*
Created by vishnu on 28/2/18 1:26 PM
*/

import com.google.gson.Gson;
import com.vishnu.aggarwal.quartz.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.quartz.core.vo.DataTableVO;
import com.vishnu.aggarwal.quartz.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The type Base controller.
 */
@CommonsLog
public class BaseController {

    /**
     * The Base message resolver.
     */
    @Autowired
    BaseMessageResolver baseMessageResolver;

    @Autowired
    Gson gson;

    /**
     * Set rest response vo.
     *
     * @param restResponseVO the rest response vo
     * @param data           the data
     * @param httpStatus     the http status
     * @param message        the message
     */
    @SuppressWarnings("unchecked")
    protected static void setRestResponseVO(RestResponseVO restResponseVO, Object data, HttpStatus httpStatus, String message) {
        restResponseVO.setData(data);
        if (nonNull(httpStatus)) {
            restResponseVO.setCode(httpStatus.value());
        } else {
            restResponseVO.setCode(null);
        }
        restResponseVO.setMessage(message);
    }

    /**
     * Set data table vo.
     *
     * @param dataTableVO     the data table vo
     * @param count           the count
     * @param recordsTotal    the records total
     * @param recordsFiltered the records filtered
     * @param data            the data
     */
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

    /**
     * Gets message.
     *
     * @param messageCode the message code
     * @return the message
     */
    public String getMessage(String messageCode) {
        return isNotBlank(messageCode) ? baseMessageResolver.getMessage(messageCode) : EMPTY;
    }

    /**
     * Gets message.
     *
     * @param messageCode the message code
     * @param messageArgs the message args
     * @return the message
     */
    public String getMessage(String messageCode, Object... messageArgs) {
        if (isNotBlank(messageCode)) {
            if (allNotNull(messageArgs)) {
                return baseMessageResolver.getMessage(messageCode, messageArgs);
            }
            return baseMessageResolver.getMessage(messageCode);
        }
        return EMPTY;
    }

    /**
     * Format message string.
     *
     * @param messagePattern   the message pattern
     * @param messageArguments the message arguments
     * @return the string
     */
    public String formatMessage(String messagePattern, Object... messageArguments) {
        if (isNotBlank(messagePattern)) {
            if (allNotNull(messageArguments)) {
                return format(messagePattern, messageArguments);
            }
            return format(messagePattern);
        }
        return EMPTY;
    }
}
