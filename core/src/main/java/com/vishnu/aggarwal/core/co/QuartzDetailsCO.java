package com.vishnu.aggarwal.core.co;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Quartz details co.
 */
@Getter
@Setter
public class QuartzDetailsCO {
    private JobDetailsCO jobDetails;
    private List<TriggerDetailsCO> triggerDetails;
}
