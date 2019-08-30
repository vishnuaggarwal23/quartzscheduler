package com.vishnu.aggarwal.rest.service.repository.jpa;

/*
Created by vishnu on 1/3/18 1:00 PM
*/

import com.vishnu.aggarwal.core.co.DataTableCO;
import com.vishnu.aggarwal.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.io.Serializable;

import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@CommonsLog
public abstract class BaseRepoService<T, ID extends Serializable> extends BaseService {
    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    protected abstract Class<T> getEntityClass();

    protected abstract JpaRepository<T, ID> getJpaRepository();

    Root<T> getRoot(CriteriaQuery<T> criteriaQuery) {
        return criteriaQuery.from(getEntityClass());
    }

    Root<T> getRoot(CriteriaDelete<T> criteriaDelete) {
        return criteriaDelete.from(getEntityClass());
    }

    Root<T> getRoot(CriteriaUpdate<T> criteriaUpdate) {
        return criteriaUpdate.from(getEntityClass());
    }

    CriteriaBuilder getCriteriaBuilder() {
        return this.getSession().getCriteriaBuilder();
    }

    /*
    protected Criteria getBaseCriteriaSelectImpl() {
        return getSession().createCriteria(getEntityClass());
    }*/
    CriteriaQuery<T> getBaseCriteriaSelectImpl() {
        return this.getCriteriaBuilder().createQuery(getEntityClass());
    }

    CriteriaUpdate<T> getBaseCriteriaUpdateImpl() {
        return this.getCriteriaBuilder().createCriteriaUpdate(getEntityClass());
    }

    protected CriteriaDelete<T> getBaseCriteriaDeleteImpl() {
        return this.getCriteriaBuilder().createCriteriaDelete(getEntityClass());
    }

    private Order getCriteriaOrder(CriteriaBuilder criteriaBuilder, DataTableCO dataTableCO) {
        if (dataTableCO.getOrderBy().equalsIgnoreCase("desc")) {
            return criteriaBuilder.desc(getRoot(getBaseCriteriaSelectImpl()).get(isEmpty(dataTableCO.getSortBy()) ? "id" : dataTableCO.getSortBy()));
        } else {
            return criteriaBuilder.asc(getRoot(getBaseCriteriaSelectImpl()).get(isEmpty(dataTableCO.getSortBy()) ? "id" : dataTableCO.getSortBy()));
        }
    }

    @Transactional
    Integer updateQuery(CriteriaUpdate<T> criteriaUpdate) {
        return getSession().createQuery(criteriaUpdate).executeUpdate();
    }

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

    @Transactional
    Integer deleteQuery(CriteriaDelete<T> criteriaDelete) {
        return getSession().createQuery(criteriaDelete).executeUpdate();
    }

    @Transactional
    <S extends T> S save(S entity) {
        return getJpaRepository().save(entity);
    }

    @Transactional
    T findOne(ID id) {
        return getJpaRepository().findById(id).orElse(null);
    }

    @Transactional
    T getOne(ID id) {
        return getJpaRepository().getOne(id);
    }

}
