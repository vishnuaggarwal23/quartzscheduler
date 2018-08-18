package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.enums.JobType;
import lombok.*;

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
    private Boolean durability = FALSE;
    private Boolean recover = FALSE;
    private JobType type;
    private Boolean scheduled = FALSE;
}
