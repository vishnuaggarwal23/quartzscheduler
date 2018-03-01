package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

/**
 * The enum Job executor class.
 */
@Getter
public enum JobExecutorClass {
    /**
     * Get api executor job executor class.
     */
    GET_API_EXECUTOR("com.vishnu.aggarwal.rest.util.ApiGetRequestUtil", "ApiGetRequestUtil"),
    /**
     * Post api executor job executor class.
     */
    POST_API_EXECUTOR("com.vishnu.aggarwal.rest.util.ApiPostRequestUtil", "ApiPostRequestUtil");

    private String packageName;
    private String className;

    JobExecutorClass(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    /**
     * Find job executor class by value job executor class.
     *
     * @param clazz the clazz
     * @return the job executor class
     */
    public static JobExecutorClass findJobExecutorClassByValue(Class clazz) {
        for (JobExecutorClass jobExecutorClass : values()) {
            if (jobExecutorClass.getClassName().equalsIgnoreCase(clazz.getSimpleName()) || jobExecutorClass.getPackageName().equalsIgnoreCase(clazz.toString())) {
                return jobExecutorClass;
            }
        }
        return null;
    }
}
