package com.vishnu.aggarwal.quartz.rest.controller;

/*
Created by vishnu on 18/4/18 10:33 AM
*/

import com.vishnu.aggarwal.quartz.core.controller.BaseController;
import com.vishnu.aggarwal.quartz.core.exceptions.InvalidRequestException;
import com.vishnu.aggarwal.quartz.rest.service.ValidationService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.KEY_NAME;
import static com.vishnu.aggarwal.quartz.core.constants.UrlMapping.Rest.Validation.*;
import static com.vishnu.aggarwal.quartz.core.util.TypeTokenUtils.getHashMapOfStringAndBoolean;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * The type Validation controller.
 */
@RestController
@RequestMapping(value = BASE_URI, produces = {APPLICATION_JSON_VALUE})
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
     * @param keyName the key name
     * @return the response entity
     */
    @RequestMapping(value = UNIQUE_JOB_KEY_PER_GROUP, method = GET)
    @ResponseBody
    public ResponseEntity<String> isJobKeyUnique(@RequestParam(KEY_NAME) final String keyName) throws InvalidRequestException {
        if (isBlank(keyName)) {
            throw new InvalidRequestException();
        }
        HashMap<String, Boolean> responseMap = new HashMap<String, Boolean>();
        responseMap.put("valid", validationService.isJobKeyUnique(keyName));
        return new ResponseEntity<String>(gson().toJson(responseMap, getHashMapOfStringAndBoolean()), ACCEPTED);
    }

    /**
     * Is trigger key unique response entity.
     *
     * @param keyName the key name
     * @return the response entity
     */
    @RequestMapping(value = UNIQUE_TRIGGER_KEY_PER_GROUP, method = GET)
    @ResponseBody
    public ResponseEntity<String> isTriggerKeyUnique(@RequestParam(KEY_NAME) final String keyName) throws InvalidRequestException {
        if (isBlank(keyName)) {
            throw new InvalidRequestException();
        }
        HashMap<String, Boolean> responseMap = new HashMap<String, Boolean>();
        responseMap.put("valid", validationService.isTriggerKeyUnique(keyName));
        return new ResponseEntity<String>(gson().toJson(responseMap, getHashMapOfStringAndBoolean()), ACCEPTED);
    }
}
