package com.vishnu.aggarwal.rest.config;

/*
Created by vishnu on 1/3/18 2:05 PM
*/

import com.vishnu.aggarwal.rest.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * The type Auditor aware.
 */
public class AuditorAwareImpl implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = getContext().getAuthentication();
        return nonNull(authentication) ? (Optional<User>) authentication.getPrincipal() : Optional.empty();
    }
}
