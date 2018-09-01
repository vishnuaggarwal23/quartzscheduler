package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

import static java.lang.Boolean.FALSE;

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
    private Boolean durability = FALSE;
    private Boolean scheduled = FALSE;
    private Boolean recover = FALSE;
    private Boolean concurrentExecutionDisallowed = FALSE;
    private Boolean persistJobDataAfterExecution = FALSE;
    private Date scheduledDate;
}
