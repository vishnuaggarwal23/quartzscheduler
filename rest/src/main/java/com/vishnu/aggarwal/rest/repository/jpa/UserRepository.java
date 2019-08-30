package com.vishnu.aggarwal.rest.repository.jpa;

import com.vishnu.aggarwal.rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Long countByUsernameAndIsDeletedAndAccountEnabledAndAccountExpiredAndAccountLockedAndCredentialsExpired(String username, Boolean isDeleted, Boolean accountEnabled, Boolean accountExpired, Boolean accountLocked, Boolean credentialsExpired);

    Long countByUsernameAndIsDeleted(String username, Boolean isDeleted);
}
