package com.vishnu.aggarwal.admin.controller.api;

/*
Created by vishnu on 18/4/18 10:33 AM
*/

import com.vishnu.aggarwal.admin.service.ValidationService;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.Validation.*;
import static java.lang.Boolean.FALSE;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * The type Validation controller.
 */
@RestController(value = "apiValidationController")
@RequestMapping(value = BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE})
@CommonsLog
public class ValidationController extends BaseController {

    /**
     * The Validation service.
     */
    private final ValidationService validationService;

    @Autowired
    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * Is job key unique response entity.
     *
     * @param keyName    the key name
     * @param xAuthToken the x auth token
     * @param request    the request
     * @param response   the response
     * @return the response entity
     */
    @RequestMapping(value = UNIQUE_JOB_KEY_PER_GROUP, method = GET)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> isJobKeyUnique(@RequestParam("keyName") String keyName, @CookieValue(X_AUTH_TOKEN) Cookie xAuthToken, HttpServletRequest request, HttpServletResponse response) {
        RestResponseVO<Boolean> validationResponse = new RestResponseVO<Boolean>();
        try {
            validationResponse = validationService.isJobKeyUnique(keyName, xAuthToken);
        } catch (Exception e) {
            log.error("[Request ID " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while fetching unique job key validation response");
            log.error(getStackTrace(e));
            setRestResponseVO(validationResponse, FALSE, valueOf(response.getStatus()), getMessage(""));
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(validationResponse, valueOf(response.getStatus()));
    }

    /**
     * Is trigger key unique response entity.
     *
     * @param keyName    the key name
     * @param xAuthToken the x auth token
     * @param request    the request
     * @param response   the response
     * @return the response entity
     */
    @RequestMapping(value = UNIQUE_TRIGGER_KEY_PER_GROUP, method = GET)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> isTriggerKeyUnique(@RequestParam("keyName") String keyName, @CookieValue(X_AUTH_TOKEN) Cookie xAuthToken, HttpServletRequest request, HttpServletResponse response) {
        RestResponseVO<Boolean> validationResponse = new RestResponseVO<Boolean>();
        try {
            validationResponse = validationService.isTriggerKeyUnique(keyName, xAuthToken);
        } catch (Exception e) {
            log.error("[Request ID " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while fetching unique trigger key validation response");
            log.error(getStackTrace(e));
            setRestResponseVO(validationResponse, FALSE, valueOf(response.getStatus()), getMessage(""));
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(validationResponse, valueOf(response.getStatus()));
    }
}
