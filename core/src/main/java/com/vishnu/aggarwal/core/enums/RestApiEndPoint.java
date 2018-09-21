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
import static org.springframework.http.HttpMethod.*;

/**
 * The enum Quartz rest api end point enum.
 */
@Getter
public enum RestApiEndPoint {

    /**
     * Create new job quartz rest api end point enum.
     */
    CREATE_API_JOB(getQuartzRestUrlEndPoint(Quartz.CREATE_API_JOB), POST),
    /**
     * Create api job scheduled simple rest api end point.
     */
    CREATE_API_JOB_SCHEDULED_SIMPLE(getQuartzRestUrlEndPoint(Quartz.CREATE_API_JOB_SCHEDULED_SIMPLE), POST),
    /**
     * Create api job scheduled cron rest api end point.
     */
    CREATE_API_JOB_SCHEDULED_CRON(getQuartzRestUrlEndPoint(Quartz.CREATE_API_JOB_SCHEDULED_CRON), POST),

    /**
     * Create new trigger quartz rest api end point enum.
     */
    CREATE_SIMPLE_TRIGGER(getQuartzRestUrlEndPoint(Quartz.CREATE_SIMPLE_TRIGGER), POST),
    /**
     * Create cron trigger rest api end point.
     */
    CREATE_CRON_TRIGGER(getQuartzRestUrlEndPoint(Quartz.CREATE_CRON_TRIGGER), POST),

    /**
     * Update existing job rest api end point.
     */
    UPDATE_API_JOB(getQuartzRestUrlEndPoint(Quartz.UPDATE_API_JOB), PUT),

    /**
     * Update existing trigger rest api end point.
     */
    UPDATE_SIMPLE_TRIGGER(getQuartzRestUrlEndPoint(Quartz.UPDATE_SIMPLE_TRIGGER), PUT),
    /**
     * Update cron trigger rest api end point.
     */
    UPDATE_CRON_TRIGGER(getQuartzRestUrlEndPoint(Quartz.UPDATE_CRON_TRIGGER), PUT),

    /**
     * Fetch jobs by group name quartz rest api end point enum.
     */
    FETCH_JOBS_OF_CURRENT_USER_GROUP(getQuartzRestUrlEndPoint(Quartz.FETCH_JOBS_OF_CURRENT_USER_GROUP), GET),

    /**
     * Fetch triggers by job key name and group name quartz rest api end point enum.
     */
    FETCH_TRIGGERS_BY_JOB_KEY_AND_CURRENT_USER_GROUP(getQuartzRestUrlEndPoint(Quartz.FETCH_TRIGGERS_BY_JOB_KEY_AND_CURRENT_USER_GROUP), GET),

    /**
     * Fetch quartz details by group name quartz rest api end point enum.
     */
    FETCH_QUARTZ_DETAILS_BY_CURRENT_USER_GROUP(getQuartzRestUrlEndPoint(Quartz.FETCH_QUARTZ_DETAILS_BY_CURRENT_USER_GROUP), GET),

    /**
     * Resume jobs quartz rest api end point enum.
     */
    RESUME_JOBS(getQuartzRestUrlEndPoint(Quartz.RESUME_JOBS), PUT),

    /**
     * Pause jobs quartz rest api end point enum.
     */
    PAUSE_JOBS(getQuartzRestUrlEndPoint(Quartz.PAUSE_JOBS), PUT),

    /**
     * Resume triggers quartz rest api end point enum.
     */
    RESUME_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.RESUME_TRIGGERS), PUT),

    /**
     * Pause triggers quartz rest api end point enum.
     */
    PAUSE_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.PAUSE_TRIGGERS), PUT),

    /**
     * Delete jobs quartz rest api end point enum.
     */
    DELETE_JOBS(getQuartzRestUrlEndPoint(Quartz.DELETE_JOBS), DELETE),

    /**
     * Delete triggers quartz rest api end point enum.
     */
    DELETE_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.DELETE_TRIGGERS), DELETE),

    /**
     * User authentication rest api end point.
     */
    AUTHENTICATE(getUserRestUrlEndPoint(User.AUTHENTICATE), GET),

    /**
     * Current logged in user rest api end point.
     */
    CURRENT_LOGGED_IN_USER(getUserRestUrlEndPoint(User.CURRENT_LOGGED_IN_USER), GET),

    /**
     * Login user rest api end point.
     */
    LOGIN(getUserRestUrlEndPoint(User.LOGIN), POST),

    /**
     * Logout user rest api end point.
     */
    LOGOUT(getUserRestUrlEndPoint(User.LOGOUT), POST),

    /**
     * Unique job key per group rest api end point.
     */
    UNIQUE_JOB_KEY_PER_GROUP(getValidationRestUrlEndPoint(Validation.UNIQUE_JOB_KEY_PER_GROUP), GET),

    /**
     * Unique trigger key per group rest api end point.
     */
    UNIQUE_TRIGGER_KEY_PER_GROUP(getValidationRestUrlEndPoint(Validation.UNIQUE_TRIGGER_KEY_PER_GROUP), GET),

    JOB_KEYS_AUTOCOMPLETE(getQuartzRestUrlEndPoint(Quartz.JOB_KEYS_AUTOCOMPLETE), GET);

    private String apiEndPoint;
    private HttpMethod httpMethod;

    RestApiEndPoint(String apiEndPoint, HttpMethod httpMethod) {
        this.apiEndPoint = apiEndPoint;
        this.httpMethod = httpMethod;
    }

    private static String getQuartzRestUrlEndPoint(String uri) {
        return format("%s%s", Quartz.BASE_URI, uri);
    }

    private static String getUserRestUrlEndPoint(String uri) {
        return format("%s%s", User.BASE_URI, uri);
    }

    private static String getValidationRestUrlEndPoint(String uri) {
        return format("%s%s", Validation.BASE_URI, uri);
    }
}
