package com.vishnu.aggarwal.quartz.rest.bootstrap;

import com.vishnu.aggarwal.quartz.core.constants.RoleType;
import com.vishnu.aggarwal.quartz.rest.entity.Authority;
import com.vishnu.aggarwal.quartz.rest.entity.User;
import com.vishnu.aggarwal.quartz.rest.entity.UserAuthority;
import com.vishnu.aggarwal.quartz.rest.service.repository.jpa.AuthorityRepoService;
import com.vishnu.aggarwal.quartz.rest.service.repository.jpa.UserAuthorityRepoService;
import com.vishnu.aggarwal.quartz.rest.service.repository.jpa.UserRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.quartz.core.constants.RoleType.ROLE_ADMIN;
import static com.vishnu.aggarwal.quartz.core.constants.RoleType.ROLE_USER;
import static com.vishnu.aggarwal.quartz.core.enums.Status.ACTIVE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

/**
 * The type Application ready event handler service.
 */
@Component
@CommonsLog
public class ApplicationReadyEventHandlerService extends com.vishnu.aggarwal.quartz.core.bootstrap.ApplicationReadyEventHandlerService {

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

    //    private final CacheManager cacheManager;
    @Value("${cache.clear.enabled:false}")
    Boolean clearCacheEnabled;

    @Autowired
    public ApplicationReadyEventHandlerService(
            UserRepoService userRepoService,
            AuthorityRepoService authorityRepoService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserAuthorityRepoService userAuthorityRepoService/*,
            CacheManager cacheManager*/) {
        this.userRepoService = userRepoService;
        this.authorityRepoService = authorityRepoService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userAuthorityRepoService = userAuthorityRepoService;
//        this.cacheManager = cacheManager;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (clearCacheEnabled) {
            /*cacheManager.getCacheNames().stream().filter(Objects::nonNull).forEach(it -> {
                try {
                    requireNonNull(cacheManager.getCache(it)).clear();
                    log.info("[Application Bootstrap] Clearing cache " + it);
                } catch (Exception e) {
                    log.error("[Application Bootstrap] Unable to clear cache " + it);
                    log.error(getStackTrace(e));
                }
            });*/
        }
        if (getBootstrapEnabled()) {
            log.info("[Application Bootstrap] Application Ready Event Handler called for Rest Application");
            createAuthorities();
            createUsers();
            createUserAuthorities();
        }
    }

    private void createAuthorities() {
        RoleType.getValues().forEach(it -> {
            Authority authority = authorityRepoService.findByName(it);
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
        final User adminUser = userRepoService.findByUsername(ADMIN_USER_USERNAME);
        if (nonNull(adminUser)) {
            createUserAuthority(adminUser, authorityRepoService.findByName(ROLE_ADMIN));
            createUserAuthority(adminUser, authorityRepoService.findByName(ROLE_USER));
        }
    }

    private void createUserAuthority(@NotNull final User user, @NotNull final Authority authority) {
        userAuthorityRepoService.countByUserAndAuthority(user, authority);
        if (userAuthorityRepoService.countByUserAndAuthority(user, authority) != 0) {
            log.info("[Application Bootstrap] Creating User Authority for [User " + user + "] and [Authority " + authority + "]");
            userAuthorityRepoService.save(new UserAuthority(user, authority, ACTIVE));
        }
    }
}
