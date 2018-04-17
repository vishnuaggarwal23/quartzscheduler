package com.vishnu.aggarwal.admin.controller.web;

/*
Created by vishnu on 28/3/18 11:41 AM
*/

import com.vishnu.aggarwal.core.controller.BaseController;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.USER_LOGGED_IN;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.User.*;
import static java.lang.Boolean.TRUE;

@Controller(value = "webUserController")
@CommonsLog
@RequestMapping(BASE_URI)
public class UserController extends BaseController {

    @RequestMapping({USER_LOGIN_1, USER_LOGIN_2})
    public ModelAndView login(HttpServletRequest request) {
        return new ModelAndView("login");
    }

    @RequestMapping(FORGOT_PASSWORD)
    public ModelAndView forgotPassword(@RequestParam(value = "username", required = false) String username) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("username", username);
        return new ModelAndView("forgotPassword", modelMap);
    }

    @RequestMapping(USER_DASHBOARD)
    public ModelAndView dashboard() {
        ModelMap modelMap = new ModelMap();
        modelMap.put(USER_LOGGED_IN, TRUE);
        return new ModelAndView("dashboard", modelMap);
    }
}