package wrapper.quartz.scheduler.repository.jpa;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import wrapper.quartz.scheduler.entity.jpa.BaseEntity;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The type Entity repository service.
 *
 * @param <T>  the type parameter
 * @param <ID> the type parameter
 */
@Slf4j
abstract class EntityRepositoryService<T extends BaseEntity<ID>, ID extends Serializable> {
    /**
     * Gets entity class.
     *
     * @return the entity class
     */
    protected abstract Class<T> getEntityClass();

    /**
     * Gets id class.
     *
     * @return the id class
     */
    protected abstract Class<ID> getIdClass();

    /**
     * Gets entity repository.
     *
     * @return the entity repository
     */
    protected abstract EntityRepository<T, ID> getEntityRepository();

    /**
     * Save t.
     *
     * @param object the object
     * @return the t
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    @Transactional
    public T save(T object) throws IllegalArgumentException, PersistenceException {
        return save(object, false);
    }

    /**
     * Save t.
     *
     * @param object the object
     * @param flush  the flush
     * @return the t
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    @Transactional
    public T save(T object, boolean flush) throws IllegalArgumentException, PersistenceException {
        return flush ? getEntityRepository().saveAndFlush(object) : getEntityRepository().save(object);
    }

    /**
     * Find by id t.
     *
     * @param id the id
     * @return the t
     * @throws ObjectNotFoundException  the object not found exception
     * @throws IllegalArgumentException the illegal argument exception
     */
    @Transactional(readOnly = true)
    public T findById(ID id) throws ObjectNotFoundException, IllegalArgumentException {
        return getEntityRepository().findById(id).<ObjectNotFoundException>orElseThrow(() -> {
            throw new ObjectNotFoundException(id, getEntityClass().getName());
        });
    }

    /**
     * Gets one.
     *
     * @param id the id
     * @return the one
     * @throws IllegalArgumentException the illegal argument exception
     * @throws EntityNotFoundException  the entity not found exception
     */
    @Transactional(readOnly = true)
    public T getOne(ID id) throws IllegalArgumentException, EntityNotFoundException {
        return getEntityRepository().getOne(id);
    }

    /**
     * Find all list.
     *
     * @param ids the ids
     * @return the list
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    @Transactional(readOnly = true)
    public List<T> findAll(ID... ids) throws IllegalArgumentException, PersistenceException {
        return findAll(Arrays.asList(ids));
    }

    /**
     * Find all list.
     *
     * @param ids the ids
     * @return the list
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    @Transactional(readOnly = true)
    public List<T> findAll(Collection<ID> ids) throws IllegalArgumentException, PersistenceException {
        return getEntityRepository().findAllById(ids);
    }
}