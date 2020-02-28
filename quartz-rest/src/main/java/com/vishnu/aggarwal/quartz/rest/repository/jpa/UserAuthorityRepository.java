package com.vishnu.aggarwal.quartz.rest.repository.jpa;

import com.vishnu.aggarwal.quartz.rest.entity.Authority;
import com.vishnu.aggarwal.quartz.rest.entity.User;
import com.vishnu.aggarwal.quartz.rest.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

/**
 * The interface User authority repository.
 */
@Repository
@Transactional
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
