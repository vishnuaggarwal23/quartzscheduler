package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import com.vishnu.aggarwal.core.enums.JobType;
import lombok.*;

import java.util.Date;

import static java.lang.Boolean.FALSE;

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

    public Date getCreatedDate() {
        return this.replace ? null : new Date();
    }

    public Date getUpdatedDate() {
        return this.replace ? new Date() : this.getCreatedDate();
    }

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
