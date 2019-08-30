package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

import java.util.Collection;

import static org.apache.commons.lang3.EnumUtils.getEnumList;

@Getter
public enum ScheduleType {
    SIMPLE(com.vishnu.aggarwal.core.constants.ScheduleType.SIMPLE, com.vishnu.aggarwal.core.constants.ScheduleType.simple),
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

    public static Collection<ScheduleType> getAll() {
        return getEnumList(ScheduleType.class);
    }

}
