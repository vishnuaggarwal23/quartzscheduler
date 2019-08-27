package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

/**
 * The enum Schedule type.
 */
@Getter
public enum ScheduleType {
    /**
     * Simple schedule type.
     */
    SIMPLE(com.vishnu.aggarwal.core.constants.ScheduleType.SIMPLE, com.vishnu.aggarwal.core.constants.ScheduleType.simple),
    /**
     * Cron schedule type.
     */
    CRON(com.vishnu.aggarwal.core.constants.ScheduleType.CRON, com.vishnu.aggarwal.core.constants.ScheduleType.cron);

    private String key;
    private String urlKey;

    ScheduleType(String key, String urlKey) {
        this.key = key;
        this.urlKey = urlKey;
    }

    public static ScheduleType getScheduleType(String nameOrKey) {
        for (ScheduleType scheduleType : values()) {
            if (scheduleType.name().equalsIgnoreCase(nameOrKey) || scheduleType.getKey().equalsIgnoreCase(nameOrKey)) {
                return scheduleType;
            }
        }
        return null;
    }
}
