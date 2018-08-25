package com.vishnu.aggarwal.rest.bootstrap;

import com.vishnu.aggarwal.core.constants.RoleType;
import com.vishnu.aggarwal.rest.entity.Authority;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserAuthority;
import com.vishnu.aggarwal.rest.service.repository.jpa.AuthorityRepoService;
import com.vishnu.aggarwal.rest.service.repository.jpa.UserAuthorityRepoService;
import com.vishnu.aggarwal.rest.service.repository.jpa.UserRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.constants.RoleType.ROLE_ADMIN;
import static com.vishnu.aggarwal.core.constants.RoleType.ROLE_USER;
import static com.vishnu.aggarwal.core.enums.Status.ACTIVE;
import static java.lang.Boolean.FALSE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

/**
 * The type Application ready event handler service.
 */
@Component
@CommonsLog
public class ApplicationReadyEventHandlerService extends com.vishnu.aggarwal.core.bootstrap.ApplicationReadyEventHandlerService {

    /**
     * The User repo service.
     */
    private final UserRepoService userRepoService;

    /**
     * The Authority repo service.
     */
    private final AuthorityRepoService authorityRepoService;

    /**
     * The B crypt password encoder.
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * The User authority repo service.
     */
    private final UserAuthorityRepoService userAuthorityRepoService;

    @Autowired
    public ApplicationReadyEventHandlerService(
            UserRepoService userRepoService,
            AuthorityRepoService authorityRepoService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserAuthorityRepoService userAuthorityRepoService) {
        this.userRepoService = userRepoService;
        this.authorityRepoService = authorityRepoService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userAuthorityRepoService = userAuthorityRepoService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (getBootstrapEnabled()) {
            log.info("[Application Bootstrap] Application Ready Event Handler called for Rest Application");
            createAuthorities();
            createUsers();
            createUserAuthorities();
        }
    }

    private void createAuthorities() {
        RoleType.getValues().forEach(it -> {
            Authority authority = authorityRepoService.findByAuthorityAndIsDeleted(it, FALSE);
            if (isNull(authority)) {
                log.info("[Application Bootstrap] Creating " + it + " Authority");
                authority = new Authority();
                authority.setName(it);
                authorityRepoService.save(authority);
            }
        });
    }

    private void createUsers() {
        if (isTrue(userRepoService.checkIfUsernameIsUnique(ADMIN_USER_USERNAME))) {
            log.info("[Application Bootstrap] Creating admin user ");
            User adminUser = new User();
            adminUser.setUsername(ADMIN_USER_USERNAME);
            adminUser.setPassword(bCryptPasswordEncoder.encode(ADMIN_USER_PASSWORD));
            adminUser.setEmail(ADMIN_USER_EMAIL);
            adminUser.setLastName(ADMIN_USER_LASTNAME);
            adminUser.setFirstName(ADMIN_USER_FIRSTNAME);
            adminUser.setStatus(ACTIVE);
            userRepoService.save(adminUser);
        }
    }

    private void createUserAuthorities() {
        User adminUser = userRepoService.findByUsername(ADMIN_USER_USERNAME);
        if (nonNull(adminUser)) {
            createUserAuthority(adminUser, authorityRepoService.findByAuthorityAndIsDeleted(ROLE_ADMIN, FALSE));
            createUserAuthority(adminUser, authorityRepoService.findByAuthorityAndIsDeleted(ROLE_USER, FALSE));
        }
    }

    private void createUserAuthority(final User user, final Authority authority) {
        if (nonNull(authority)) {
            UserAuthority userAuthority = userAuthorityRepoService.findByUserAndAuthorityAndIsDeleted(user, authority, FALSE);
            if (isNull(userAuthority)) {
                log.info("[Application Bootstrap] Creating User Authority for [User " + user + "] and [Authority " + authority + "]");
                userAuthority = new UserAuthority();
                userAuthority.setUser(user);
                userAuthority.setAuthority(authority);
                userAuthority.setStatus(ACTIVE);
                userAuthorityRepoService.save(userAuthority);
            }
        }
    }
}
