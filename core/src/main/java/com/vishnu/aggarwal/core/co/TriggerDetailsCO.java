package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.ScheduleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
@NoArgsConstructor
public class TriggerDetailsCO {
    private String keyName;
    private String groupName;
    private String description;
    private Date startTime;
    private Date nextFireTime;
    private Date previousFireTime;
    private Date endTime;
    private Date finalEndTime;
    private Integer priority;
    private ScheduleType type;
    private String state;

    public TriggerDetailsCO(String keyName, String groupName, String description, Date startTime, Date nextFireTime, Date previousFireTime, Date endTime, Date finalFireTime, Integer priority, String state) {
        this.keyName = keyName;
        this.groupName = groupName;
        this.description = description;
        this.startTime = startTime;
        this.nextFireTime = nextFireTime;
        this.previousFireTime = previousFireTime;
        this.endTime = endTime;
        this.finalEndTime = finalFireTime;
        this.priority = priority;
        this.state = state;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public class SimpleTriggerDetails {
        private Integer countTriggered;
        private Integer repeatCount;
        private Long repeatInterval;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public class CronTriggerDetails {
        private String cronExpression;
        private String expressionSummary;
        private TimeZone timeZone;
    }
}
