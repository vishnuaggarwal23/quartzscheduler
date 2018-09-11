package com.vishnu.aggarwal.rest.repository.jpa;

import com.vishnu.aggarwal.rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

/**
 * The interface User repository.
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Count by username and is deleted and account enabled and account expired and account locked and credentials expired long.
     *
     * @param username           the username
     * @param isDeleted          the is deleted
     * @param accountEnabled     the account enabled
     * @param accountExpired     the account expired
     * @param accountLocked      the account locked
     * @param credentialsExpired the credentials expired
     * @return the long
     */
    Long countByUsernameAndIsDeletedAndAccountEnabledAndAccountExpiredAndAccountLockedAndCredentialsExpired(String username, Boolean isDeleted, Boolean accountEnabled, Boolean accountExpired, Boolean accountLocked, Boolean credentialsExpired);

    Long countByUsernameAndIsDeleted(String username, Boolean isDeleted);
}
