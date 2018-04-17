package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

/**
 * The enum Job type.
 */
@Getter
public enum JobType {
    /**
     * Api job type.
     */
    API("API");

    private String key;

    JobType(String key) {
        this.key = key;
    }

    public static JobType getJobType(String nameOrKey) {
        for (JobType jobType : values()) {
            if (jobType.name().equalsIgnoreCase(nameOrKey) || jobType.getKey().equalsIgnoreCase(nameOrKey)) {
                return jobType;
            }
        }
        return null;
    }
}
