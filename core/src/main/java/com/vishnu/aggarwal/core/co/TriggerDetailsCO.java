package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TriggerDetailsCO {
    private KeyGroupDescriptionDTO details;
    private Date startTime;
    private Date nextFireTime;
    private Date previousFireTime;
    private Date endTime;
    private Date finalEndTime;
    private Integer priority;
    private ScheduleType type;
    private String state;
    private Date scheduledDate;
    private Date createdDate;
    private Date updatedDate;

    public TriggerDetailsCO(KeyGroupDescriptionDTO details, Date startTime, Date nextFireTime, Date previousFireTime, Date endTime, Date finalFireTime, Integer priority, String state) {
        this.details = details;
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
    @AllArgsConstructor
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

    public long getCreatedDateInMillis() {
        return this.createdDate != null ? this.createdDate.toInstant().toEpochMilli() : 0L;
    }

    public long getUpdatedDateInMillis() {
        return this.updatedDate != null ? this.updatedDate.toInstant().toEpochMilli() : 0L;
    }
}
