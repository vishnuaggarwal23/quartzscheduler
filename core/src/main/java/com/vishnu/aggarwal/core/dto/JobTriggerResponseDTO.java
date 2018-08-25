package com.vishnu.aggarwal.core.dto;

import com.vishnu.aggarwal.core.co.DataTableCO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Job trigger response dto.
 */
@Getter
@Setter
public class JobTriggerResponseDTO extends DataTableCO {
    private String triggerKeyName;
    private UserDTO triggerGroupName;
    private String jobKeyName;
    private UserDTO jobGroupName;
    private Integer responseCode;
    private String responseHeader;
    private Object responseBody;
    private Date fireTime;
}
