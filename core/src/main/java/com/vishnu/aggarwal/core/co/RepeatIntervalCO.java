package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.RepeatUnit;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.isNull;

/**
 * The type Repeat interval co.
 */
@Getter
@Setter
public class RepeatIntervalCO {
    private Integer repeatValue;
    private Boolean repeatForever;
    private Integer repeatCount;
    private RepeatUnit repeatUnit;

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
