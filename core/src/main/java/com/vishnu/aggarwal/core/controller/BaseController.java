package com.vishnu.aggarwal.core.controller;

/*
Created by vishnu on 28/2/18 1:26 PM
*/

import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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

    /**
     * Get message string.
     *
     * @param messageCode the message code
     * @return the string
     */
    public String getMessage(String messageCode) {
        return isNotEmpty(messageCode) ? baseMessageResolver.getMessage(messageCode) : EMPTY;
    }

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
}
