package com.vishnu.aggarwal.rest.service;

import com.vishnu.aggarwal.rest.service.repository.QuartzDAOService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
Created by vishnu on 18/4/18 11:06 AM
*/

@Service
@CommonsLog
public class ValidationService {

    @Autowired
    QuartzDAOService quartzDAOService;

    public Boolean isJobKeyUnique(String keyName) {
        return quartzDAOService.isUniqueJobKey(keyName, "DEFAULT");
    }

    public Boolean isTriggerKeyUnique(String keyName) {
        return quartzDAOService.isUniqueTriggerKey(keyName, "DEFAULT");
    }
}
