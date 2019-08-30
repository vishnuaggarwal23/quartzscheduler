package com.vishnu.aggarwal.core.enums;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.Collection;

import static com.vishnu.aggarwal.core.enums.JobType.API;
import static com.vishnu.aggarwal.core.enums.JobType.SHELL_SCRIPT;
import static org.apache.commons.lang3.EnumUtils.getEnumList;
import static org.springframework.http.HttpMethod.*;

@Getter
public enum JobExecutorClass {
    GET_API_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.api.ApiGetRequestUtil", "ApiGetRequestUtil", GET, API),
    POST_API_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.api.ApiPostRequestUtil", "ApiPostRequestUtil", POST, API),
    DELETE_API_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.api.ApiDeleteRequestUtil", "ApiDeleteRequestUtil", DELETE, API),
    SHELL_SCRIPT_EXECUTOR("com.vishnu.aggarwal.rest.util.quartz.shell.ShellScriptRequestUtil", "ShellScriptRequestUtil", null, SHELL_SCRIPT);

    private String packageName;
    private String className;
    private HttpMethod httpMethod;
    private JobType associatedJobType;

    JobExecutorClass(String packageName, String className, HttpMethod httpMethod, JobType associatedJobType) {
        this.packageName = packageName;
        this.className = className;
        this.httpMethod = httpMethod;
        this.associatedJobType = associatedJobType;
    }

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
