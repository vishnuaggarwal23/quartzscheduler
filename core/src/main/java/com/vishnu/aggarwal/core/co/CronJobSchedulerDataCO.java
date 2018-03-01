package com.vishnu.aggarwal.core.co;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Cron job scheduler data co.
 */
@Getter
@Setter
public class CronJobSchedulerDataCO {
    private TriggerCO trigger;
    private String second;
    private String minute;
    private String hour;
    private String dayOfMonth;
    private String month;
    private String dayOfWeek;
    private String year;

    /**
     * Gets cron expression.
     *
     * @return the cron expression
     */
    public String getCronExpression() {
        return second + " " + minute + " " + hour + " " + dayOfMonth + " " + month + " " + dayOfWeek + " " + year;
    }
}
