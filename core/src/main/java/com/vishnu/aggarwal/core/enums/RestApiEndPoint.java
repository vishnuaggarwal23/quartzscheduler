package com.vishnu.aggarwal.core.enums;

/*
Created by vishnu on 9/3/18 11:08 AM
*/

import com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Quartz;
import com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.Map;

import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.User.*;
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
    CREATE_NEW_JOB(getQuartzRestUrlEndPoint(Quartz.CREATE_JOB), POST, Map.class),

    /**
     * Create new trigger quartz rest api end point enum.
     */
    CREATE_NEW_TRIGGER(getQuartzRestUrlEndPoint(Quartz.CREATE_TRIGGER), POST, RestResponseVO.class),

    /**
     * Update existing job rest api end point.
     */
    UPDATE_EXISTING_JOB(getQuartzRestUrlEndPoint(Quartz.UPDATE_TRIGGER), PUT, RestResponseVO.class),

    /**
     * Update existing trigger rest api end point.
     */
    UPDATE_EXISTING_TRIGGER(getQuartzRestUrlEndPoint(Quartz.UPDATE_TRIGGER), PUT, RestResponseVO.class),

    /**
     * Fetch jobs by group name quartz rest api end point enum.
     */
    FETCH_JOBS_BY_GROUP_NAME(getQuartzRestUrlEndPoint(Quartz.FETCH_JOB_BY_JOB_GROUP_NAME), GET, DataTableVO.class),

    /**
     * Fetch triggers by job key name and group name quartz rest api end point enum.
     */
    FETCH_TRIGGERS_BY_JOB_KEY_NAME_AND_GROUP_NAME(getQuartzRestUrlEndPoint(Quartz.FETCH_TRIGGER_BY_JOB_KEY_JOB_GROUP_NAME), GET, DataTableVO.class),

    /**
     * Fetch quartz details by group name quartz rest api end point enum.
     */
    FETCH_QUARTZ_DETAILS_BY_GROUP_NAME(getQuartzRestUrlEndPoint(Quartz.FETCH_QUARTZ_DETAILS_JOB_GROUP_NAME), GET, DataTableVO.class),

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
    USER_AUTHENTICATION(getUserRestUrlEndPoint(AUTHENTICATE), GET, UserAuthenticationDTO.class),

    /**
     * Current logged in user rest api end point.
     */
    CURRENT_LOGGED_IN_USER(getUserRestUrlEndPoint(User.CURRENT_LOGGED_IN_USER), GET, UserDTO.class),

    /**
     * Login user rest api end point.
     */
    LOGIN_USER(getUserRestUrlEndPoint(LOGIN), POST, UserAuthenticationDTO.class),

    /**
     * Logout user rest api end point.
     */
    LOGOUT_USER(getUserRestUrlEndPoint(LOGOUT), POST, RestResponseVO.class);

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
