package wrapper.quartz.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import wrapper.quartz.scheduler.entity.jpa.Authority;
import wrapper.quartz.scheduler.entity.jpa.QuartzGroup;
import wrapper.quartz.scheduler.entity.jpa.User;
import wrapper.quartz.scheduler.repository.jpa.AuthorityRepositoryService;
import wrapper.quartz.scheduler.repository.jpa.QuartzGroupRepositoryService;
import wrapper.quartz.scheduler.repository.jpa.UserRepositoryService;

import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;

/**
 * The type User service.
 */
@Service
@Slf4j
public class UserService {

    private final UserRepositoryService userRepositoryService;
    private final QuartzGroupRepositoryService quartzGroupRepositoryService;
    private final AuthorityRepositoryService authorityRepositoryService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Instantiates a new User service.
     *
     * @param userRepositoryService        the user repository service
     * @param quartzGroupRepositoryService the group repository service
     * @param authorityRepositoryService   the authority repository service
     * @param bCryptPasswordEncoder        the b crypt password encoder
     */
    public UserService(UserRepositoryService userRepositoryService, QuartzGroupRepositoryService quartzGroupRepositoryService, AuthorityRepositoryService authorityRepositoryService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepositoryService = userRepositoryService;
        this.quartzGroupRepositoryService = quartzGroupRepositoryService;
        this.authorityRepositoryService = authorityRepositoryService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        User user = userRepositoryService.findById(userId);
        List<Authority> authorities = authorityRepositoryService.findAll(authorityIds);
        assignAuthorities(user, authorities);
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
}
