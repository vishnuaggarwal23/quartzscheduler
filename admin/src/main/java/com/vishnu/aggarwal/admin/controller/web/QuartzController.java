package com.vishnu.aggarwal.admin.controller.web;

/*
Created by vishnu on 13/3/18 11:33 AM
*/

import com.vishnu.aggarwal.admin.service.AuthenticationService;
import com.vishnu.aggarwal.core.controller.BaseController;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Controller(value = "webQuartzController")
@CommonsLog
@RequestMapping("/web/quartz")
public class QuartzController extends BaseController {

    @Autowired
    AuthenticationService authenticationService;

    public String createNewJob(@CookieValue(name = "x-auth-token") Cookie cookie, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 1L);
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.addHeader("Cache-Control", "no-store");

        if (isTrue(authenticationService.isAuthenticatedUser(cookie))) {
            return "quartz/job/create";
        } else {
            return "login";
        }
    }

    public String createNewTrigger(@CookieValue(name = "x-auth-token") Cookie cookie, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 1L);
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.addHeader("Cache-Control", "no-store");
        if (isTrue(authenticationService.isAuthenticatedUser(cookie))) {
            return "quartz/trigger/create";
        } else {
            return "login";
        }
    }
}
