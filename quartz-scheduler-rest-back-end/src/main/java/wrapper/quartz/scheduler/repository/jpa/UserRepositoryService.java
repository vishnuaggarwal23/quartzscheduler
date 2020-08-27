package wrapper.quartz.scheduler.repository.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wrapper.quartz.scheduler.entity.jpa.User;

/**
 * The type User repository service.
 */
@Service
@Slf4j
public class UserRepositoryService extends EntityRepositoryService<User, Long> {

    private final UserRepository userRepository;

    /**
     * Instantiates a new User repository service.
     *
     * @param userRepository the user repository
     */
    public UserRepositoryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<Long> getIdClass() {
        return Long.class;
    }

    @Override
    protected EntityRepository<User, Long> getEntityRepository() {
        return userRepository;
    }

    /**
     * Find by username equals and email equals and deleted user.
     *
     * @param username the username
     * @param email    the email
     * @param deleted  the deleted
     * @return the user
     */
    @Transactional(readOnly = true)
    public User findByUsernameEqualsAndEmailEqualsAndDeleted(String username, String email, boolean deleted) {
        return userRepository.findByUsernameEqualsAndEmailEqualsAndDeleted(username, email, deleted);
    }

    /**
     * Find by username equals or email equals and deleted user.
     *
     * @param username the username
     * @param email    the email
     * @param deleted  the deleted
     * @return the user
     */
    @Transactional(readOnly = true)
    public User findByUsernameEqualsOrEmailEqualsAndDeleted(String username, String email, boolean deleted) {
        return userRepository.findByUsernameEqualsOrEmailEqualsAndDeleted(username, email, deleted);
    }
}
