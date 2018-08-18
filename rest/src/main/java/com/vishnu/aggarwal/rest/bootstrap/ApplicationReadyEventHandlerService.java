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
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
     * The Application context.
     */
    private final ApplicationContext applicationContext;

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
            ApplicationContext applicationContext,
            AuthorityRepoService authorityRepoService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserAuthorityRepoService userAuthorityRepoService) {
        this.userRepoService = userRepoService;
        this.applicationContext = applicationContext;
        this.authorityRepoService = authorityRepoService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userAuthorityRepoService = userAuthorityRepoService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (getBootstrapEnabled()) {
            log.info("Application Ready Event Handler called for Rest Application");
            createAuthorities();
            createUsers();
            createUserAuthorities();
        }
    }

    private void createAuthorities() {
        RoleType.getValues().forEach(it -> {
            Authority authority = authorityRepoService.findByAuthorityAndIsDeleted(it, FALSE);
            if (isNull(authority)) {
                log.info("Creating " + it + " Authority");
                authority = new Authority();
                authority.setName(it);
                authorityRepoService.save(authority);
            }
        });
    }

    private void createUsers() {
        if (isTrue(userRepoService.checkIfUsernameIsUnique("admin"))) {
            log.info("Creating admin user ");
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(bCryptPasswordEncoder.encode("admin"));
            adminUser.setEmail("admin@admin.admin");
            adminUser.setLastName("user");
            adminUser.setFirstName("admin");
            adminUser.setStatus(ACTIVE);
            userRepoService.save(adminUser);
        }

        /*if (isTrue(userRepoService.checkIfUsernameIsUnique("user"))) {
            log.info("**************** Creating normal user ");
            User user = new User();
            user.setUsername("user");
            user.setPassword(bCryptPasswordEncoder.encode("user"));
            user.setEmail("user@user.user");
            userRepoService.save(user);
        }*/
    }

    private void createUserAuthorities() {
        User adminUser = userRepoService.findByUsername("admin");
        if (nonNull(adminUser)) {
            createUserAuthority(adminUser, authorityRepoService.findByAuthorityAndIsDeleted(ROLE_ADMIN, FALSE));
            createUserAuthority(adminUser, authorityRepoService.findByAuthorityAndIsDeleted(ROLE_USER, FALSE));
        }
        /*User normalUser = userRepoService.findByUsername("user");
        if (nonNull(normalUser)) {
            Authority normalUserAuthority = authorityRepoService.findByAuthorityAndIsDeleted(RoleType.ROLE_USER, FALSE);
            createUserAuthority(normalUser, normalUserAuthority);
        }*/
    }

    private void createUserAuthority(User user, Authority authority) {
        if (nonNull(authority)) {
            UserAuthority userAuthority = userAuthorityRepoService.findByUserAndAuthorityAndIsDeleted(user, authority, FALSE);
            if (isNull(userAuthority)) {
                log.info("Creating User Authority for [User " + user + "] and [Authority " + authority + "]");
                userAuthority = new UserAuthority();
                userAuthority.setUser(user);
                userAuthority.setAuthority(authority);
                userAuthority.setStatus(ACTIVE);
                userAuthorityRepoService.save(userAuthority);
            }
        }
    }
}
