package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

@Getter
public enum ScheduleType {
    SIMPLE("SIMPLE"),
    CRON("CRON");

    private String key;

    ScheduleType(String key) {
        this.key = key;
    }
}
