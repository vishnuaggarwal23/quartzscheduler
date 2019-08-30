package com.vishnu.aggarwal.rest.repository.jpa;

/*
Created by vishnu on 21/4/18 2:37 PM
*/

import com.vishnu.aggarwal.rest.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByTokenAndIsDeleted(String token, Boolean isDeleted);
}
