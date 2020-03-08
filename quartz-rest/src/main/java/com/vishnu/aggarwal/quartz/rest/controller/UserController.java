package com.vishnu.aggarwal.quartz.rest.controller;

import com.vishnu.aggarwal.quartz.core.controller.BaseController;
import com.vishnu.aggarwal.quartz.core.dto.UserDTO;
import com.vishnu.aggarwal.quartz.rest.service.UserServiceImpl;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.HASHMAP_USER_KEY;
import static com.vishnu.aggarwal.quartz.core.constants.UrlMapping.Rest.User.BASE_URI;
import static com.vishnu.aggarwal.quartz.core.constants.UrlMapping.Rest.User.CURRENT_LOGGED_IN_USER;
import static com.vishnu.aggarwal.quartz.core.util.TypeTokenUtils.getHashMapOfStringAndUserDTO;
import static com.vishnu.aggarwal.quartz.rest.util.DTOConversion.convertFromUser;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/*
Created by vishnu on 30/3/18 10:46 AM
*/

/**
 * The type User controller.
 */
@RestController
@CommonsLog
@RequestMapping(value = BASE_URI, produces = APPLICATION_JSON_VALUE)
@Secured("ROLE_USER")
public class UserController extends BaseController {

    /**
     * The User service.
     */
    private final UserServiceImpl userServiceImpl;

    /**
     * Instantiates a new User controller.
     *
     * @param userServiceImpl the user service
     */
    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * Gets current logged in user.
     *
     * @return the current logged in user
     */
    @RequestMapping(value = CURRENT_LOGGED_IN_USER, method = GET)
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> getCurrentLoggedInUser() {
        HashMap<String, UserDTO> response = new HashMap<String, UserDTO>();
        response.put(HASHMAP_USER_KEY, convertFromUser(userServiceImpl.getCurrentLoggedInUser()));
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndUserDTO()), ACCEPTED);

    }
}
