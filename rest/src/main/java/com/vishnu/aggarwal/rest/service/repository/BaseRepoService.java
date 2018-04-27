package com.vishnu.aggarwal.rest.service.repository;

/*
Created by vishnu on 1/3/18 1:00 PM
*/

import com.vishnu.aggarwal.core.co.DataTableCO;
import com.vishnu.aggarwal.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import java.io.Serializable;

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
    protected Criteria getBaseCriteriaImpl() {
        return getSession().createCriteria(getEntityClass());
    }

    /**
     * Gets base criteria update.
     *
     * @return the base criteria update
     */
    protected CriteriaUpdate<T> getBaseCriteriaUpdateImpl() {
        return this.getCriteriaBuilder().createCriteriaUpdate(getEntityClass());
    }

    /**
     * Gets criteria order.
     *
     * @param dataTableCO the data table co
     * @return the criteria order
     */
    Order getCriteriaOrder(DataTableCO dataTableCO) {
        if (dataTableCO.getOrderBy().equalsIgnoreCase("desc")) {
            return Order.desc(isEmpty(dataTableCO.getSortBy()) ? "id" : dataTableCO.getSortBy());
        } else {
            return Order.asc(isEmpty(dataTableCO.getSortBy()) ? "id" : dataTableCO.getSortBy());
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
    T findOne(ID id) {
        return getJpaRepository().findById(id).orElse(null);
    }

}
