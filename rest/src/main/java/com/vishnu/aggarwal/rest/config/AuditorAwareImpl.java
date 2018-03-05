package com.vishnu.aggarwal.rest.config;

/*
Created by vishnu on 1/3/18 2:05 PM
*/

import org.springframework.data.domain.AuditorAware;

/**
 * The type Auditor aware.
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        return null;
    }
}
