package wrapper.quartz.scheduler.repository.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wrapper.quartz.scheduler.entity.jpa.User;

@Service
@Slf4j
public class UserRepositoryService extends EntityRepositoryService<User, Long> {

    private final UserRepository userRepository;

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

    public User findByUsernameEqualsAndEmailEqualsAndDeleted(String username, String email, boolean deleted) {
        return userRepository.findByUsernameEqualsAndEmailEqualsAndDeleted(username, email, deleted);
    }
}
