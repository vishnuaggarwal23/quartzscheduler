package com.vishnu.aggarwal.rest.controller;

import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.rest.service.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.HASHMAP_USER_KEY;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User.BASE_URI;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User.CURRENT_LOGGED_IN_USER;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndUserDTO;
import static com.vishnu.aggarwal.rest.util.DTOConversion.convertFromUser;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/*
Created by vishnu on 30/3/18 10:46 AM
*/

/**
 * The type User controller.
 */
@RestController
@CommonsLog
@RequestMapping(value = BASE_URI, produces = APPLICATION_JSON_UTF8_VALUE)
@Secured("ROLE_USER")
public class UserController extends BaseController {

    /**
     * The User service.
     */
    private final UserService userService;

    /**
     * Instantiates a new User controller.
     *
     * @param userService the user service
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = CURRENT_LOGGED_IN_USER, method = GET)
    public ResponseEntity<String> getCurrentLoggedInUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        HashMap<String, UserDTO> response = new HashMap<String, UserDTO>();
        response.put(HASHMAP_USER_KEY, convertFromUser(userService.getCurrentLoggedInUser()));
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndUserDTO()), ACCEPTED);

    }
}
