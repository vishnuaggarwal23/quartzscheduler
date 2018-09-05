package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.enums.ScheduleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.TimeZone;

/**
 * The type Trigger details co.
 */
@Getter
@Setter
@NoArgsConstructor
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

    /**
     * Instantiates a new Trigger details co.
     *
     * @param details          the details
     * @param startTime        the start time
     * @param nextFireTime     the next fire time
     * @param previousFireTime the previous fire time
     * @param endTime          the end time
     * @param finalFireTime    the final fire time
     * @param priority         the priority
     * @param state            the state
     */
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

    /**
     * The type Simple trigger details.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public class SimpleTriggerDetails {
        private Integer countTriggered;
        private Integer repeatCount;
        private Long repeatInterval;
    }

    /**
     * The type Cron trigger details.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public class CronTriggerDetails {
        private String cronExpression;
        private String expressionSummary;
        private TimeZone timeZone;
    }
}
