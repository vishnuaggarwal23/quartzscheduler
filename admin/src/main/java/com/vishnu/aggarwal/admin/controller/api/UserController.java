package com.vishnu.aggarwal.admin.controller.api;

/*
Created by vishnu on 14/4/18 4:13 PM
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vishnu.aggarwal.admin.service.AuthenticationService;
import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.User;
import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.BASE_URI;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.User.*;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndString;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.getHashMapOfStringAndUserAuthenticationDTO;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.util.WebUtils.getCookie;

/**
 * The type User controller.
 */
@RestController(value = "apiUserController")
@CommonsLog
@RequestMapping(value = BASE_URI + User.BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE})
public class UserController extends BaseController {

    /**
     * The Authentication service.
     */
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;
    private final Gson gson;

    /**
     * Instantiates a new User controller.
     *
     * @param authenticationService the authentication service
     * @param objectMapper          the object mapper
     * @param gson
     */
    @Autowired
    public UserController(
            AuthenticationService authenticationService,
            ObjectMapper objectMapper,
            Gson gson) {
        this.authenticationService = authenticationService;
        this.objectMapper = objectMapper;
        this.gson = gson;
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
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> login(@RequestBody UserDTO login, HttpServletRequest request, HttpServletResponse response) {
        final ResponseEntity<String> responseEntity = authenticationService.loginUser(login);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity;
        }

        final HashMap<String, UserAuthenticationDTO> responseBody = gson.fromJson(responseEntity.getBody(), getHashMapOfStringAndUserAuthenticationDTO());
        final UserAuthenticationDTO userAuthenticationDTO = !isEmpty(responseBody) && responseBody.containsKey(HASHMAP_USER_KEY) ? responseBody.get(HASHMAP_USER_KEY) : null;

        if (nonNull(userAuthenticationDTO) && userAuthenticationDTO.getIsAuthenticated()) {
            Cookie authTokenCookie = new Cookie(X_AUTH_TOKEN, userAuthenticationDTO.getXAuthToken());
            authTokenCookie.setPath("/");
            authTokenCookie.setMaxAge(MAX_COOKIE_AGE);
            response.addCookie(authTokenCookie);
        }

//        responseEntity.getHeaders().toSingleValueMap().forEach(response::addHeader);
        HashMap<String, String> responseMap = new HashMap<String, String>();
        responseMap.put("path", format("%s%s%s%s", request.getContextPath(), Web.BASE_URI, Web.User.BASE_URI, Web.User.USER_DASHBOARD));
        return new ResponseEntity<String>(gson.toJson(responseMap, getHashMapOfStringAndString()), ACCEPTED);
    }

    /**
     * Logout string.
     *
     * @param request  the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = LOGOUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie xAuthTokenCookie = getCookie(request, X_AUTH_TOKEN);
        authenticationService.logoutUser(xAuthTokenCookie);
        xAuthTokenCookie.setValue(null);
        xAuthTokenCookie.setMaxAge(0);
        response.addCookie(xAuthTokenCookie);

        HashMap<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("path", format("%s%s%s%s", request.getContextPath(), Web.BASE_URI, Web.User.BASE_URI, Web.User.USER_LOGIN_1));
        responseMap.put("logout", TRUE);
        return new ResponseEntity<Map<String, Object>>(responseMap, ACCEPTED);
    }

    /**
     * Gets current logged in user.
     *
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the current logged in user
     */
    @RequestMapping(value = CURRENT_LOGGED_IN_USER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> getCurrentLoggedInUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return authenticationService.getCurrentLoggedInUser(getCookie(httpServletRequest, X_AUTH_TOKEN));
    }
}
