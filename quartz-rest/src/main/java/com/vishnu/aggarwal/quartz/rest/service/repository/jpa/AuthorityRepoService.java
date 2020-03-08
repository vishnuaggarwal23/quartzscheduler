package com.vishnu.aggarwal.quartz.rest.service.repository.jpa;

import com.vishnu.aggarwal.quartz.rest.entity.Authority;
import com.vishnu.aggarwal.quartz.rest.repository.jpa.AuthorityRepository;
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
 * The type Authority repo service.
 */
@Service
@CommonsLog
@Transactional
public class AuthorityRepoService extends BaseRepoService<Authority, Long> {

    /**
     * The Authority repository.
     */
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityRepoService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    protected Class<Authority> getEntityClass() {
        return Authority.class;
    }

    @Override
    protected JpaRepository<Authority, Long> getJpaRepository() {
        return authorityRepository;
    }

    /**
     * Find by authority and is deleted authority.
     *
     * @param name the name
     * @return the authority
     */
    @Cacheable(value = "findAuthorityByName", key = "#name", unless = "#result == null")
    public Authority findByName(String name) {
        return authorityRepository.findByNameAndIsDeleted(name, FALSE);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "findAuthorityByName", key = "#authority.name", beforeInvocation = true)
            },
            put = {
                    @CachePut(value = "findAuthorityByName", key = "#authority.name", unless = "#result == null")
            }
    )
    @SuppressWarnings("unchecked")
    public Authority save(Authority authority) {
        return super.save(authority);
    }
}
