package wrapper.quartz.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import wrapper.quartz.scheduler.entity.jpa.Authority;
import wrapper.quartz.scheduler.entity.jpa.QuartzGroup;
import wrapper.quartz.scheduler.entity.jpa.User;
import wrapper.quartz.scheduler.util.LoggerUtility;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventListenerService {

    private final AuthorityService authorityService;
    private final UserService userService;
    private final GroupService groupService;

    @Autowired
    public EventListenerService(AuthorityService authorityService, UserService userService, GroupService groupService) {
        this.authorityService = authorityService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public final void bootstrap(ApplicationReadyEvent event) {
        LoggerUtility.debug(log, "Application Bootstrap Begin.");
        List<Authority> authorities = authorityService.createAuthorities();
        LoggerUtility.debug(log, "Authority objects created.");
        QuartzGroup quartzGroup = groupService.createAdminGroup();
        LoggerUtility.debug(log, "Admin group created.");
        User user = userService.createAdminUser();
        LoggerUtility.debug(log, "Admin user created.");
        userService.assignGroup(user.getId(), quartzGroup.getId());
        LoggerUtility.debug(log, "Admin group assigned to the admin user.");
        userService.assignAuthorities(user.getId(), authorities
                .stream()
                .map(Authority::getId)
                .collect(Collectors.toSet()));
        LoggerUtility.debug(log, "Admin role assigned to the admin user.");
    }
}