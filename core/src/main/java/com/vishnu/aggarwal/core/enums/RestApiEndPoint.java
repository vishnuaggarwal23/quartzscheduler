package com.vishnu.aggarwal.core.enums;

/*
Created by vishnu on 9/3/18 11:08 AM
*/

import com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Quartz;
import com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.Getter;

import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.*;

/**
 * The enum Quartz rest api end point enum.
 */
@Getter
public enum RestApiEndPoint {

    /**
     * Create new job quartz rest api end point enum.
     */
    CREATE_NEW_JOB(getQuartzRestUrlEndPoint(Quartz.CREATE_JOB), POST, RestResponseVO.class),

    /**
     * Create new trigger quartz rest api end point enum.
     */
    CREATE_NEW_TRIGGER(getQuartzRestUrlEndPoint(Quartz.CREATE_TRIGGER), POST, RestResponseVO.class),

    /**
     * Update existing job rest api end point.
     */
    UPDATE_EXISTING_JOB(getQuartzRestUrlEndPoint(Quartz.UPDATE_TRIGGER), asList(PUT, PATCH), RestResponseVO.class),

    /**
     * Update existing trigger rest api end point.
     */
    UPDATE_EXISTING_TRIGGER(getQuartzRestUrlEndPoint(Quartz.UPDATE_TRIGGER), asList(PUT, PATCH), RestResponseVO.class),

    /**
     * Fetch jobs by group name quartz rest api end point enum.
     */
    FETCH_JOBS_BY_GROUP_NAME(getQuartzRestUrlEndPoint(Quartz.FETCH_JOB_BY_GROUP_NAME), GET, RestResponseVO.class),

    /**
     * Fetch triggers by job key name and group name quartz rest api end point enum.
     */
    FETCH_TRIGGERS_BY_JOB_KEY_NAME_AND_GROUP_NAME(getQuartzRestUrlEndPoint(Quartz.FETCH_TRIGGER_BY_JOB_KEY_GROUP_NAME), GET, RestResponseVO.class),

    /**
     * Fetch quartz details by group name quartz rest api end point enum.
     */
    FETCH_QUARTZ_DETAILS_BY_GROUP_NAME(getQuartzRestUrlEndPoint(Quartz.FETCH_QUARTZ_DETAILS_GROUP_NAME), GET, RestResponseVO.class),

    /**
     * Resume jobs quartz rest api end point enum.
     */
    RESUME_JOBS(getQuartzRestUrlEndPoint(Quartz.RESUME_JOBS), asList(PUT, PATCH), RestResponseVO.class),

    /**
     * Pause jobs quartz rest api end point enum.
     */
    PAUSE_JOBS(getQuartzRestUrlEndPoint(Quartz.PAUSE_JOBS), asList(PUT, PATCH), RestResponseVO.class),

    /**
     * Resume triggers quartz rest api end point enum.
     */
    RESUME_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.RESUME_TRIGGERS), asList(PUT, PATCH), RestResponseVO.class),

    /**
     * Pause triggers quartz rest api end point enum.
     */
    PAUSE_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.PAUSE_TRIGGERS), asList(PUT, PATCH), RestResponseVO.class),

    /**
     * Delete jobs quartz rest api end point enum.
     */
    DELETE_JOBS(getQuartzRestUrlEndPoint(Quartz.DELETE_JOBS), DELETE, RestResponseVO.class),

    /**
     * Delete triggers quartz rest api end point enum.
     */
    DELETE_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.DELETE_TRIGGERS), DELETE, RestResponseVO.class),

    /**
     * User authentication rest api end point.
     */
    USER_AUTHENTICATION(getUserRestUrlEndPoint(AUTHENTICATE), GET, RestResponseVO.class),

    /**
     * Current logged in user rest api end point.
     */
    CURRENT_LOGGED_IN_USER(getUserRestUrlEndPoint(User.CURRENT_LOGGED_IN_USER), GET, RestResponseVO.class),

    /**
     * Login user rest api end point.
     */
    LOGIN_USER(getUserRestUrlEndPoint(LOGIN), POST, RestResponseVO.class),

    /**
     * Logout user rest api end point.
     */
    LOGOUT_USER(getUserRestUrlEndPoint(LOGOUT), POST, RestResponseVO.class);

    private String apiEndPoint;
    private Object httpMethods;
    private Class<?> responseTypeClass;

    RestApiEndPoint(String apiEndPoint, Object httpMethods, Class<?> responseTypeClass) {
        this.apiEndPoint = apiEndPoint;
        this.httpMethods = httpMethods;
        this.responseTypeClass = responseTypeClass;
    }

    private static String getQuartzRestUrlEndPoint(String uri) {
        return format("%s%s", Quartz.BASE_URI, uri);
    }

    private static String getUserRestUrlEndPoint(String uri) {
        return format("%s%s", User.BASE_URI, uri);
    }
}
