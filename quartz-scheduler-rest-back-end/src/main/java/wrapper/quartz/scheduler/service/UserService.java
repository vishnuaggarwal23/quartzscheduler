package wrapper.quartz.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import wrapper.quartz.scheduler.entity.jpa.Authority;
import wrapper.quartz.scheduler.entity.jpa.QuartzGroup;
import wrapper.quartz.scheduler.entity.jpa.User;
import wrapper.quartz.scheduler.repository.jpa.AuthorityRepositoryService;
import wrapper.quartz.scheduler.repository.jpa.QuartzGroupRepositoryService;
import wrapper.quartz.scheduler.repository.jpa.UserRepositoryService;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The type User service.
 */
@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepositoryService userRepositoryService;
    private final QuartzGroupRepositoryService quartzGroupRepositoryService;
    private final AuthorityRepositoryService authorityRepositoryService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsChecker userDetailsChecker;

    /**
     * Instantiates a new User service.
     *
     * @param userRepositoryService        the user repository service
     * @param quartzGroupRepositoryService the group repository service
     * @param authorityRepositoryService   the authority repository service
     * @param bCryptPasswordEncoder        the b crypt password encoder
     * @param userDetailsChecker           the user details checker
     */
    public UserService(UserRepositoryService userRepositoryService, QuartzGroupRepositoryService quartzGroupRepositoryService, AuthorityRepositoryService authorityRepositoryService, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsChecker userDetailsChecker) {
        this.userRepositoryService = userRepositoryService;
        this.quartzGroupRepositoryService = quartzGroupRepositoryService;
        this.authorityRepositoryService = authorityRepositoryService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsChecker = userDetailsChecker;
    }

    /**
     * Create admin user user.
     *
     * @return the user
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    public User createAdminUser() throws IllegalArgumentException, PersistenceException {
        User user = userRepositoryService.findByUsernameEqualsAndEmailEqualsAndDeleted("admin", "admin@adminUser.com", false);
        if (user == null) {
            user = new User();
            user.setUsername("admin");
            user.setEmail("admin@adminUser.com");
            user.setPassword(bCryptPasswordEncoder.encode("Password@1"));
            user = userRepositoryService.save(user);
        }
        return user;
    }

    /**
     * Assign group.
     *
     * @param userId  the user id
     * @param groupId the group id
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    public void assignGroup(Long userId, Long groupId) throws IllegalArgumentException, PersistenceException {
        User user = userRepositoryService.findById(userId);
        QuartzGroup quartzGroup = quartzGroupRepositoryService.findById(groupId);
        assignGroup(user, quartzGroup);
    }

    /**
     * Assign group.
     *
     * @param user        the user
     * @param quartzGroup the group
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    public void assignGroup(User user, QuartzGroup quartzGroup) throws IllegalArgumentException, PersistenceException {
        user.setQuartzGroup(quartzGroup);
        userRepositoryService.save(user);
    }

    /**
     * Assign authorities.
     *
     * @param userId       the user id
     * @param authorityIds the authority ids
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    public void assignAuthorities(Long userId, Long... authorityIds) throws IllegalArgumentException, PersistenceException {
        assignAuthorities(userId, Arrays.asList(authorityIds));
    }

    /**
     * Assign authorities.
     *
     * @param userId       the user id
     * @param authorityIds the authority ids
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    public void assignAuthorities(Long userId, Collection<Long> authorityIds) throws IllegalArgumentException, PersistenceException {
        User user = userRepositoryService.findById(userId);
        List<Authority> authorities = authorityRepositoryService.findAll(authorityIds);
        assignAuthorities(user, authorities);
    }

    /**
     * Assign authorities.
     *
     * @param user        the user
     * @param authorities the authorities
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    public void assignAuthorities(User user, Collection<Authority> authorities) throws IllegalArgumentException, PersistenceException {
        user.getAuthorities().addAll(authorities);
        userRepositoryService.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, AccountStatusException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("No username is passed");
        }
        User user = userRepositoryService.findByUsernameEqualsOrEmailEqualsAndDeleted(username, username, false);
        if (user == null) {
            throw new UsernameNotFoundException("No user found for " + username);
        }
        if (CollectionUtils.isEmpty(user.getAuthorities())) {
            throw new UsernameNotFoundException("User has no authorities assigned.");
        }
        userDetailsChecker.check(user);
        return user;
    }

    /**
     * Gets current logged in.
     *
     * @return the current logged in
     * @throws UsernameNotFoundException the username not found exception
     * @throws AccountStatusException    the account status exception
     */
    public User getCurrentLoggedIn() throws UsernameNotFoundException, AccountStatusException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("No authentication found in Security Context");
        }
        if (authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof String)) {
            throw new UsernameNotFoundException("No principal found in Security Context");
        }
        if (CollectionUtils.isEmpty(authentication.getAuthorities())) {
            throw new UsernameNotFoundException("User has no authorities assigned.");
        }
        User user = userRepositoryService.findByUsernameEqualsOrEmailEqualsAndDeleted(((String) authentication.getPrincipal()), ((String) authentication.getPrincipal()), false);
        userDetailsChecker.check(user);
        return user;
    }
}