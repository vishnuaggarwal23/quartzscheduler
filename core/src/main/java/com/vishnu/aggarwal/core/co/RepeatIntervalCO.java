package com.vishnu.aggarwal.core.co;

import lombok.*;

import static java.lang.Boolean.FALSE;
import static java.util.Objects.isNull;

/**
 * The type Repeat interval co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RepeatIntervalCO {
    private Integer repeatValue;
    private Boolean repeatForever = FALSE;
    private Integer repeatCount;

    /**
     * Gets repeat forever.
     *
     * @return the repeat forever
     */
    public Boolean getRepeatForever() {
        return this.repeatForever;
    }

    /**
     * Gets repeat count.
     *
     * @return the repeat count
     */
    public Integer getRepeatCount() {
        return isNull(this.repeatCount) || this.repeatCount < 1 ? 1 : this.repeatCount;
    }
}
