package com.vishnu.aggarwal.core.co;

import lombok.*;

import static java.lang.Boolean.FALSE;
import static java.util.Objects.isNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RepeatIntervalCO {

    private Integer repeatValue;
    private boolean repeatForever = FALSE;
    private Integer repeatCount;

    public Boolean getRepeatForever() {
        return this.repeatForever;
    }

    public Integer getRepeatCount() {
        return isNull(this.repeatCount) || this.repeatCount < 1 ? 1 : this.repeatCount;
    }
}
