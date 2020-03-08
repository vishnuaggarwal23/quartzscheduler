package com.vishnu.aggarwal.quartz.rest.service.repository.jpa;

import com.vishnu.aggarwal.quartz.rest.entity.Authority;
import com.vishnu.aggarwal.quartz.rest.entity.User;
import com.vishnu.aggarwal.quartz.rest.entity.UserAuthority;
import com.vishnu.aggarwal.quartz.rest.repository.jpa.UserAuthorityRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

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
    public UserAuthorityRepoService(@NotNull final UserAuthorityRepository userAuthorityRepository) {
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Override
    protected Class<UserAuthority> getEntityClass() {
        return UserAuthority.class;
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
    public UserAuthority save(@NotNull final UserAuthority userAuthority) {
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
    public UserAuthority findByUserAndAuthority(@NotNull final User user, @NotNull final Authority authority) {
        return userAuthorityRepository.findByUserAndAuthorityAndIsDeleted(user, authority, FALSE);
    }

    @Cacheable(value = "countUserAuthorityByUserAndAuthority", key = "#user.toString() + #authority.toString()", unless = "#result == null")
    public int countByUserAndAuthority(@NotNull final User user, @NotNull final Authority authority) {
        return userAuthorityRepository.countByUserAndAuthorityAndIsDeleted(user, authority, FALSE);
    }
}
