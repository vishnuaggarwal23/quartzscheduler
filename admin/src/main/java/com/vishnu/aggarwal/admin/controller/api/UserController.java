package com.vishnu.aggarwal.admin.controller.api;

/*
Created by vishnu on 14/4/18 4:13 PM
*/

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static com.vishnu.aggarwal.admin.utils.CookieUtils.create;
import static com.vishnu.aggarwal.admin.utils.CookieUtils.remove;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.BASE_URI;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.*;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.CollectionUtils.isEmpty;

@RestController(value = "apiUserController")
@CommonsLog
@RequestMapping(value = BASE_URI + User.BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE})
public class UserController extends BaseController {

    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = User.LOGIN)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> login(@RequestBody @NotNull @NotBlank @NotEmpty final UserDTO login, HttpServletRequest request, HttpServletResponse response) {
        final ResponseEntity<String> responseEntity = authenticationService.loginUser(login);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity;
        }

        final HashMap<String, UserAuthenticationDTO> responseBody = gson().fromJson(responseEntity.getBody(), getHashMapOfStringAndUserAuthenticationDTO());
        final UserAuthenticationDTO userAuthenticationDTO = !isEmpty(responseBody) && responseBody.containsKey(HASHMAP_USER_KEY) ? responseBody.get(HASHMAP_USER_KEY) : null;

        if (nonNull(userAuthenticationDTO) && userAuthenticationDTO.getIsAuthenticated()) {
            response.addCookie(create(X_AUTH_TOKEN, userAuthenticationDTO.getXAuthToken(), null, true, MAX_COOKIE_AGE, null));
        }

        HashMap<String, String> responseMap = new HashMap<String, String>();
        responseMap.put("path", format("%s%s%s%s", request.getContextPath(), Web.BASE_URI, Web.User.BASE_URI, Web.User.USER_DASHBOARD));
        return createResponseEntity(responseMap, getHashMapOfStringAndString());
    }

    @PostMapping(User.LOGOUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> logout(@CookieValue(X_AUTH_TOKEN) String xAuthToken, HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logoutUser(xAuthToken);
        HashMap<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("path", format("%s%s%s%s", request.getContextPath(), Web.BASE_URI, Web.User.BASE_URI, Web.User.USER_LOGIN_1));
        responseMap.put("logout", TRUE);
        response.addCookie(remove(request, X_AUTH_TOKEN, null, null, true));
        return createResponseEntity(responseMap, getHashMapOfStringAndObject());
    }

    @GetMapping(User.CURRENT_LOGGED_IN_USER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> getCurrentLoggedInUser(@CookieValue(X_AUTH_TOKEN) String xAuthToken, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return authenticationService.getCurrentLoggedInUser(xAuthToken);
    }
}
