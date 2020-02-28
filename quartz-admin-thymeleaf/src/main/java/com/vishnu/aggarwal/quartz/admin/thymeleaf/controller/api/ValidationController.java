package com.vishnu.aggarwal.quartz.admin.thymeleaf.controller.api;

/*
Created by vishnu on 18/4/18 10:33 AM
*/

import com.vishnu.aggarwal.quartz.admin.thymeleaf.service.ValidationService;
import com.vishnu.aggarwal.quartz.core.constants.UrlMapping.Admin.Api.Validation;
import com.vishnu.aggarwal.quartz.core.controller.BaseController;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static com.vishnu.aggarwal.quartz.core.constants.UrlMapping.Admin.Api.BASE_URI;
import static com.vishnu.aggarwal.quartz.core.constants.UrlMapping.Admin.Api.Validation.UNIQUE_JOB_KEY_PER_GROUP;
import static com.vishnu.aggarwal.quartz.core.constants.UrlMapping.Admin.Api.Validation.UNIQUE_TRIGGER_KEY_PER_GROUP;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * The type Validation controller.
 */
@RestController(value = "apiValidationController")
@RequestMapping(value = BASE_URI + Validation.BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE})
@CommonsLog
public class ValidationController extends BaseController {

    /**
     * The Validation service.
     */
    private final ValidationService validationService;

    /**
     * Instantiates a new Validation controller.
     *
     * @param validationService the validation service
     */
    @Autowired
    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

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
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> isJobKeyUnique(@RequestParam("keyName") final String keyName, HttpServletRequest request, HttpServletResponse response) {
        return validationService.isJobKeyUnique(keyName, WebUtils.getCookie(request, X_AUTH_TOKEN));
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
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> isTriggerKeyUnique(@RequestParam("keyName") final String keyName, HttpServletRequest request, HttpServletResponse response) {
        return validationService.isTriggerKeyUnique(keyName, WebUtils.getCookie(request, X_AUTH_TOKEN));
    }
}
