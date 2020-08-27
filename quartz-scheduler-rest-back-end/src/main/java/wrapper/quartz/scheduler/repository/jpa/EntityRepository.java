package wrapper.quartz.scheduler.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import wrapper.quartz.scheduler.entity.jpa.BaseEntity;

/**
 * The interface Entity repository.
 *
 * @param <T>  the type parameter
 * @param <ID> the type parameter
 */
@NoRepositoryBean
interface EntityRepository<T extends BaseEntity<ID>, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}