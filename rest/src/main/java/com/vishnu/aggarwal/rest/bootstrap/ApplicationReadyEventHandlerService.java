package com.vishnu.aggarwal.rest.bootstrap;

import com.vishnu.aggarwal.core.constants.RoleType;
import com.vishnu.aggarwal.rest.entity.Role;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.service.repository.RoleRepoService;
import com.vishnu.aggarwal.rest.service.repository.UserRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.vishnu.aggarwal.core.constants.RoleType.ROLE_ADMIN;
import static com.vishnu.aggarwal.core.constants.RoleType.ROLE_USER;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

/**
 * The type Application ready event handler service.
 */
@Component
@CommonsLog
public class ApplicationReadyEventHandlerService extends com.vishnu.aggarwal.core.bootstrap.ApplicationReadyEventHandlerService {

    @Autowired
    UserRepoService userRepoService;

    @Autowired
    RoleRepoService roleRepoService;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (getBootstrapEnabled()) {
            log.info("*****************Application Ready Event Handler called for Rest Application.");
//            createUsers();
//            createRoles();
        }
    }

    private void createRoles() {
        for (String roleType : RoleType.getValues()) {
            Role role = roleRepoService.findByAuthority(roleType);
            if (isNull(role)) {
                log.info("************** Creating " + roleType + " role ************");
                role = new Role();
                role.setAuthority(roleType);
                roleRepoService.save(role);
            }
        }
    }

    private void createUsers() {

        if (isTrue(userRepoService.checkIfUsernameIsUnique("admin"))) {
            log.info("**************** Creating admin user *****************");
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(applicationContext.getBean(BCryptPasswordEncoder.class).encode("admin"));
            adminUser.setEmail("admin@admin.admin");
            Role adminRole = roleRepoService.findByAuthority(ROLE_ADMIN);
            if (isNull(adminRole)) {
                adminRole = new Role();
                adminRole.setAuthority(ROLE_ADMIN);
            }
            adminUser.getRoles().add(adminRole);
            userRepoService.save(adminUser);
        }

        if (isTrue(userRepoService.checkIfUsernameIsUnique("user"))) {
            log.info("**************** Creating normal user *****************");
            User user = new User();
            user.setUsername("user");
            user.setPassword(applicationContext.getBean(BCryptPasswordEncoder.class).encode("user"));
            user.setEmail("user@user.user");
            Role userRole = roleRepoService.findByAuthority(ROLE_USER);
            if (isNull(userRole)) {
                userRole = new Role();
                userRole.setAuthority(ROLE_USER);
            }
            user.getRoles().add(userRole);
            userRepoService.save(user);
        }
    }
}
