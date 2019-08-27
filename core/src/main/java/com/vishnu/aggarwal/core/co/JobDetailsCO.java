package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
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
    private KeyGroupDescriptionDTO details;
    private JobExecutorClass executorClass;
    private Map data;
    private Boolean durability = FALSE;
    private Boolean scheduled = FALSE;
    private Boolean recover = FALSE;
    private Boolean concurrentExecutionDisallowed = FALSE;
    private Boolean persistJobDataAfterExecution = FALSE;
}
