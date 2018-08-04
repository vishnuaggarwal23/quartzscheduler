package com.vishnu.aggarwal.rest.repository.jpa;

import com.vishnu.aggarwal.rest.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

/**
 * The interface Authority repository.
 */
@Repository
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
