package com.vishnu.aggarwal.rest.service.repository;

/*
Created by vishnu on 6/3/18 10:35 AM
*/

import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.repository.UserRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hibernate.criterion.Restrictions.eq;

@Service
@CommonsLog
public class UserRepoService extends BaseRepoService<User, Long> {

    @Autowired
    UserRepository userRepository;

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected JpaRepository<User, Long> getJpaRepository() {
        return userRepository;
    }

    public Boolean checkIfUsernameIsUnique(String username) {
        return userRepository.countByUsernameAndIsDeletedAndAccountEnabledAndAccountExpiredAndAccountLockedAndCredentialsExpired(username, FALSE, TRUE, FALSE, FALSE, FALSE) == 0;
    }

    @SuppressWarnings("unchecked")
    public User save(User user) {
        return super.save(user);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) throws HibernateException {
        Criteria criteria = getBaseCriteriaImpl()
                .setFetchMode("roles", FetchMode.JOIN)
                .setReadOnly(TRUE)
                .add(eq("username", username))
                .add(eq("isDeleted", FALSE))
                .add(eq("accountEnabled", TRUE))
                .add(eq("accountExpired", FALSE))
                .add(eq("accountLocked", FALSE))
                .add(eq("credentialsExpired", FALSE))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (User) criteria.uniqueResult();
    }
}
