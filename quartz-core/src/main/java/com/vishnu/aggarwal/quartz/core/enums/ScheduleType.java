package com.vishnu.aggarwal.quartz.core.enums;

import lombok.Getter;

/**
 * The enum Schedule type.
 */
@Getter
public enum ScheduleType {
    /**
     * Simple schedule type.
     */
    SIMPLE("SIMPLE"),
    /**
     * Cron schedule type.
     */
    CRON("CRON");

    private String key;

    ScheduleType(String key) {
        this.key = key;
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
