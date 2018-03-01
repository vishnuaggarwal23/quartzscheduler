package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * The type Job details co.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDetailsCO {
    private String keyName;
    private String groupName;
    private String description;
    private JobExecutorClass executorClass;
    private Map data;
    private Boolean durability;
    private Boolean scheduled;
    private Boolean recover;
    private Boolean concurrentExecutionDisallowed;
    private Boolean persistJobDataAfterExecution;
}
