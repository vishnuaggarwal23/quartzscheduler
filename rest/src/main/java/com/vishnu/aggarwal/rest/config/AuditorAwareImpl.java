package com.vishnu.aggarwal.rest.config;

/*
Created by vishnu on 1/3/18 2:05 PM
*/

import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * The type Auditor aware.
 */
public class AuditorAwareImpl implements AuditorAware<User> {

    private UserService userService;

    /**
     * Instantiates a new Auditor aware.
     *
     * @param userService the user service
     */
    public AuditorAwareImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = getContext().getAuthentication();
        User user = null;
        if (nonNull(authentication)) {
            String username = (String) authentication.getPrincipal();
            if (isNotBlank(username)) {
                user = userService.findByUsername(username);
            }
        }
        return Optional.ofNullable(user);
    }
}
