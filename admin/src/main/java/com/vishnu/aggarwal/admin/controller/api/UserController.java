package com.vishnu.aggarwal.admin.controller.api;

/*
Created by vishnu on 14/4/18 4:13 PM
*/

import com.vishnu.aggarwal.admin.service.AuthenticationService;
import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api;
import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.exceptions.RestServiceCallException;
import com.vishnu.aggarwal.core.exceptions.UserNotAuthenticatedException;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.MAX_COOKIE_AGE;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.User.LOGIN;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.User.LOGOUT;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.User.USER_DASHBOARD;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.User.USER_LOGIN_1;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.util.WebUtils.getCookie;

@RestController(value = "apiUserController")
@CommonsLog
@RequestMapping(value = Api.User.BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE})
public class UserController extends BaseController {

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = LOGIN, method = POST)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> login(@RequestBody UserDTO login, HttpServletRequest request, HttpServletResponse response) {
        try {
            RestResponseVO<String> restResponseVO = authenticationService.loginUser(login);
            if (nonNull(restResponseVO) && restResponseVO.getCode() != HttpStatus.UNAUTHORIZED.value() && nonNull(restResponseVO.getData())) {
                Cookie authTokenCookie = new Cookie(X_AUTH_TOKEN, restResponseVO.getData());
                authTokenCookie.setPath("/");
                authTokenCookie.setMaxAge(MAX_COOKIE_AGE);
                response.addCookie(authTokenCookie);
                return new ResponseEntity<RestResponseVO<String>>(new RestResponseVO<String>("{\"path\" : \"" + format("%s%s%s", request.getContextPath(), Web.User.BASE_URI, USER_DASHBOARD) + "\"}", restResponseVO.getCode(), getMessage("")), valueOf(restResponseVO.getCode()));
            } else {
                throw new UserNotAuthenticatedException(getMessage(""));
            }
        } catch (RestServiceCallException e) {
            log.error("***************** Error while logging in user *************************");
            e.getLocalizedMessage();
            response.setStatus(SC_UNAUTHORIZED);
            return new ResponseEntity<RestResponseVO<String>>(new RestResponseVO<String>(null, SC_UNAUTHORIZED, getMessage("")), valueOf(SC_UNAUTHORIZED));
        } catch (Exception e) {
            log.error("***************** Error while logging in user *************************");
            e.getLocalizedMessage();
            response.setStatus(SC_UNAUTHORIZED);
            return new ResponseEntity<RestResponseVO<String>>(new RestResponseVO<String>(null, SC_UNAUTHORIZED, getMessage("")), valueOf(SC_UNAUTHORIZED));
        }
    }

    @RequestMapping(value = LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String redirectPath;
        try {
            Cookie xAuthTokenCookie = getCookie(request, X_AUTH_TOKEN);
            RestResponseVO<String> restResponseVO = authenticationService.logoutUser(xAuthTokenCookie);
            xAuthTokenCookie.setValue(null);
            xAuthTokenCookie.setMaxAge(0);
            response.addCookie(xAuthTokenCookie);
            redirectPath = format("%s%s%s", request.getContextPath(), Web.User.BASE_URI, USER_LOGIN_1);
        } catch (RestServiceCallException e) {
            log.error("******************* Error while logging out user ********************");
            e.getLocalizedMessage();
            response.setStatus(SC_UNAUTHORIZED);
            redirectPath = format("%s%s%s", request.getContextPath(), Web.User.BASE_URI, USER_LOGIN_1);
        } catch (Exception e) {
            log.error("******************* Error while logging out user ********************");
            e.getLocalizedMessage();
            response.setStatus(SC_UNAUTHORIZED);
            redirectPath = format("%s%s%s", request.getContextPath(), Web.User.BASE_URI, USER_LOGIN_1);
        }
        return "{\"path\" : \"" + redirectPath + "\", \"status\" : \"" + response.getStatus() + "\"}";
    }
}