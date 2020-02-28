package com.vishnu.aggarwal.quartz.rest.repository.jpa;

import com.vishnu.aggarwal.quartz.rest.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

/**
 * The interface Authority repository.
 */
@Repository
@Transactional
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    /**
     * Find by name and is deleted authority.
     *
     * @param name      the name
     * @param isDeleted the is deleted
     * @return the authority
     */
    Authority findByNameAndIsDeleted(String name, Boolean isDeleted);
}
