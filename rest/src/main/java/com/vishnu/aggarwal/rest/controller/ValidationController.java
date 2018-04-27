package com.vishnu.aggarwal.rest.controller;

/*
Created by vishnu on 18/4/18 10:33 AM
*/

import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import com.vishnu.aggarwal.rest.service.ValidationService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Validation.*;
import static java.lang.Boolean.FALSE;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * The type Validation controller.
 */
@RestController
@RequestMapping(value = BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE})
@CommonsLog
public class ValidationController extends BaseController {

    /**
     * The Validation service.
     */
    @Autowired
    ValidationService validationService;

    /**
     * Is job key unique response entity.
     *
     * @param keyName  the key name
     * @param request  the request
     * @param response the response
     * @return the response entity
     */
    @RequestMapping(value = UNIQUE_JOB_KEY_PER_GROUP, method = GET)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> isJobKeyUnique(@RequestParam("keyName") String keyName, HttpServletRequest request, HttpServletResponse response) {
        RestResponseVO<Boolean> validationResponse = new RestResponseVO<Boolean>();
        try {
            setRestResponseVO(validationResponse, validationService.isJobKeyUnique(keyName), valueOf(response.getStatus()), getMessage(""));
        } catch (Exception e) {
            log.error("************* Error while fetching unique job key validation response **************** \n");
            log.error("************* [Request ID " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Stacktrace ****************** \n");
            log.error(getStackTrace(e));
            setRestResponseVO(validationResponse, FALSE, valueOf(response.getStatus()), getMessage(""));
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(validationResponse, valueOf(response.getStatus()));
    }

    /**
     * Is trigger key unique response entity.
     *
     * @param keyName  the key name
     * @param request  the request
     * @param response the response
     * @return the response entity
     */
    @RequestMapping(value = UNIQUE_TRIGGER_KEY_PER_GROUP, method = GET)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> isTriggerKeyUnique(@RequestParam("keyName") String keyName, HttpServletRequest request, HttpServletResponse response) {
        RestResponseVO<Boolean> validationResponse = new RestResponseVO<Boolean>();
        try {
            setRestResponseVO(validationResponse, validationService.isTriggerKeyUnique(keyName), valueOf(response.getStatus()), getMessage(""));
        } catch (Exception e) {
            log.error("************* [Request ID " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while fetching unique trigger key validation response **************** \n");
            log.error("************* [Request ID " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Stacktrace ****************** \n");
            log.error(getStackTrace(e));
            setRestResponseVO(validationResponse, FALSE, valueOf(response.getStatus()), getMessage(""));
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(validationResponse, valueOf(response.getStatus()));
    }
}
