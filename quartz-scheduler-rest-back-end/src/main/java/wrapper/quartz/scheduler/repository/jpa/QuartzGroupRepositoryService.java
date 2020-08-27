package wrapper.quartz.scheduler.repository.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wrapper.quartz.scheduler.entity.jpa.QuartzGroup;

/**
 * The type Quartz group repository service.
 */
@Service
@Slf4j
public class QuartzGroupRepositoryService extends EntityRepositoryService<QuartzGroup, Long> {

    private final QuartzGroupRepository quartzGroupRepository;

    /**
     * Instantiates a new Quartz group repository service.
     *
     * @param quartzGroupRepository the quartz group repository
     */
    public QuartzGroupRepositoryService(QuartzGroupRepository quartzGroupRepository) {
        this.quartzGroupRepository = quartzGroupRepository;
    }

    @Override
    protected Class<QuartzGroup> getEntityClass() {
        return QuartzGroup.class;
    }

    @Override
    protected Class<Long> getIdClass() {
        return Long.class;
    }

    @Override
    protected EntityRepository<QuartzGroup, Long> getEntityRepository() {
        return quartzGroupRepository;
    }

    /**
     * Find by name equals and deleted quartz group.
     *
     * @param name    the name
     * @param deleted the deleted
     * @return the quartz group
     */
    @Transactional(readOnly = true)
    public QuartzGroup findByNameEqualsAndDeleted(String name, boolean deleted) {
        return quartzGroupRepository.findByNameEqualsAndDeleted(name, deleted);
    }
}
