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
    SIMPLE("SIMPLE"),
    /**
     * Cron schedule type.
     */
    CRON("CRON");

    private String key;

    ScheduleType(String key) {
        this.key = key;
    }
}
