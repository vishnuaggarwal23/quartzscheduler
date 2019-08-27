package com.vishnu.aggarwal.core.enums;

/*
Created by vishnu on 9/3/18 11:08 AM
*/

import com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Quartz;
import com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User;
import com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Validation;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpMethod.*;

@Getter
public enum RestApiEndPoint {

    CREATE_JOB(getQuartzRestUrlEndPoint(Quartz.CREATE_UPDATE_JOB), POST),
    UPDATE_JOB(getQuartzRestUrlEndPoint(Quartz.CREATE_UPDATE_JOB), PUT),
    DELETE_JOB(getQuartzRestUrlEndPoint(Quartz.DELETE_JOB), DELETE),
    DELETE_JOBS(getQuartzRestUrlEndPoint(Quartz.DELETE_JOBS), DELETE),
    RESUME_JOB(getQuartzRestUrlEndPoint(Quartz.RESUME_JOB), PUT),
    RESUME_JOBS(getQuartzRestUrlEndPoint(Quartz.RESUME_JOBS), PUT),
    PAUSE_JOB(getQuartzRestUrlEndPoint(Quartz.PAUSE_JOB), PUT),
    PAUSE_JOBS(getQuartzRestUrlEndPoint(Quartz.PAUSE_JOBS), PUT),
    SHOW_JOB(getQuartzRestUrlEndPoint(Quartz.SHOW_JOB), GET),
    LIST_JOBS(getQuartzRestUrlEndPoint(Quartz.LIST_JOBS), GET),

    CREATE_TRIGGER(getQuartzRestUrlEndPoint(Quartz.CREATE_UPDATE_TRIGGER), POST),
    UPDATE_TRIGGER(getQuartzRestUrlEndPoint(Quartz.CREATE_UPDATE_TRIGGER), PUT),
    DELETE_TRIGGER(getQuartzRestUrlEndPoint(Quartz.DELETE_TRIGGER), DELETE),
    DELETE_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.DELETE_TRIGGERS), DELETE),
    RESUME_TRIGGER(getQuartzRestUrlEndPoint(Quartz.RESUME_TRIGGER), PUT),
    RESUME_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.RESUME_TRIGGERS), PUT),
    PAUSE_TRIGGER(getQuartzRestUrlEndPoint(Quartz.PAUSE_TRIGGER), PUT),
    PAUSE_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.PAUSE_TRIGGERS), PUT),
    SHOW_TRIGGER(getQuartzRestUrlEndPoint(Quartz.SHOW_TRIGGER), GET),
    LIST_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.LIST_TRIGGERS), GET),

    LIST_QUARTZ_DETAILS(getQuartzRestUrlEndPoint(Quartz.LIST_QUARTZ_DETAILS), GET),

    JOB_KEYS_AUTOCOMPLETE(getQuartzRestUrlEndPoint(Quartz.JOB_KEYS_AUTOCOMPLETE), GET),

    AUTHENTICATE(getUserRestUrlEndPoint(User.AUTHENTICATE), GET),

    CURRENT_LOGGED_IN_USER(getUserRestUrlEndPoint(User.CURRENT_LOGGED_IN_USER), GET),

    LOGIN(getUserRestUrlEndPoint(User.LOGIN), POST),

    LOGOUT(getUserRestUrlEndPoint(User.LOGOUT), POST),

    UNIQUE_JOB_KEY_PER_GROUP(getValidationRestUrlEndPoint(Validation.UNIQUE_JOB_KEY_PER_GROUP), GET),

    UNIQUE_TRIGGER_KEY_PER_GROUP(getValidationRestUrlEndPoint(Validation.UNIQUE_TRIGGER_KEY_PER_GROUP), GET);

    private String apiEndPoint;
    private HttpMethod httpMethod;

    RestApiEndPoint(String apiEndPoint, HttpMethod httpMethod) {
        this.apiEndPoint = apiEndPoint;
        this.httpMethod = httpMethod;
    }

    private static String getQuartzRestUrlEndPoint(String uri) {
        return getQuartzRestUrlEndPoint(uri, EMPTY);
    }

    private static String getQuartzRestUrlEndPoint(String uri, String additionalInfo) {
        return format("%s%s%s", Quartz.BASE_URI, uri, additionalInfo);
    }

    private static String getUserRestUrlEndPoint(String uri) {
        return format("%s%s", User.BASE_URI, uri);
    }

    private static String getValidationRestUrlEndPoint(String uri) {
        return format("%s%s", Validation.BASE_URI, uri);
    }
}
