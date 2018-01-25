package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.RepeatUnit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepeatIntervalCO {
    private Integer repeatValue;
    private Boolean repeatForever;
    private Integer repeatCount;
    private RepeatUnit repeatUnit;

    public Boolean getRepeatForever() {
        return Boolean.TRUE;
    }
}
