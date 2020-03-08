package com.vishnu.aggarwal.quartz.rest.service;

import com.vishnu.aggarwal.quartz.rest.service.dao.jpa.QuartzDAOService;
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

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public ValidationService(
            QuartzDAOService quartzDAOService,
            UserServiceImpl userServiceImpl) {
        this.quartzDAOService = quartzDAOService;
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * Is job key unique boolean.
     *
     * @param keyName the key name
     * @return the boolean
     */
    public Boolean isJobKeyUnique(String keyName) throws HibernateException {
        return quartzDAOService.isUniqueJobKey(keyName, userServiceImpl.getCurrentLoggedInUser().getId().toString());
    }

    /**
     * Is trigger key unique boolean.
     *
     * @param keyName the key name
     * @return the boolean
     */
    public Boolean isTriggerKeyUnique(String keyName) {
        return quartzDAOService.isUniqueTriggerKey(keyName, userServiceImpl.getCurrentLoggedInUser().getId().toString());
    }
}
