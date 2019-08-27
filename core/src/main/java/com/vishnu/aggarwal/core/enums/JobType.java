package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

import java.util.Collection;

import static org.apache.commons.lang3.EnumUtils.getEnumList;

/**
 * The enum Job type.
 */
@Getter
public enum JobType {
    /**
     * Api job type.
     */
    API(com.vishnu.aggarwal.core.constants.JobType.API, com.vishnu.aggarwal.core.constants.JobType.api, com.vishnu.aggarwal.core.constants.JobType.FRAGMENT_API),
    SHELL_SCRIPT(com.vishnu.aggarwal.core.constants.JobType.SHELL_SCRIPT, com.vishnu.aggarwal.core.constants.JobType.shell_script, com.vishnu.aggarwal.core.constants.JobType.FRAGMENT_SCRIPT);

    private String key;
    private String urlKey;
    private String fragment;

    JobType(String key, String urlKey, String fragment) {
        this.key = key;
        this.urlKey = urlKey;
        this.fragment = fragment;
    }

    /**
     * Gets job type.
     *
     * @param nameOrKey the name or key
     * @return the job type
     */
    public static JobType getJobType(String nameOrKey) {
        for (JobType jobType : values()) {
            if (jobType.name().equalsIgnoreCase(nameOrKey) || jobType.getKey().equalsIgnoreCase(nameOrKey)) {
                return jobType;
            }
        }
        return null;
    }

    public static Collection<JobType> getAll() {
        return getEnumList(JobType.class);
    }
}
