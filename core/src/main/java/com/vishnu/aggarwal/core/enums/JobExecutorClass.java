package com.vishnu.aggarwal.core.enums;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.Collection;

import static org.apache.commons.lang3.EnumUtils.getEnumList;
import static org.springframework.http.HttpMethod.*;

/**
 * The enum Job executor class.
 */
@Getter
public enum JobExecutorClass {
    /**
     * Get api executor job executor class.
     */
    GET_API_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.api.ApiGetRequestUtil", "ApiGetRequestUtil", GET),
    /**
     * Post api executor job executor class.
     */
    POST_API_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.api.ApiPostRequestUtil", "ApiPostRequestUtil", POST),

    /**
     * Delete api executor job executor class.
     */
    DELETE_API_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.api.ApiDeleteRequestUtil", "ApiDeleteRequestUtil", DELETE),

    SHELL_SCRIPT_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.shell.ShellScriptRequestUtil", "ShellScriptRequestUtil", null);

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

    public static Collection<JobExecutorClass> getAll() {
        return getEnumList(JobExecutorClass.class);
    }
}
