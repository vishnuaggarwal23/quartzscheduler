package com.vishnu.aggarwal.core.dto;

import com.vishnu.aggarwal.core.co.DataTableCO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JobTriggerResponseDTO extends DataTableCO {
    private String triggerName;
    private String jobName;
    private Integer responseCode;
    private String responseHeader;
    private String responseBody;
    private Date fireTime;
}
