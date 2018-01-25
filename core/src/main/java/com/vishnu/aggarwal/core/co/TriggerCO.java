package com.vishnu.aggarwal.core.co;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TriggerCO {
    private String keyName;
    private String groupName;
    private String triggerDescription;
    private Boolean startNow;
    private Date startTime;
}
