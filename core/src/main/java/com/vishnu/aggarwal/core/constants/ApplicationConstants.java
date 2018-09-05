package com.vishnu.aggarwal.core.constants;

import com.vishnu.aggarwal.core.constants.UrlMapping.Admin;

import static com.vishnu.aggarwal.core.constants.UrlMapping.ALL_URL_PREFIX;
import static java.lang.String.format;

/**
 * The type Application constants.
 */
/*
Created by vishnu on 30/3/18 11:00 AM
*/
public class ApplicationConstants {
    /**
     * The constant X_AUTH_TOKEN.
     */
    public static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";
    /**
     * The constant INFO.
     */
    public static final String INFO = "info";
    /**
     * The constant ERROR.
     */
    public static final String ERROR = "error";
    /**
     * The constant SUCCESS.
     */
    public static final String SUCCESS = "success";
    /**
     * The constant WARNING.
     */
    public static final String WARNING = "warning";
    /**
     * The constant USER_LOGGED_IN.
     */
    public static final String USER_LOGGED_IN = "userLoggedIn";
    /**
     * The constant MAX_COOKIE_AGE.
     */
    public static final Integer MAX_COOKIE_AGE = 24 * 60 * 60;
    /**
     * The constant CUSTOM_REQUEST_ID.
     */
    public static final String CUSTOM_REQUEST_ID = "customRequestId";

    /**
     * The constant USER.
     */
    public static final String USER = "USER-DETAILS";

    /**
     * The constant REQUEST_URL.
     */
    public static final String REQUEST_URL = "requestURL";

    /**
     * The constant REQUEST_TYPE.
     */
    public static final String REQUEST_TYPE = "requestType";

    /**
     * The constant REQUEST_HEADER.
     */
    public static final String REQUEST_HEADER = "requestHeader";

    /**
     * The constant PARAMETERS.
     */
    public static final String PARAMETERS = "parameters";

    /**
     * The constant KEY.
     */
    public static final String KEY = "key";

    /**
     * The constant AUTH.
     */
    public static final String AUTH = "auth";

    /**
     * The constant CONTENT_TYPE.
     */
    public static final String CONTENT_TYPE = "contentType";

    /**
     * The constant REST_EXCLUDE_INTERCEPTOR_PATTERN.
     */
    public static final String[] REST_EXCLUDE_REQUEST_INTERCEPTOR_PATTERN = {
            "/**/js/**/",
            "/**/css/**/",
            "/**/img/**/",
            "/**/font/**/",
            "/**/fonts/**/",
            "/**/webjars/**/",
            "/**/webjar/**/"
    };

    /**
     * The constant ADMIN_EXCLUDE_REQUEST_INTERCEPTOR_PATTERN.
     */
    public static final String[] ADMIN_EXCLUDE_REQUEST_INTERCEPTOR_PATTERN = {
            "/**/js/**/",
            "/**/css/**/",
            "/**/img/**/",
            "/**/font/**/",
            "/**/fonts/**/",
            "/**/webjars/**/",
            "/**/webjar/**/"
    };

    /**
     * The constant ADMIN_AUTHENTICATION_INTERCEPTOR_INCLUDE_PATTERN.
     */
    public static final String[] ADMIN_AUTHENTICATION_INTERCEPTOR_INCLUDE_PATTERN = {
            format("%s%s%s%s", ALL_URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, ALL_URL_PREFIX),
            format("%s%s%s%s", ALL_URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.Quartz.BASE_URI, ALL_URL_PREFIX)
    };
    /**
     * The constant ADMIN_AUTHENTICATION_INTERCEPTOR_EXCLUDE_PATTERN.
     */
    public static final String[] ADMIN_AUTHENTICATION_INTERCEPTOR_EXCLUDE_PATTERN = {
            format("%s%s%s%s", ALL_URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.USER_LOGIN_1),
            format("%s%s%s%s", ALL_URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.USER_LOGIN_2),
            format("%s%s%s%s", ALL_URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.FORGOT_PASSWORD),
            "/**/js/**/",
            "/**/css/**/",
            "/**/img/**/",
            "/**/font/**/",
            "/**/fonts/**/",
            "/**/webjars/**/",
            "/**/webjar/**/"
    };
    /**
     * The constant ADMIN_LOGIN_INTERCEPTOR_INCLUDE_PATTERN.
     */
    public static final String[] ADMIN_LOGIN_INTERCEPTOR_INCLUDE_PATTERN = new String[]{
            format("%s%s%s%s", ALL_URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.USER_LOGIN_1),
            format("%s%s%s%s", ALL_URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.USER_LOGIN_2)
    };
    /**
     * The constant ADMIN_LOGIN_INTERCEPTOR_EXCLUDE_PATTERN.
     */
    public static final String[] ADMIN_LOGIN_INTERCEPTOR_EXCLUDE_PATTERN = {
            "/**/js/**/",
            "/**/css/**/",
            "/**/img/**/",
            "/**/font/**/",
            "/**/fonts/**/",
            "/**/webjars/**/",
            "/**/webjar/**/"
    };
    /**
     * The constant ADMIN_LOGOUT_INTERCEPTOR_INCLUDE_PATTERN.
     */
    public static final String[] ADMIN_LOGOUT_INTERCEPTOR_INCLUDE_PATTERN = {
            format("%s%s%s%s", ALL_URL_PREFIX, Admin.Api.BASE_URI, Admin.Api.User.BASE_URI, Admin.Api.User.LOGOUT),
            format("%s%s%s%s", ALL_URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.LOGOUT)
    };
    /**
     * The constant ADMIN_LOGOUT_INTERCEPTOR_EXCLUDE_PATTERN.
     */
    public static final String[] ADMIN_LOGOUT_INTERCEPTOR_EXCLUDE_PATTERN = {
            "/**/js/**/",
            "/**/css/**/",
            "/**/img/**/",
            "/**/font/**/",
            "/**/fonts/**/",
            "/**/webjars/**/",
            "/**/webjar/**/"
    };
    /**
     * The constant ADMIN_USER_USERNAME.
     */
    public static final String ADMIN_USER_USERNAME = "admin";
    /**
     * The constant ADMIN_USER_PASSWORD.
     */
    public static final String ADMIN_USER_PASSWORD = "admin";
    /**
     * The constant ADMIN_USER_FIRSTNAME.
     */
    public static final String ADMIN_USER_FIRSTNAME = "admin";
    /**
     * The constant ADMIN_USER_LASTNAME.
     */
    public static final String ADMIN_USER_LASTNAME = "user";
    /**
     * The constant ADMIN_USER_EMAIL.
     */
    public static final String ADMIN_USER_EMAIL = "admin@admin.admin";

    /**
     * The constant KEY_NAME.
     */
    public static final String KEY_NAME = "keyName";

    /**
     * The constant USERNAME.
     */
    public static final String USERNAME = "username";

    /**
     * The constant TYPE.
     */
    public static final String TYPE = "type";

    /**
     * The constant HASHMAP_USER_KEY.
     */
    public static final String HASHMAP_USER_KEY = "user";
    /**
     * The constant HASHMAP_ERROR_KEY.
     */
    public static final String HASHMAP_ERROR_KEY = "error";

    public static final String REDIRECTED = "redirected";

    public static final String QUESTION_MARK = "?";

    public static final String EQUAL_TO = "=";
}
