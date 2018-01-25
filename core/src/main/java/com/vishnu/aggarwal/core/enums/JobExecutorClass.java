package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

@Getter
public enum JobExecutorClass {
    GET_API_EXECUTOR("com.vishnu.aggarwal.rest.util.ApiGetRequestUtil", "ApiGetRequestUtil"),
    POST_API_EXECUTOR("com.vishnu.aggarwal.rest.util.ApiPostRequestUtil", "ApiPostRequestUtil");

    private String packageName;
    private String className;

    JobExecutorClass(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public static JobExecutorClass findJobExecutorClassByValue(Class clazz) {
        for (JobExecutorClass jobExecutorClass : values()) {
            if (jobExecutorClass.getClassName().equalsIgnoreCase(clazz.getSimpleName()) || jobExecutorClass.getPackageName().equalsIgnoreCase(clazz.toString())) {
                return jobExecutorClass;
            }
        }
        return null;
    }
}
