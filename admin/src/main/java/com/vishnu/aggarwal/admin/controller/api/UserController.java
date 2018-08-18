package com.vishnu.aggarwal.admin.controller.api;

/*
Created by vishnu on 14/4/18 4:13 PM
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.aggarwal.admin.service.AuthenticationService;
import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api;
import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.User.*;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.User.USER_DASHBOARD;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.User.USER_LOGIN_1;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.util.Assert.*;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.util.WebUtils.getCookie;

/**
 * The type User controller.
 */
@RestController(value = "apiUserController")
@CommonsLog
@RequestMapping(value = Api.User.BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE})
public class UserController extends BaseController {

    /**
     * The Authentication service.
     */
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new User controller.
     *
     * @param authenticationService the authentication service
     * @param objectMapper          the object mapper
     */
    @Autowired
    public UserController(
            AuthenticationService authenticationService,
            ObjectMapper objectMapper) {
        this.authenticationService = authenticationService;
        this.objectMapper = objectMapper;
    }

    /**
     * Login response entity.
     *
     * @param login    the login
     * @param request  the request
     * @param response the response
     * @return the response entity
     */
    @RequestMapping(value = LOGIN, method = POST)
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody UserDTO login, HttpServletRequest request, HttpServletResponse response) {
        try {
            ResponseEntity<UserAuthenticationDTO> userAuthenticationDTOResponseEntity = authenticationService.loginUser(login);

            notNull(userAuthenticationDTOResponseEntity, formatMessage(getMessage("")));
            notNull(userAuthenticationDTOResponseEntity.getBody(), formatMessage(getMessage("")));
            notEmpty(userAuthenticationDTOResponseEntity.getHeaders(), formatMessage(getMessage("")));

            String xAuthToken = isEmpty(userAuthenticationDTOResponseEntity.getHeaders().get(X_AUTH_TOKEN)) ? userAuthenticationDTOResponseEntity.getBody().getXAuthToken() : userAuthenticationDTOResponseEntity.getHeaders().get(X_AUTH_TOKEN).get(0);
            hasText(xAuthToken, formatMessage(getMessage("")));

            Cookie authTokenCookie = new Cookie(X_AUTH_TOKEN, xAuthToken);
            authTokenCookie.setPath("/");
            authTokenCookie.setMaxAge(MAX_COOKIE_AGE);
            response.addCookie(authTokenCookie);

            for (Entry<String, List<String>> headerEntry : userAuthenticationDTOResponseEntity.getHeaders().entrySet()) {
                response.addHeader(headerEntry.getKey(), headerEntry.getValue().get(0));
            }

            Map<String, String> responseMap = new HashMap<String, String>();
            responseMap.put("path", format("%s%s%s", request.getContextPath(), Web.User.BASE_URI, USER_DASHBOARD));

            return new ResponseEntity<String>(objectMapper.writeValueAsString(responseMap), valueOf(response.getStatus()));
        } catch (Exception e) {
            log.error("[Request ID " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while logging in user");
            log.error(getStackTrace(e));
            return new ResponseEntity<String>(getMessage(""), valueOf(response.getStatus()));
        }
    }

    /**
     * Logout string.
     *
     * @param request  the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = LOGOUT)
    public ResponseEntity<Map> logout(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Cookie xAuthTokenCookie = getCookie(request, X_AUTH_TOKEN);
            authenticationService.logoutUser(xAuthTokenCookie);
            xAuthTokenCookie.setValue(null);
            xAuthTokenCookie.setMaxAge(0);
            response.addCookie(xAuthTokenCookie);

            map.put("path", format("%s%s%s", request.getContextPath(), Web.User.BASE_URI, USER_LOGIN_1));
            map.put("logout", TRUE);
        } catch (Exception e) {
            log.error("[Request ID " + request.getAttribute(CUSTOM_REQUEST_ID) + "] Error while logging out user");
            log.error(getStackTrace(e));
            map.put("logout", FALSE);
        }
        return new ResponseEntity<Map>(map, valueOf(response.getStatus()));
    }

    /**
     * Gets current logged in user.
     *
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the current logged in user
     */
    @RequestMapping(value = CURRENT_LOGGED_IN_USER)
    public ResponseEntity<UserDTO> getCurrentLoggedInUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return authenticationService.getCurrentLoggedInUser(getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request ID " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while logging out user");
            log.error(getStackTrace(e));
            return new ResponseEntity<UserDTO>(valueOf(httpServletResponse.getStatus()));
        }
    }
}
