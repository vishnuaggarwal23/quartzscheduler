package com.vishnu.aggarwal.rest.controller;

import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import com.vishnu.aggarwal.rest.service.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User.*;
import static java.lang.Boolean.FALSE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/*
Created by vishnu on 30/3/18 10:46 AM
*/

@RestController
@CommonsLog
@RequestMapping(value = BASE_URI, produces = APPLICATION_JSON_UTF8_VALUE)
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @RequestMapping(value = AUTHENTICATE, method = GET)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> authenticateUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<>(FALSE, null, EMPTY);
        try {
            setRestResponseVO(restResponseVO, userService.authenticateUser(httpServletRequest.getHeader(X_AUTH_TOKEN)), null, getMessage(""));
        } catch (Exception e) {
            log.error("************** Error while authenticating user **************** \n");
            e.getLocalizedMessage();
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    @RequestMapping(value = LOGIN, method = POST)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> login(@RequestBody UserDTO login, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<>(null, null, EMPTY);
        try {
            setRestResponseVO(restResponseVO, userService.login(login), null, getMessage(""));
        } catch (Exception e) {
            log.error("************** Error while login user **************** \n");
            e.getLocalizedMessage();
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }
}
