package com.vishnu.aggarwal.core.enums;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import static org.springframework.http.HttpMethod.*;

/**
 * The enum Job executor class.
 */
@Getter
public enum JobExecutorClass {
    /**
     * Get api executor job executor class.
     */
    GET_API_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.ApiGetRequestUtil", "ApiGetRequestUtil", GET),
    /**
     * Post api executor job executor class.
     */
    POST_API_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.ApiPostRequestUtil", "ApiPostRequestUtil", POST),

    /**
     * Delete api executor job executor class.
     */
    DELETE_API_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.ApiDeleteRequestUtil", "ApiDeleteRequestUtil", DELETE);

    private String packageName;
    private String className;
    private HttpMethod httpMethod;

    JobExecutorClass(String packageName, String className, HttpMethod httpMethod) {
        this.packageName = packageName;
        this.className = className;
        this.httpMethod = httpMethod;
    }

    /**
     * Find job executor class by value job executor class.
     *
     * @param clazz the clazz
     * @return the job executor class
     */
    public static JobExecutorClass findJobExecutorClassByValue(final Class clazz) {
        for (JobExecutorClass jobExecutorClass : values()) {
            if (jobExecutorClass.getClassName().equalsIgnoreCase(clazz.getSimpleName()) || jobExecutorClass.getPackageName().equalsIgnoreCase(clazz.toString())) {
                return jobExecutorClass;
            }
        }
        return null;
    }
}
