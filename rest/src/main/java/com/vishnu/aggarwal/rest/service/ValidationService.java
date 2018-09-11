package com.vishnu.aggarwal.rest.service;

import com.vishnu.aggarwal.rest.service.dao.jpa.QuartzDAOService;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 18/4/18 11:06 AM
*/

/**
 * The type Validation service.
 */
@Service
@CommonsLog
@Transactional
public class ValidationService {

    /**
     * The Quartz dao service.
     */
    private final QuartzDAOService quartzDAOService;

    private final UserService userService;

    @Autowired
    public ValidationService(
            QuartzDAOService quartzDAOService,
            UserService userService) {
        this.quartzDAOService = quartzDAOService;
        this.userService = userService;
    }

    /**
     * Is job key unique boolean.
     *
     * @param keyName the key name
     * @return the boolean
     */
    public Boolean isJobKeyUnique(String keyName) throws HibernateException {
        return quartzDAOService.isUniqueJobKey(keyName, userService.getCurrentLoggedInUser().getId().toString());
    }

    /**
     * Is trigger key unique boolean.
     *
     * @param keyName the key name
     * @return the boolean
     */
    public Boolean isTriggerKeyUnique(String keyName) {
        return quartzDAOService.isUniqueTriggerKey(keyName, userService.getCurrentLoggedInUser().getId().toString());
    }
}
