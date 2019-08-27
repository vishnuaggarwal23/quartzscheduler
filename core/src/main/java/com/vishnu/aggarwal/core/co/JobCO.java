package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import com.vishnu.aggarwal.core.enums.JobType;
import com.vishnu.aggarwal.core.validation.interfaces.*;
import lombok.*;

import javax.validation.constraints.NotNull;

import static java.lang.Boolean.FALSE;

/**
 * The type Job co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobCO {
    private KeyGroupDescriptionDTO details;
    private boolean durability = FALSE;
    private boolean recover = FALSE;
    private boolean replace = FALSE;
    private JobType type;
    private boolean scheduled = FALSE;
    private JobExecutorClass executorClass;

    private APIJobDataCO apiJobData;
    private ShellScriptJobDataCO shellScriptJobData;

    public boolean getDurability() {
        return durability;
    }

    public boolean getRecover() {
        return recover;
    }

    public boolean getReplace() {
        return replace;
    }

    public boolean getScheduled() {
        return scheduled;
    }
}
