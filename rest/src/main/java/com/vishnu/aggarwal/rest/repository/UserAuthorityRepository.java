package com.vishnu.aggarwal.rest.repository;

import com.vishnu.aggarwal.rest.entity.Authority;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

/**
 * The interface User authority repository.
 */
@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
    /**
     * Find by user and authority and is deleted user authority.
     *
     * @param user      the user
     * @param authority the authority
     * @param isDeleted the is deleted
     * @return the user authority
     */
    UserAuthority findByUserAndAuthorityAndIsDeleted(User user, Authority authority, Boolean isDeleted);
}
