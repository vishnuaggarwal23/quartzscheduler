package com.vishnu.aggarwal.quartz.rest.service.repository.jpa;

/*
Created by vishnu on 6/3/18 10:35 AM
*/

import com.vishnu.aggarwal.quartz.rest.entity.User;
import com.vishnu.aggarwal.quartz.rest.repository.jpa.UserRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * The type User repo service.
 */
@Service
@CommonsLog
@Transactional
public class UserRepoService extends BaseRepoService<User, Long> {

    /**
     * The User repository.
     */
    private final UserRepository userRepository;

    @Autowired
    public UserRepoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
//    @Cacheable(value = "isUsernameUnique", key = "#username", unless = "#result == null")
    public Boolean checkIfUsernameIsUnique(String username) {
        return userRepository.countByUsernameAndIsDeleted(username, FALSE) == 0;
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "findUserByUsername", key = "#user.username", beforeInvocation = true),
                    @CacheEvict(value = "findUserById", key = "#user.id", beforeInvocation = true),
                    @CacheEvict(value = "isUsernameUnique", key = "#user.id", beforeInvocation = true)
            },
            put = {
                    @CachePut(value = "findUserByUsername", key = "#user.username", unless = "#result == null"),
                    @CachePut(value = "findUserById", key = "#user.id", unless = "#result == null")
            }
    )
    @SuppressWarnings("unchecked")
    public User save(final User user) {
        return super.save(user);
    }

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     * @throws HibernateException the hibernate exception
     */
    @Cacheable(value = "findUserByUsername", key = "#username", unless = "#result == null")
    public User findByUsername(final String username) throws HibernateException, NoResultException {
        CriteriaQuery<User> criteriaQuery = getBaseCriteriaSelectImpl();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        Root<User> userToken = getRoot(criteriaQuery);
        criteriaQuery
                .where(
                        criteriaBuilder.equal(userToken.<String>get("username"), username),
                        criteriaBuilder.isFalse(userToken.get("isDeleted")),
                        criteriaBuilder.isTrue(userToken.get("accountEnabled")),
                        criteriaBuilder.isFalse(userToken.get("accountExpired")),
                        criteriaBuilder.isFalse(userToken.get("accountLocked")),
                        criteriaBuilder.isFalse(userToken.get("credentialsExpired"))
                );
        return (User) selectQuery(criteriaQuery, TRUE, TRUE, null);
    }

    @Cacheable(value = "findUserById", key = "#id", unless = "#result == null")
    public User findById(Long id) {
        return super.getOne(id);
    }
}
