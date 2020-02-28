package com.vishnu.aggarwal.quartz.rest.service.repository.jpa;

/*
Created by vishnu on 1/3/18 1:00 PM
*/

import com.vishnu.aggarwal.quartz.core.co.DataTableCO;
import com.vishnu.aggarwal.quartz.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.io.Serializable;

import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * The type Base service.
 *
 * @param <T>  the type parameter
 * @param <ID> the type parameter
 */
@CommonsLog
public abstract class BaseRepoService<T, ID extends Serializable> extends BaseService {
    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * Gets entity class.
     *
     * @return the entity class
     */
    protected abstract Class<T> getEntityClass();

    /**
     * Gets jpa repository.
     *
     * @return the jpa repository
     */
    protected abstract JpaRepository<T, ID> getJpaRepository();

    /**
     * Gets root.
     *
     * @param criteriaQuery the criteria query
     * @return the root
     */
    Root<T> getRoot(CriteriaQuery<T> criteriaQuery) {
        return criteriaQuery.from(getEntityClass());
    }

    /**
     * Gets root.
     *
     * @param criteriaDelete the criteria delete
     * @return the root
     */
    Root<T> getRoot(CriteriaDelete<T> criteriaDelete) {
        return criteriaDelete.from(getEntityClass());
    }

    /**
     * Gets root.
     *
     * @param criteriaUpdate the criteria update
     * @return the root
     */
    Root<T> getRoot(CriteriaUpdate<T> criteriaUpdate) {
        return criteriaUpdate.from(getEntityClass());
    }

    /**
     * Gets criteria builder.
     *
     * @return the criteria builder
     */
    CriteriaBuilder getCriteriaBuilder() {
        return this.getSession().getCriteriaBuilder();
    }

    /**
     * Get base criteria criteria.
     *
     * @return the criteria
     */
/*
    protected Criteria getBaseCriteriaSelectImpl() {
        return getSession().createCriteria(getEntityClass());
    }*/
    CriteriaQuery<T> getBaseCriteriaSelectImpl() {
        return this.getCriteriaBuilder().createQuery(getEntityClass());
    }

    /**
     * Gets base criteria update.
     *
     * @return the base criteria update
     */
    CriteriaUpdate<T> getBaseCriteriaUpdateImpl() {
        return this.getCriteriaBuilder().createCriteriaUpdate(getEntityClass());
    }

    /**
     * Gets base criteria delete.
     *
     * @return the base criteria delete
     */
    protected CriteriaDelete<T> getBaseCriteriaDeleteImpl() {
        return this.getCriteriaBuilder().createCriteriaDelete(getEntityClass());
    }

    /**
     * Gets criteria order.
     *
     * @param criteriaBuilder the criteria builder
     * @param dataTableCO     the data table co
     * @return the criteria order
     */
    private Order getCriteriaOrder(CriteriaBuilder criteriaBuilder, DataTableCO dataTableCO) {
        if (dataTableCO.getOrderBy().equalsIgnoreCase("desc")) {
            return criteriaBuilder.desc(getRoot(getBaseCriteriaSelectImpl()).get(isEmpty(dataTableCO.getSortBy()) ? "id" : dataTableCO.getSortBy()));
        } else {
            return criteriaBuilder.asc(getRoot(getBaseCriteriaSelectImpl()).get(isEmpty(dataTableCO.getSortBy()) ? "id" : dataTableCO.getSortBy()));
        }
    }

    /**
     * Update query integer.
     *
     * @param criteriaUpdate the criteria update
     * @return the integer
     */
    @Transactional
    Integer updateQuery(CriteriaUpdate<T> criteriaUpdate) {
        return getSession().createQuery(criteriaUpdate).executeUpdate();
    }

    /**
     * Select query object.
     *
     * @param criteriaQuery          the criteria query
     * @param isDistinct             the is distinct
     * @param isSingleResultExpected the is single result expected
     * @param dataTableCO            the data table co
     * @return the object
     * @throws NoResultException the no result exception
     */
    @Transactional
    Object selectQuery(CriteriaQuery<T> criteriaQuery, Boolean isDistinct, Boolean isSingleResultExpected, DataTableCO dataTableCO) {
        criteriaQuery.distinct(isDistinct);
        if (nonNull(dataTableCO)) {
            criteriaQuery.orderBy(getCriteriaOrder(getCriteriaBuilder(), dataTableCO));
        }
        Query<T> query = getSession().createQuery(criteriaQuery);
        query.setReadOnly(TRUE);
        if (nonNull(dataTableCO)) {
            query.setFirstResult(dataTableCO.getOffset());
            query.setMaxResults(dataTableCO.getMax());
        }
        return isTrue(isSingleResultExpected) ? query.getSingleResult() : query.getResultList();
    }

    /**
     * Delete query integer.
     *
     * @param criteriaDelete the criteria delete
     * @return the integer
     */
    @Transactional
    Integer deleteQuery(CriteriaDelete<T> criteriaDelete) {
        return getSession().createQuery(criteriaDelete).executeUpdate();
    }

    /**
     * Save s.
     *
     * @param <S>    the type parameter
     * @param entity the entity
     * @return the s
     */
    @Transactional
    <S extends T> S save(S entity) {
        return getJpaRepository().save(entity);
    }

    /**
     * Find one t.
     *
     * @param id the id
     * @return the t
     */
    @Transactional
    T findOne(ID id) {
        return getJpaRepository().findById(id).orElse(null);
    }

    @Transactional
    T getOne(ID id) {
        return getJpaRepository().getOne(id);
    }

}
