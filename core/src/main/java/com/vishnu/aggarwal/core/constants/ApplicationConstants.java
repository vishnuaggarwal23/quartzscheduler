package com.vishnu.aggarwal.core.constants;

import com.vishnu.aggarwal.core.constants.UrlMapping.Admin;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static lombok.AccessLevel.PRIVATE;

/*
Created by vishnu on 30/3/18 11:00 AM
*/
@NoArgsConstructor(access = PRIVATE)
public class ApplicationConstants {
    public static final String URL_PREFIX = "/**";
    public static final String URL_SUFFIX = "**/";
    public static final String BACKSLASH = "/";
    public static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";
    public static final String LOGGED_IN_USER_ID = "CURRENT_LOGGED_IN_USER_ID";
    public static final String LOGGED_IN_USER = "CURRENT_LOGGED_IN_USER";
    public static final String INFO = "info";
    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    public static final String WARNING = "warning";
    public static final String USER_LOGGED_IN = "userLoggedIn";
    public static final Integer MAX_COOKIE_AGE = 24 * 60 * 60;
    public static final String CUSTOM_REQUEST_ID = "customRequestId";
    public static final String REQUEST_URL = "requestURL";
    public static final String REQUEST_TYPE = "requestType";
    public static final String REQUEST_HEADER = "requestHeader";
    public static final String CREATED_DATE = "createdDate";
    public static final String UPDATED_DATE = "updatedDate";
    public static final String PARAMETERS = "parameters";
    public static final String KEY = "key";
    public static final String AUTH = "auth";
    public static final String CONTENT_TYPE = "contentType";
    public static final String JS = "js";
    public static final String CSS = "css";
    public static final String IMG = "img";
    public static final String FONT = "font";
    public static final String FONTS = "fonts";
    public static final String WEBJAR = "webjar";
    public static final String WEBJARS = "webjars";
    public static final String ACTUATOR = "actuator";
    public static final String SYSTEM = "system";
    public static final String MONITOR = "monitor";
    public static final String ADMIN_USER_USERNAME = "admin";
    public static final String ADMIN_USER_PASSWORD = "admin";
    public static final String ADMIN_USER_FIRSTNAME = "admin";
    public static final String ADMIN_USER_LASTNAME = "user";
    public static final String ADMIN_USER_EMAIL = "admin@admin.admin";
    public static final String KEY_NAME = "keyName";
    public static final String USERNAME = "username";
    public static final String TYPE = "type";
    public static final String HASHMAP_USER_KEY = "user";
    public static final String HASHMAP_ERROR_KEY = "error";
    public static final String REDIRECTED = "redirected";
    public static final String QUESTION_MARK = "?";
    public static final String EQUAL_TO = "=";
    public static final String HYPHEN = "-";
    public static final String JOB_KEY = "jobKey";
    public static final String TRIGGER_TYPE = "triggerType";
    public static final String NEW_FORM = "newForm";
    public static final String JOB_TYPE = "jobType";
    public static final String INVALID_JOB_TYPE = "invalidJobType";
    public static final String INVALID_TRIGGER_TYPE = "invalidTriggerType";
    public static final String INVALID_FRAGMENT = "invalidFragment";
    public static final String EMPTY_FRAGMENT = "emptyFragment";
    public static final String INVALID_NEW_FORM = "invalidNewFormRequest";
    public static final String TRIGGER_KEY = "triggerKey";
    public static final String SEARCH_TEXT = "searchText";
    public static final String MDC_TOKEN = "mdcToken";
    public static final String CONTAINS_SHELL_SCRIPT = "containsShellScript";
    public static final String SHELL_SCRIPT_PATH = "shellScriptPath";
    public static final String USER_DIR_PATH = "userDirectory";
    public static final String ACTION = "action";
    public static final String DELETED = "deleted";
    public static final String RESUMED = "resumed";
    public static final String PAUSED = "paused";
    public static final String JOB = "job";
    public static final String TRIGGER = "trigger";
    public static final String QUARTZ = "quartz";
    public static final String ALL = "all";
    public static final String DETAILS = "details";
    public static final String VALIDATION = "validation";
    public static final String UNIQUE = "unique";
    public static final String USER = "user";
    public static final String AUTHENTICATE = "authenticate";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String CURRENT = "current";
    public static final String DASHBOARD = "dashboard";
    public static final String COUNTER = "counter";
    public static final String AUTO_COMPLETE = "autocomplete";
    public static final String API = "api";
    public static final String WEB = "web";
    public static final String FORGOT_PASSWORD = "forgotPassword";
    public static final String UTF8 = "UTF-8";
    public static final String UTC = "UTC";
    public static final String FRAGMENT = "fragment";
    public static final String JS_REGEX = URL_PREFIX + BACKSLASH + JS + BACKSLASH + URL_SUFFIX;
    public static final String CSS_REGEX = URL_PREFIX + BACKSLASH + CSS + BACKSLASH + URL_SUFFIX;
    public static final String IMG_REGEX = URL_PREFIX + BACKSLASH + IMG + BACKSLASH + URL_SUFFIX;
    public static final String FONT_REGEX = URL_PREFIX + BACKSLASH + FONT + BACKSLASH + URL_SUFFIX;
    public static final String FONTS_REGEX = URL_PREFIX + BACKSLASH + FONTS + BACKSLASH + URL_SUFFIX;
    public static final String WEBJAR_REGEX = URL_PREFIX + BACKSLASH + WEBJAR + BACKSLASH + URL_SUFFIX;
    public static final String WEBJARS_REGEX = URL_PREFIX + BACKSLASH + WEBJARS + BACKSLASH + URL_SUFFIX;
    public static final String ACTUATOR_REGEX = URL_PREFIX + BACKSLASH + ACTUATOR + BACKSLASH + URL_SUFFIX;
    public static final String SYSTEM_MONITOR_REGEX = URL_PREFIX + BACKSLASH + SYSTEM + BACKSLASH + MONITOR + BACKSLASH + URL_SUFFIX;
    public static final String ERROR_REGEX = URL_PREFIX + BACKSLASH + ERROR;
    public static final String USER_LOGOUT_REGEX = URL_PREFIX + BACKSLASH + USER + BACKSLASH + LOGOUT;
    public static final String USER_LOGIN_REGEX = URL_PREFIX + BACKSLASH + USER + BACKSLASH + LOGIN;
    public static final String USER_AUTHENTICATE_REGEX = URL_PREFIX + BACKSLASH + USER + BACKSLASH + AUTHENTICATE;
    public static final String QUARTZ_REGEX = URL_PREFIX + BACKSLASH + QUARTZ + BACKSLASH + URL_SUFFIX;
    public static final String VALIDATION_REGEX = URL_PREFIX + BACKSLASH + VALIDATION + BACKSLASH + URL_SUFFIX;
    public static final String[] REST_EXCLUDE_REQUEST_INTERCEPTOR_PATTERN = {
            JS_REGEX,
            CSS_REGEX,
            IMG_REGEX,
            FONT_REGEX,
            FONTS_REGEX,
            WEBJAR_REGEX,
            WEBJARS_REGEX
    };
    public static final String[] ADMIN_EXCLUDE_REQUEST_INTERCEPTOR_PATTERN = {
            JS_REGEX,
            CSS_REGEX,
            IMG_REGEX,
            FONT_REGEX,
            FONTS_REGEX,
            WEBJAR_REGEX,
            WEBJARS_REGEX
    };
    public static final String[] SPRING_SECURITY_EXCLUDED_PATHS = {
            JS_REGEX,
            CSS_REGEX,
            IMG_REGEX,
            FONT_REGEX,
            FONTS_REGEX,
            WEBJAR_REGEX,
            WEBJARS_REGEX,
            ACTUATOR_REGEX,
            SYSTEM_MONITOR_REGEX
    };
    public static final String[] ADMIN_AUTHENTICATION_INTERCEPTOR_INCLUDE_PATTERN = {
            format("%s%s%s%s", URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, URL_PREFIX),
            format("%s%s%s%s", URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.Quartz.BASE_URI, URL_PREFIX)
    };
    public static final String[] ADMIN_AUTHENTICATION_INTERCEPTOR_EXCLUDE_PATTERN = {
            format("%s%s%s%s", URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.USER_LOGIN_1),
            format("%s%s%s%s", URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.USER_LOGIN_2),
            format("%s%s%s%s", URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.FORGOT_PASSWORD),
            JS_REGEX,
            CSS_REGEX,
            IMG_REGEX,
            FONT_REGEX,
            FONTS_REGEX,
            WEBJAR_REGEX,
            WEBJARS_REGEX
    };
    public static final String[] ADMIN_LOGIN_INTERCEPTOR_INCLUDE_PATTERN = {
            format("%s%s%s%s", URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.USER_LOGIN_1),
            format("%s%s%s%s", URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.USER_LOGIN_2)
    };
    public static final String[] ADMIN_LOGIN_INTERCEPTOR_EXCLUDE_PATTERN = {
            JS_REGEX,
            CSS_REGEX,
            IMG_REGEX,
            FONT_REGEX,
            FONTS_REGEX,
            WEBJAR_REGEX,
            WEBJARS_REGEX
    };
    public static final String[] ADMIN_LOGOUT_INTERCEPTOR_INCLUDE_PATTERN = {
            format("%s%s%s%s", URL_PREFIX, Admin.Api.BASE_URI, Admin.Api.User.BASE_URI, Admin.Api.User.LOGOUT),
            format("%s%s%s%s", URL_PREFIX, Admin.Web.BASE_URI, Admin.Web.User.BASE_URI, Admin.Web.User.LOGOUT)
    };
    public static final String[] ADMIN_LOGOUT_INTERCEPTOR_EXCLUDE_PATTERN = {
            JS_REGEX,
            CSS_REGEX,
            IMG_REGEX,
            FONT_REGEX,
            FONTS_REGEX,
            WEBJAR_REGEX,
            WEBJARS_REGEX
    };
    public static final List<String> FRAGMENTS_JOB = asList(
            "JOB_TYPE_" + JobType.API,
            "JOB_TYPE_" + JobType.SHELL_SCRIPT,
            "JOB_TYPE_NONE",
            "JOB_FORM_NEW",
            "JOB_FORM_NONE"
    );
    public static final List<String> FRAGMENTS_TRIGGER = asList(
            "TRIGGER_TYPE_" + ScheduleType.SIMPLE,
            "TRIGGER_TYPE_" + ScheduleType.CRON,
            "TRIGGER_TYPE_NONE",
            "TRIGGER_FORM_NEW",
            "TRIGGER_FORM_NONE"
    );
    public static final List<String> LOG_TYPES = asList(
            INFO,
            WARNING,
            ERROR,
            SUCCESS
    );
}
