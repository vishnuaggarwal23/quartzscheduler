package com.vishnu.aggarwal.rest.service.repository.jpa;

import com.vishnu.aggarwal.rest.entity.Authority;
import com.vishnu.aggarwal.rest.repository.jpa.AuthorityRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/*
Created by vishnu on 20/4/18 2:55 PM
*/

/**
 * The type Authority repo service.
 */
@Service
@CommonsLog
public class AuthorityRepoService extends BaseRepoService<Authority, Long> {

    /**
     * The Authority repository.
     */
    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    protected Class<Authority> getEntityClass() {
        return null;
    }

    @Override
    protected JpaRepository<Authority, Long> getJpaRepository() {
        return authorityRepository;
    }

    /**
     * Find by authority and is deleted authority.
     *
     * @param name      the name
     * @param isDeleted the is deleted
     * @return the authority
     */
    public Authority findByAuthorityAndIsDeleted(String name, Boolean isDeleted) {
        return authorityRepository.findByNameAndIsDeleted(name, isDeleted);
    }

    @SuppressWarnings("unchecked")
    public Authority save(Authority authority) {
        return super.save(authority);
    }
}
