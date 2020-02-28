package com.vishnu.aggarwal.quartz.rest.repository.jpa;

import com.vishnu.aggarwal.quartz.rest.entity.Token;
import com.vishnu.aggarwal.quartz.rest.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

/**
 * The interface User token repository.
 */
@Repository
@Transactional
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    /**
     * Find by token user token.
     *
     * @param token the token
     * @return the user token
     */
    UserToken findByToken(Token token);
}
