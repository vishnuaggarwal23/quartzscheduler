package com.vishnu.aggarwal.rest.annotation;

import com.vishnu.aggarwal.rest.service.dao.jpa.UserDAOService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/*
Created by vishnu on 19/8/18 1:43 PM
*/

public class CustomUniqueUsernameValidator implements ConstraintValidator<CustomUniqueUsername, String> {

    private final UserDAOService userDAOService;

    @Autowired
    public CustomUniqueUsernameValidator(UserDAOService userDAOService) {
        this.userDAOService = userDAOService;
    }

    public void initialize(CustomUniqueUsername constraint) {
    }

    public boolean isValid(String username, ConstraintValidatorContext context) {
        return isEmpty(username) ? false : userDAOService.isUsernameUnique(username);
    }
}
