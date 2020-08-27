package wrapper.quartz.scheduler.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wrapper.quartz.scheduler.entity.jpa.Authority;
import wrapper.quartz.scheduler.enums.AuthorityEnum;

import javax.validation.constraints.NotNull;

@Repository
interface AuthorityRepository extends EntityRepository<Authority, Long> {

    @Transactional(readOnly = true)
    int countAllByName(@NotNull AuthorityEnum name);

    @Transactional(readOnly = true)
    Authority findByNameEqualsAndDeleted(AuthorityEnum name, boolean deleted);
}