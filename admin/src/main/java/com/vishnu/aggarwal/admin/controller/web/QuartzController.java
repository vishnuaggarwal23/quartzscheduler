package com.vishnu.aggarwal.admin.controller.web;

/*
Created by vishnu on 13/3/18 11:33 AM
*/

import com.vishnu.aggarwal.admin.service.AuthenticationService;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.exceptions.RestServiceCallException;
import com.vishnu.aggarwal.core.exceptions.UserNotAuthenticatedException;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
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

        try {
            RestResponseVO<Boolean> restResponseVO = authenticationService.isAuthenticatedUser(cookie);
            if (isTrue(restResponseVO.getData())) {
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                return "quartz/job/create";
            } else {
                throw new UserNotAuthenticatedException(getMessage(""));
            }
        } catch (RestServiceCallException | UserNotAuthenticatedException e) {
            log.error("************* Error while fetching and returning createNewJob HTML web page \n", e);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "login";
        } catch (Exception e) {
            log.error("************* Error while fetching and returning createNewJob HTML web page \n", e);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "login";
        }
    }

    public String createNewTrigger(@CookieValue(name = "x-auth-token") Cookie cookie, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 1L);
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.addHeader("Cache-Control", "no-store");

        try {
            RestResponseVO<Boolean> restResponseVO = authenticationService.isAuthenticatedUser(cookie);
            if (isTrue(restResponseVO.getData())) {
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                return "quartz/trigger/create";
            } else {
                throw new UserNotAuthenticatedException(getMessage(""));
            }
        } catch (RestServiceCallException | UserNotAuthenticatedException e) {
            log.error("************* Error while fetching and returning createNewTrigger HTML web page \n", e);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "login";
        } catch (Exception e) {
            log.error("************* Error while fetching and returning createNewTrigger HTML web page \n", e);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "login";
        }
    }
}
