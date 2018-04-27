package com.vishnu.aggarwal.rest.service;

import com.vishnu.aggarwal.rest.service.repository.QuartzDAOService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
Created by vishnu on 18/4/18 11:06 AM
*/

/**
 * The type Validation service.
 */
@Service
@CommonsLog
public class ValidationService {

    /**
     * The Quartz dao service.
     */
    @Autowired
    QuartzDAOService quartzDAOService;

    /**
     * Is job key unique boolean.
     *
     * @param keyName the key name
     * @return the boolean
     */
    public Boolean isJobKeyUnique(String keyName) {
        return quartzDAOService.isUniqueJobKey(keyName, "DEFAULT");
    }

    /**
     * Is trigger key unique boolean.
     *
     * @param keyName the key name
     * @return the boolean
     */
    public Boolean isTriggerKeyUnique(String keyName) {
        return quartzDAOService.isUniqueTriggerKey(keyName, "DEFAULT");
    }
}
