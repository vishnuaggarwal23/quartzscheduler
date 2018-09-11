package com.vishnu.aggarwal.core.dto;

import com.vishnu.aggarwal.core.co.DataTableCO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * The type Job trigger response dto.
 */
@Getter
@Setter
@ToString
public class JobTriggerResponseDTO extends DataTableCO {
    private String triggerKey;
    private UserDTO triggerGroup;
    private String jobKey;
    private UserDTO jobGroup;
    private Integer responseCode;
    private String responseHeader;
    private Object responseBody;
    private Date fireTime;
}
