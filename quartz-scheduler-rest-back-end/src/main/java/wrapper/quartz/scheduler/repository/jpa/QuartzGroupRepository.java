package wrapper.quartz.scheduler.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wrapper.quartz.scheduler.entity.jpa.QuartzGroup;

/**
 * The interface Quartz group repository.
 */
@Repository
interface QuartzGroupRepository extends EntityRepository<QuartzGroup, Long> {
    /**
     * Find by name equals and deleted quartz group.
     *
     * @param name    the name
     * @param deleted the deleted
     * @return the quartz group
     */
    @Transactional(readOnly = true)
    QuartzGroup findByNameEqualsAndDeleted(String name, boolean deleted);
}