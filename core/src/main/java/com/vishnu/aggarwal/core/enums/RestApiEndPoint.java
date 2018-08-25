package com.vishnu.aggarwal.core.enums;

/*
Created by vishnu on 9/3/18 11:08 AM
*/

import com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Quartz;
import com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.Map;

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
    CREATE_API_JOB(getQuartzRestUrlEndPoint(Quartz.CREATE_API_JOB), POST, Map.class),
    /**
     * Create api job scheduled simple rest api end point.
     */
    CREATE_API_JOB_SCHEDULED_SIMPLE(getQuartzRestUrlEndPoint(Quartz.CREATE_API_JOB_SCHEDULED_SIMPLE), POST, Map.class),
    /**
     * Create api job scheduled cron rest api end point.
     */
    CREATE_API_JOB_SCHEDULED_CRON(getQuartzRestUrlEndPoint(Quartz.CREATE_API_JOB_SCHEDULED_CRON), POST, Map.class),

    /**
     * Create new trigger quartz rest api end point enum.
     */
    CREATE_SIMPLE_TRIGGER(getQuartzRestUrlEndPoint(Quartz.CREATE_SIMPLE_TRIGGER), POST, Map.class),
    /**
     * Create cron trigger rest api end point.
     */
    CREATE_CRON_TRIGGER(getQuartzRestUrlEndPoint(Quartz.CREATE_CRON_TRIGGER), POST, Map.class),

    /**
     * Update existing job rest api end point.
     */
    UPDATE_API_JOB(getQuartzRestUrlEndPoint(Quartz.UPDATE_API_JOB), PUT, Map.class),

    /**
     * Update existing trigger rest api end point.
     */
    UPDATE_SIMPLE_TRIGGER(getQuartzRestUrlEndPoint(Quartz.UPDATE_SIMPLE_TRIGGER), PUT, Map.class),
    /**
     * Update cron trigger rest api end point.
     */
    UPDATE_CRON_TRIGGER(getQuartzRestUrlEndPoint(Quartz.UPDATE_CRON_TRIGGER), PUT, Map.class),

    /**
     * Fetch jobs by group name quartz rest api end point enum.
     */
    FETCH_JOB_BY_JOB_GROUP_NAME(getQuartzRestUrlEndPoint(Quartz.FETCH_JOB_BY_JOB_GROUP_NAME), GET, DataTableVO.class),

    /**
     * Fetch triggers by job key name and group name quartz rest api end point enum.
     */
    FETCH_TRIGGER_BY_JOB_KEY_JOB_GROUP_NAME(getQuartzRestUrlEndPoint(Quartz.FETCH_TRIGGER_BY_JOB_KEY_JOB_GROUP_NAME), GET, DataTableVO.class),

    /**
     * Fetch quartz details by group name quartz rest api end point enum.
     */
    FETCH_QUARTZ_DETAILS_JOB_GROUP_NAME(getQuartzRestUrlEndPoint(Quartz.FETCH_QUARTZ_DETAILS_JOB_GROUP_NAME), GET, DataTableVO.class),

    /**
     * Resume jobs quartz rest api end point enum.
     */
    RESUME_JOBS(getQuartzRestUrlEndPoint(Quartz.RESUME_JOBS), PUT, Map.class),

    /**
     * Pause jobs quartz rest api end point enum.
     */
    PAUSE_JOBS(getQuartzRestUrlEndPoint(Quartz.PAUSE_JOBS), PUT, Map.class),

    /**
     * Resume triggers quartz rest api end point enum.
     */
    RESUME_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.RESUME_TRIGGERS), PUT, Map.class),

    /**
     * Pause triggers quartz rest api end point enum.
     */
    PAUSE_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.PAUSE_TRIGGERS), PUT, Map.class),

    /**
     * Delete jobs quartz rest api end point enum.
     */
    DELETE_JOBS(getQuartzRestUrlEndPoint(Quartz.DELETE_JOBS), DELETE, Map.class),

    /**
     * Delete triggers quartz rest api end point enum.
     */
    DELETE_TRIGGERS(getQuartzRestUrlEndPoint(Quartz.DELETE_TRIGGERS), DELETE, Map.class),

    /**
     * User authentication rest api end point.
     */
    AUTHENTICATE(getUserRestUrlEndPoint(User.AUTHENTICATE), GET, UserAuthenticationDTO.class),

    /**
     * Current logged in user rest api end point.
     */
    CURRENT_LOGGED_IN_USER(getUserRestUrlEndPoint(User.CURRENT_LOGGED_IN_USER), GET, UserDTO.class),

    /**
     * Login user rest api end point.
     */
    LOGIN(getUserRestUrlEndPoint(User.LOGIN), POST, UserAuthenticationDTO.class),

    /**
     * Logout user rest api end point.
     */
    LOGOUT(getUserRestUrlEndPoint(User.LOGOUT), POST, Object.class);

    private String apiEndPoint;
    private HttpMethod httpMethod;
    private Class responseTypeClass;

    RestApiEndPoint(String apiEndPoint, HttpMethod httpMethod, Class responseTypeClass) {
        this.apiEndPoint = apiEndPoint;
        this.httpMethod = httpMethod;
        this.responseTypeClass = responseTypeClass;
    }

    private static String getQuartzRestUrlEndPoint(String uri) {
        return format("%s%s", Quartz.BASE_URI, uri);
    }

    private static String getUserRestUrlEndPoint(String uri) {
        return format("%s%s", User.BASE_URI, uri);
    }
}
