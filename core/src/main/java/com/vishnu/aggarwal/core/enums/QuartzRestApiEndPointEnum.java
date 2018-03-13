package com.vishnu.aggarwal.core.enums;

/*
Created by vishnu on 9/3/18 11:08 AM
*/

import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.Getter;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.*;

/**
 * The enum Quartz rest api end point enum.
 */
@Getter
public enum QuartzRestApiEndPointEnum {

    /**
     * Create new job quartz rest api end point enum.
     */
    CREATE_NEW_JOB("/quartz/job", POST, RestResponseVO.class),
    /**
     * Create new trigger quartz rest api end point enum.
     */
    CREATE_NEW_TRIGGER("/quartz/trigger", POST, RestResponseVO.class),
    /**
     * Fetch jobs by group name quartz rest api end point enum.
     */
    FETCH_JOBS_BY_GROUP_NAME("/quartz/job/{groupName}", GET, DataTableVO.class),
    /**
     * Fetch triggers by job key name and group name quartz rest api end point enum.
     */
    FETCH_TRIGGERS_BY_JOB_KEY_NAME_AND_GROUP_NAME("/quartz/trigger/{jobKeyName}/{groupName}", GET, DataTableVO.class),
    /**
     * Fetch quartz details by group name quartz rest api end point enum.
     */
    FETCH_QUARTZ_DETAILS_BY_GROUP_NAME("/quartz/details/{groupName}", GET, DataTableVO.class),
    /**
     * Resume jobs quartz rest api end point enum.
     */
    RESUME_JOBS("/quartz/resume/jobs", asList(POST, PUT, PATCH), RestResponseVO.class),
    /**
     * Pause jobs quartz rest api end point enum.
     */
    PAUSE_JOBS("/quartz/pause/jobs", asList(POST, PUT, PATCH), RestResponseVO.class),
    /**
     * Resume triggers quartz rest api end point enum.
     */
    RESUME_TRIGGERS("/quartz/resume/triggers", asList(POST, PUT, PATCH), RestResponseVO.class),
    /**
     * Pause triggers quartz rest api end point enum.
     */
    PAUSE_TRIGGERS("/quartz/pause/triggers", asList(POST, PUT, PATCH), RestResponseVO.class),
    /**
     * Delete jobs quartz rest api end point enum.
     */
    DELETE_JOBS("/quartz/delete/jobs", DELETE, RestResponseVO.class),
    /**
     * Delete triggers quartz rest api end point enum.
     */
    DELETE_TRIGGERS("/quartz/delete/triggers", DELETE, RestResponseVO.class);

    private String apiEndPoint;
    private Object httpMethods;
    private Class<?> responseTypeClass;

    QuartzRestApiEndPointEnum(String apiEndPoint, Object httpMethods, Class<?> responseTypeClass) {
        this.apiEndPoint = apiEndPoint;
        this.httpMethods = httpMethods;
        this.responseTypeClass = responseTypeClass;
    }
}
