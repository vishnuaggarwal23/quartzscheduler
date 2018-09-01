package com.vishnu.aggarwal.rest.controller;

/*
Created by vishnu on 18/4/18 10:33 AM
*/

import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.rest.service.ValidationService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.KEY_NAME;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Validation.*;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndBoolean;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * The type Validation controller.
 */
@RestController
@RequestMapping(value = BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE})
@CommonsLog
@Secured({"ROLE_USER"})
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
     * @param keyName  the key name
     * @param request  the request
     * @param response the response
     * @return the response entity
     */
    @RequestMapping(value = UNIQUE_JOB_KEY_PER_GROUP, method = GET)
    @ResponseBody
    public ResponseEntity<String> isJobKeyUnique(@RequestParam(KEY_NAME) final String keyName, HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Boolean> responseMap = new HashMap<String, Boolean>();
        responseMap.put("valid", validationService.isJobKeyUnique(keyName));
        return new ResponseEntity<String>(gson().toJson(responseMap, getHashMapOfStringAndBoolean()), ACCEPTED);
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
    public ResponseEntity<String> isTriggerKeyUnique(@RequestParam(KEY_NAME) final String keyName, HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Boolean> responseMap = new HashMap<String, Boolean>();
        responseMap.put("valid", validationService.isTriggerKeyUnique(keyName));
        return new ResponseEntity<String>(gson().toJson(responseMap, getHashMapOfStringAndBoolean()), ACCEPTED);
    }
}
