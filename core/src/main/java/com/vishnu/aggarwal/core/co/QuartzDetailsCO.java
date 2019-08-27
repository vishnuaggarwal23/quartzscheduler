package com.vishnu.aggarwal.core.co;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

/**
 * The type Quartz details co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuartzDetailsCO {
    private JobDetailsCO jobDetails;
    private Collection<TriggerDetailsCO> triggerDetails;
}
