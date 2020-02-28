package com.vishnu.aggarwal.quartz.core.co;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The type Quartz details co.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuartzDetailsCO {
    private JobDetailsCO jobDetails;
    private List<TriggerDetailsCO> triggerDetails;
}
