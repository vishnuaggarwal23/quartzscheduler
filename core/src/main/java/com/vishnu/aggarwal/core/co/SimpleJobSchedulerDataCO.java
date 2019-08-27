package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.RepeatType;
import lombok.*;

/**
 * The type Simple job scheduler data co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SimpleJobSchedulerDataCO {
    private RepeatType repeatType;
    private RepeatIntervalCO repeatInterval;
}