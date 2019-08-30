package com.vishnu.aggarwal.core.dto;

import com.vishnu.aggarwal.core.co.JobCO;
import com.vishnu.aggarwal.core.co.TriggerCO;
import lombok.*;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuartzDTO {
    private JobCO job;
    private Collection<TriggerCO> triggers;
}
