package wrapper.quartz.scheduler.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wrapper.quartz.scheduler.entity.jpa.User;

/**
 * The interface User repository.
 */
@Repository
interface UserRepository extends EntityRepository<User, Long> {
    /**
     * Find by username equals and email equals and deleted user.
     *
     * @param username the username
     * @param email    the email
     * @param deleted  the deleted
     * @return the user
     */
    @Transactional(readOnly = true)
    User findByUsernameEqualsAndEmailEqualsAndDeleted(String username, String email, boolean deleted);

    /**
     * Find by username equals or email equals and deleted user.
     *
     * @param username the username
     * @param email    the email
     * @param deleted  the deleted
     * @return the user
     */
    @Transactional(readOnly = true)
    User findByUsernameEqualsOrEmailEqualsAndDeleted(String username, String email, boolean deleted);
}