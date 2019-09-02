package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static java.lang.Boolean.FALSE;

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
    private Date createdDate;
    private Date updatedDate;

    public JobDetailsCO(KeyGroupDescriptionDTO details, JobExecutorClass executorClass, Map data, Boolean durability, Boolean scheduled, Boolean recover, Boolean concurrentExecutionDisallowed, Boolean persistJobDataAfterExecution, Long createdDate, Long updatedDate) {
        this.details = details;
        this.executorClass = executorClass;
        this.data = data;
        this.durability = durability;
        this.scheduled = scheduled;
        this.recover = recover;
        this.concurrentExecutionDisallowed = concurrentExecutionDisallowed;
        this.persistJobDataAfterExecution = persistJobDataAfterExecution;
        if(createdDate != null) {
            this.createdDate = Date.from(Instant.ofEpochMilli(createdDate));
        }
        if(updatedDate != null) {
            this.updatedDate = Date.from(Instant.ofEpochMilli(updatedDate));
        }
    }

    public long getCreatedDateInMillis() {
        return this.createdDate != null ? this.createdDate.toInstant().toEpochMilli() : 0L;
    }

    public long getUpdatedDateInMillis() {
        return this.updatedDate != null ? this.updatedDate.toInstant().toEpochMilli() : 0L;
    }
}
