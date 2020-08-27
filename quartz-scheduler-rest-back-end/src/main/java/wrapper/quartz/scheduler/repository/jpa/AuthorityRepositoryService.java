package wrapper.quartz.scheduler.repository.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import wrapper.quartz.scheduler.entity.jpa.Authority;
import wrapper.quartz.scheduler.enums.AuthorityEnum;

/**
 * The type Authority repository service.
 */
@Service
@Slf4j
public class AuthorityRepositoryService extends EntityRepositoryService<Authority, Long> {
    private final AuthorityRepository authorityRepository;

    /**
     * Instantiates a new Authority repository service.
     *
     * @param authorityRepository the authority repository
     */
    public AuthorityRepositoryService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    /**
     * Exists boolean.
     *
     * @param authority the authority
     * @return the boolean
     * @throws IllegalArgumentException the illegal argument exception
     */
    @Transactional(readOnly = true)
    public boolean exists(AuthorityEnum authority) throws IllegalArgumentException {
        Assert.notNull(authority, "Authority is required");
        return authorityRepository.countAllByName(authority) > 0;
    }

    @Override
    protected Class<Authority> getEntityClass() {
        return Authority.class;
    }

    @Override
    protected Class<Long> getIdClass() {
        return Long.class;
    }

    @Override
    protected EntityRepository<Authority, Long> getEntityRepository() {
        return authorityRepository;
    }

    @Transactional(readOnly = true)
    public Authority findByNameEqualsAndDeleted(AuthorityEnum name, boolean deleted) {
        return authorityRepository.findByNameEqualsAndDeleted(name, deleted);
    }
}