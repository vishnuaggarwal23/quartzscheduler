package com.vishnu.aggarwal.core.co;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import static java.lang.String.format;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CronJobSchedulerDataCO {
    private String second;
    private String minute;
    private String hour;
    private String dayOfMonth;
    private String month;
    private String dayOfWeek;
    private String year;

    @JsonIgnore
    public String getCronExpression() {
        return format("%s %s %s %s %s %s %s", second, minute, hour, dayOfMonth, month, dayOfWeek, year);
    }
}
