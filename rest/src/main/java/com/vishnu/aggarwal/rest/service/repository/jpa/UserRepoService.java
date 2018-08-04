package com.vishnu.aggarwal.rest.service.repository.jpa;

/*
Created by vishnu on 6/3/18 10:35 AM
*/

import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.repository.jpa.UserRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;

/**
 * The type User repo service.
 */
@Service
@CommonsLog
public class UserRepoService extends BaseRepoService<User, Long> {

    /**
     * The User repository.
     */
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

    /**
     * Check if username is unique boolean.
     *
     * @param username the username
     * @return the boolean
     */
    public Boolean checkIfUsernameIsUnique(String username) {
        return userRepository.countByUsernameAndIsDeletedAndAccountEnabledAndAccountExpiredAndAccountLockedAndCredentialsExpired(username, FALSE, TRUE, FALSE, FALSE, FALSE) == 0;
    }

    @SuppressWarnings("unchecked")
    public User save(User user) {
        return super.save(user);
    }

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     * @throws HibernateException the hibernate exception
     */
    @Transactional(readOnly = true)
    public User findByUsername(String username) throws HibernateException, NoResultException {
        CriteriaQuery<User> criteriaQuery = getBaseCriteriaSelectImpl();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        Root<User> userToken = getRoot(criteriaQuery);
        criteriaQuery
                .where(
                        criteriaBuilder.equal(userToken.get("username"), username),
                        criteriaBuilder.isFalse(userToken.get("isDeleted")),
                        criteriaBuilder.isTrue(userToken.get("accountEnabled")),
                        criteriaBuilder.isFalse(userToken.get("accountExpired")),
                        criteriaBuilder.isFalse(userToken.get("accountLocked")),
                        criteriaBuilder.isFalse(userToken.get("credentialsExpired"))
                );
        return (User) selectQuery(criteriaQuery, TRUE, TRUE, null);

        /*Criteria criteria = getBaseCriteriaSelectImpl()
                .setFetchMode("roles", FetchMode.JOIN)
                .setReadOnly(TRUE)
                .add(eq("username", username))
                .add(eq("isDeleted", FALSE))
                .add(eq("accountEnabled", TRUE))
                .add(eq("accountExpired", FALSE))
                .add(eq("accountLocked", FALSE))
                .add(eq("credentialsExpired", FALSE))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (User) criteria.uniqueResult();*/
    }

    /**
     * Is valid user for system access boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public Boolean isValidUserForSystemAccess(User user) {
        return nonNull(user) ? user.isAccountNonExpired() && user.isAccountNonLocked() && user.isCredentialsNonExpired() && user.isEnabled() && !user.getIsDeleted() : FALSE;
    }
}
