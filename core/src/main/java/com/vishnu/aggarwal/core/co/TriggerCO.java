package com.vishnu.aggarwal.core.co;

import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import lombok.*;

import java.util.Date;

import static java.lang.Boolean.FALSE;

/**
 * The type Trigger co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TriggerCO {
    private KeyGroupDescriptionDTO details;
    private Boolean startNow = FALSE;
    private Date startTime;
    private Date endTime;
}
