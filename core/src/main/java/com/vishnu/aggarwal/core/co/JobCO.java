package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.JobType;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Job co.
 */
@Getter
@Setter
public class JobCO {
    private String keyName;
    private String groupName;
    private String description;
    private Boolean durability;
    private Boolean recover;
    private JobType type;
    private Boolean scheduled;
}
