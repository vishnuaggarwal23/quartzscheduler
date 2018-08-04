package com.vishnu.aggarwal.rest.repository.jpa;

/*
Created by vishnu on 21/4/18 2:37 PM
*/

import com.vishnu.aggarwal.rest.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Token repository.
 */
public interface TokenRepository extends JpaRepository<Token, Long> {
    /**
     * Find by token and is deleted token.
     *
     * @param token     the token
     * @param isDeleted the is deleted
     * @return the token
     */
    Token findByTokenAndIsDeleted(String token, Boolean isDeleted);
}
