package com.vishnu.aggarwal.rest.service.repository.jpa;

import com.vishnu.aggarwal.rest.entity.Authority;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserAuthority;
import com.vishnu.aggarwal.rest.repository.jpa.UserAuthorityRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Boolean.FALSE;

/*
Created by vishnu on 20/4/18 2:55 PM
*/

/**
 * The type User authority repo service.
 */
@Service
@CommonsLog
@Transactional
public class UserAuthorityRepoService extends BaseRepoService<UserAuthority, Long> {
    /**
     * The User authority repository.
     */
    private final UserAuthorityRepository userAuthorityRepository;

    @Autowired
    public UserAuthorityRepoService(UserAuthorityRepository userAuthorityRepository) {
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Override
    protected Class<UserAuthority> getEntityClass() {
        return null;
    }

    @Override
    protected JpaRepository<UserAuthority, Long> getJpaRepository() {
        return userAuthorityRepository;
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "findUserAuthorityByUserAndAuthority", key = "#userAuthority.user.toString() + #userAuthority.authority.toString()", beforeInvocation = true)
            },
            put = {
                    @CachePut(value = "findUserAuthorityByUserAndAuthority", key = "#userAuthority.user.toString() + #userAuthority.authority.toString()", unless = "#result == null")
            }
    )
    @SuppressWarnings("unchecked")
    public UserAuthority save(UserAuthority userAuthority) {
        return userAuthorityRepository.save(userAuthority);
    }

    /**
     * Find by user and authority and is deleted user authority.
     *
     * @param user      the user
     * @param authority the authority
     * @return the user authority
     */
    @Cacheable(value = "findUserAuthorityByUserAndAuthority", key = "#user.toString() + #authority.toString()", unless = "#result == null")
    public UserAuthority findByUserAndAuthority(User user, Authority authority) {
        return userAuthorityRepository.findByUserAndAuthorityAndIsDeleted(user, authority, FALSE);
    }
}
